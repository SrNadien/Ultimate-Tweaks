package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.Team;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ZoneMovementEvents {

    private static final class PlayerState {
        @Nullable
        private UUID zoneId;
        @Nullable
        private ResourceKey<Level> dimension;
        private long chunkKey = Long.MIN_VALUE;
        @Nullable
        private Vec3 lastGood;
        @Nullable
        private GameType previousMode;
        @Nullable
        private UUID deathZone;
        private boolean timeLocked;
        private boolean weatherLocked;
        private int healTicks;
        private int feedTicks;
    }

    private final Map<UUID, PlayerState> states = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        PlayerState state = states.computeIfAbsent(player.getUUID(), id -> new PlayerState());
        long chunkKey = player.chunkPosition().toLong();
        ResourceKey<Level> dimension = player.level().dimension();
        boolean recheck = player.tickCount % 20 == 0;
        if (recheck || chunkKey != state.chunkKey || !Objects.equals(dimension, state.dimension)) {
            boolean sameDimension = Objects.equals(dimension, state.dimension);
            Team current = ZoneLookup.zoneAt(player);
            UUID currentId = current == null ? null : current.getId();
            if (!Objects.equals(currentId, state.zoneId)) {
                if (sameDimension && transitionDenied(player, state, current)) {
                    return;
                }
                transition(player, state, current);
            }
            state.chunkKey = chunkKey;
            state.dimension = dimension;
        }
        state.lastGood = player.position();
        if (state.zoneId != null) {
            Team team = ZoneLookup.teamById(state.zoneId);
            if (team != null) {
                applyPeriodic(player, state, team);
            }
        }
    }

    private boolean transitionDenied(ServerPlayer player, PlayerState state, @Nullable Team entering) {
        if (state.lastGood == null || ZoneLookup.bypasses(player)) {
            return false;
        }
        Team leaving = state.zoneId == null ? null : ZoneLookup.teamById(state.zoneId);
        if (entering != null && !ZoneLookup.isMember(entering, player) && !ZoneFlags.state(entering, ZoneFlagDef.ENTRY)) {
            pushBack(player, state, ZoneFlags.text(entering, ZoneFlagDef.ENTRY_DENY_MESSAGE), entering);
            return true;
        }
        if (leaving != null && !ZoneLookup.isMember(leaving, player) && !ZoneFlags.state(leaving, ZoneFlagDef.EXIT)) {
            boolean teleported = state.lastGood.distanceToSqr(player.position()) > 36.0D;
            if (teleported && ZoneFlags.state(leaving, ZoneFlagDef.EXIT_VIA_TELEPORT)) {
                return false;
            }
            pushBack(player, state, ZoneFlags.text(leaving, ZoneFlagDef.EXIT_DENY_MESSAGE), leaving);
            return true;
        }
        return false;
    }

    private void pushBack(ServerPlayer player, PlayerState state, String message, Team team) {
        Vec3 back = state.lastGood;
        player.server.execute(() -> player.teleportTo(player.serverLevel(), back.x, back.y, back.z, player.getYRot(), player.getXRot()));
        if (!message.isBlank()) {
            player.displayClientMessage(ZoneLookup.legacy(message.replace("{team}", team.getName().getString())), true);
        }
    }

    private void transition(ServerPlayer player, PlayerState state, @Nullable Team entering) {
        Team leaving = state.zoneId == null ? null : ZoneLookup.teamById(state.zoneId);
        if (leaving != null) {
            leave(player, state, leaving);
        }
        state.zoneId = entering == null ? null : entering.getId();
        state.healTicks = 0;
        state.feedTicks = 0;
        if (entering != null) {
            enter(player, state, entering);
        }
    }

    private void leave(ServerPlayer player, PlayerState state, Team team) {
        say(player, team, ZoneFlags.text(team, ZoneFlagDef.FAREWELL), ZoneFlags.text(team, ZoneFlagDef.FAREWELL_TITLE));
        restoreGameMode(player, state);
        if (state.timeLocked) {
            state.timeLocked = false;
            ServerLevel level = player.serverLevel();
            player.connection.send(new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(),
                    level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        }
        if (state.weatherLocked) {
            state.weatherLocked = false;
            sendWeather(player, player.serverLevel().getRainLevel(1.0F), player.serverLevel().getThunderLevel(1.0F));
        }
    }

    private void enter(ServerPlayer player, PlayerState state, Team team) {
        say(player, team, ZoneFlags.text(team, ZoneFlagDef.GREETING), ZoneFlags.text(team, ZoneFlagDef.GREETING_TITLE));
        GameType mode = parseGameMode(ZoneFlags.text(team, ZoneFlagDef.GAME_MODE));
        if (mode != null && player.gameMode.getGameModeForPlayer() != mode) {
            if (state.previousMode == null) {
                state.previousMode = player.gameMode.getGameModeForPlayer();
            }
            player.setGameMode(mode);
        }
    }

    private void restoreGameMode(ServerPlayer player, PlayerState state) {
        if (state.previousMode != null) {
            player.setGameMode(state.previousMode);
            state.previousMode = null;
        }
    }

    @Nullable
    private static Vec3 parsePosition(String text) {
        String[] parts = text.trim().split("\s+");
        if (parts.length != 3) {
            return null;
        }
        try {
            return new Vec3(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    private static GameType parseGameMode(String name) {
        if (name.isBlank()) {
            return null;
        }
        GameType type = GameType.byName(name.trim().toLowerCase(Locale.ROOT), null);
        return type;
    }

    private static void say(ServerPlayer player, Team team, String chat, String title) {
        if (!chat.isBlank()) {
            player.sendSystemMessage(ZoneLookup.legacy(format(chat, team)));
        }
        if (!title.isBlank()) {
            String[] parts = format(title, team).split("\\\\n", 2);
            player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
            player.connection.send(new ClientboundSetTitleTextPacket(ZoneLookup.legacy(parts[0])));
            if (parts.length > 1) {
                player.connection.send(new ClientboundSetSubtitleTextPacket(ZoneLookup.legacy(parts[1])));
            }
        }
    }

    private static String format(String text, Team team) {
        return text.replace("{team}", team.getName().getString());
    }

    private void applyPeriodic(ServerPlayer player, PlayerState state, Team team) {
        if (!ZoneFlags.state(team, ZoneFlagDef.NATURAL_HUNGER_DRAIN)) {
            player.getFoodData().setExhaustion(0.0F);
        }
        int healDelay = ZoneFlags.integer(team, ZoneFlagDef.HEAL_DELAY) * 20;
        if (healDelay > 0 && ++state.healTicks >= healDelay) {
            state.healTicks = 0;
            heal(player, ZoneFlags.integer(team, ZoneFlagDef.HEAL_AMOUNT),
                    (float) ZoneFlags.decimal(team, ZoneFlagDef.HEAL_MIN_HEALTH),
                    (float) ZoneFlags.decimal(team, ZoneFlagDef.HEAL_MAX_HEALTH));
        }
        int feedDelay = ZoneFlags.integer(team, ZoneFlagDef.FEED_DELAY) * 20;
        if (feedDelay > 0 && ++state.feedTicks >= feedDelay) {
            state.feedTicks = 0;
            int amount = ZoneFlags.integer(team, ZoneFlagDef.FEED_AMOUNT);
            int min = ZoneFlags.integer(team, ZoneFlagDef.FEED_MIN_HUNGER);
            int max = ZoneFlags.integer(team, ZoneFlagDef.FEED_MAX_HUNGER);
            int food = player.getFoodData().getFoodLevel();
            if (amount > 0 && food < max) {
                player.getFoodData().setFoodLevel(Math.min(max, food + amount));
            } else if (amount < 0 && food > min) {
                player.getFoodData().setFoodLevel(Math.max(min, food + amount));
            }
        }
        if (player.tickCount % 20 == 0) {
            lockTime(player, state, team);
        }
        if (player.tickCount % 100 == 0) {
            lockWeather(player, state, team);
        }
    }

    private static void heal(ServerPlayer player, int amount, float min, float max) {
        float health = player.getHealth();
        if (amount > 0 && health < max) {
            player.setHealth(Math.min(max, health + amount));
        } else if (amount < 0 && health > min) {
            DamageSources sources = player.damageSources();
            player.hurt(sources.magic(), Math.min(-amount, health - min));
        }
    }

    private void lockTime(ServerPlayer player, PlayerState state, Team team) {
        String value = ZoneFlags.text(team, ZoneFlagDef.TIME_LOCK).trim().toLowerCase(Locale.ROOT);
        Long ticks = parseTime(value);
        if (ticks == null) {
            return;
        }
        state.timeLocked = true;
        player.connection.send(new ClientboundSetTimePacket(player.serverLevel().getGameTime(), ticks, false));
    }

    @Nullable
    private static Long parseTime(String value) {
        return switch (value) {
            case "" -> null;
            case "day" -> 1000L;
            case "noon" -> 6000L;
            case "night" -> 13000L;
            case "midnight" -> 18000L;
            default -> {
                try {
                    yield Long.parseLong(value);
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
        };
    }

    private void lockWeather(ServerPlayer player, PlayerState state, Team team) {
        String value = ZoneFlags.text(team, ZoneFlagDef.WEATHER_LOCK).trim().toLowerCase(Locale.ROOT);
        if (!value.equals("clear") && !value.equals("rain")) {
            return;
        }
        state.weatherLocked = true;
        sendWeather(player, value.equals("rain") ? 1.0F : 0.0F, 0.0F);
    }

    private static void sendWeather(ServerPlayer player, float rain, float thunder) {
        player.connection.send(new ClientboundGameEventPacket(
                rain > 0.0F ? ClientboundGameEventPacket.START_RAINING : ClientboundGameEventPacket.STOP_RAINING, 0.0F));
        player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, rain));
        player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, thunder));
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerState state = states.remove(event.getEntity().getUUID());
        if (state != null && event.getEntity() instanceof ServerPlayer player) {
            restoreGameMode(player, state);
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerState state = states.get(player.getUUID());
            Team team = ZoneLookup.zoneAt(player);
            if (state != null) {
                state.deathZone = team == null ? null : team.getId();
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerState state = states.get(event.getEntity().getUUID());
        if (state != null && state.deathZone != null && event.getEntity() instanceof ServerPlayer respawned) {
            Team team = ZoneLookup.teamById(state.deathZone);
            state.deathZone = null;
            Vec3 spawn = team == null ? null : parsePosition(ZoneFlags.text(team, ZoneFlagDef.SPAWN));
            if (spawn != null) {
                respawned.teleportTo(spawn.x, spawn.y, spawn.z);
            }
        }
        if (state != null) {
            state.zoneId = null;
            state.chunkKey = Long.MIN_VALUE;
            state.lastGood = null;
            state.previousMode = null;
            state.timeLocked = false;
            state.weatherLocked = false;
        }
    }
}
