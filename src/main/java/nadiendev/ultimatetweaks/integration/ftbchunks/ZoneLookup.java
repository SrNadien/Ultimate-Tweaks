package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class ZoneLookup {

    private ZoneLookup() {
    }

    @Nullable
    public static Team zoneAt(@Nullable LevelAccessor accessor, BlockPos pos) {
        if (!TweaksCommonConfig.zoneEnabled() || !(accessor instanceof ServerLevel level)
                || !FTBChunksAPI.api().isManagerLoaded()) {
            return null;
        }
        ClaimedChunk chunk = FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(level, pos));
        if (chunk == null) {
            return null;
        }
        Team team = chunk.getTeamData().getTeam();
        if (TweaksCommonConfig.zoneOnlyServerTeams() && !team.isServerTeam()) {
            return null;
        }
        return team;
    }

    @Nullable
    public static Team zoneAt(Entity entity) {
        return zoneAt(entity.level(), entity.blockPosition());
    }

    @Nullable
    public static Team teamById(UUID id) {
        return FTBTeamsAPI.api().getManager().getTeamByID(id).orElse(null);
    }

    public static boolean denies(@Nullable LevelAccessor level, BlockPos pos, ZoneFlagDef def) {
        Team team = zoneAt(level, pos);
        return team != null && !ZoneFlags.state(team, def);
    }

    public static boolean denies(@Nullable LevelAccessor level, Vec3 pos, ZoneFlagDef def) {
        return denies(level, BlockPos.containing(pos), def);
    }

    public static boolean bypasses(ServerPlayer player) {
        return switch (TweaksCommonConfig.zoneBypassLevel()) {
            case 0 -> true;
            case 1 -> player.permissions().hasPermission(Permissions.COMMANDS_MODERATOR);
            case 2 -> player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
            case 3 -> player.permissions().hasPermission(Permissions.COMMANDS_ADMIN);
            case 4 -> player.permissions().hasPermission(Permissions.COMMANDS_OWNER);
            default -> false;
        };
    }

    public static boolean isMember(Team team, ServerPlayer player) {
        return team.getRankForPlayer(player.getUUID()).isMemberOrBetter();
    }

    public static boolean deniesPlayer(ServerPlayer player, BlockPos pos, ZoneFlagDef def) {
        if (bypasses(player)) {
            return false;
        }
        Team team = zoneAt(player.level(), pos);
        if (team == null || isMember(team, player) || ZoneFlags.state(team, def)) {
            return false;
        }
        notifyDenied(player, team);
        return true;
    }

    public static boolean deniesPlayerSilent(ServerPlayer player, BlockPos pos, ZoneFlagDef def) {
        if (bypasses(player)) {
            return false;
        }
        Team team = zoneAt(player.level(), pos);
        return team != null && !isMember(team, player) && !ZoneFlags.state(team, def);
    }

    public static boolean deniesPlayer(ServerPlayer player, ZoneFlagDef def) {
        return deniesPlayer(player, player.blockPosition(), def);
    }

    public static void notifyDenied(ServerPlayer player, Team team) {
        String message = ZoneFlags.text(team, ZoneFlagDef.DENY_MESSAGE);
        if (!message.isBlank()) {
            player.sendSystemMessage(legacy(message.replace("{team}", team.getName().getString())), true);
        }
    }

    public static Component legacy(String text) {
        MutableComponent result = Component.empty();
        StringBuilder segment = new StringBuilder();
        Style style = Style.EMPTY;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            ChatFormatting format = c == '&' && i + 1 < text.length()
                    ? ChatFormatting.getByCode(text.charAt(i + 1)) : null;
            if (format == null) {
                segment.append(c);
                continue;
            }
            if (!segment.isEmpty()) {
                result.append(Component.literal(segment.toString()).withStyle(style));
                segment.setLength(0);
            }
            style = format == ChatFormatting.RESET ? Style.EMPTY : style.applyFormat(format);
            i++;
        }
        if (!segment.isEmpty()) {
            result.append(Component.literal(segment.toString()).withStyle(style));
        }
        return result;
    }
}
