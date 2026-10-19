package nadiendev.ultimatetweaks.integration.thaumaturge;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID)
public final class ThaumaturgeKnowledgeGuard {

    private static final String THAUMATURGE = "thaumaturge";
    private static final ResourceLocation KNOWLEDGE = ResourceLocation.fromNamespaceAndPath(THAUMATURGE, "knowledge");

    private static final Map<UUID, Integer> PENDING = new ConcurrentHashMap<>();
    private static int saveCountdown;

    private ThaumaturgeKnowledgeGuard() {
    }

    private static boolean active() {
        return TweaksCommonConfig.thaumaturgeGuardEnabled() && ModList.get().isLoaded(THAUMATURGE);
    }

    private static void resync(ServerPlayer player) {
        AttachmentType<?> type = NeoForgeRegistries.ATTACHMENT_TYPES.get(KNOWLEDGE);
        if (type != null && player.hasData(type)) {
            player.syncData(type);
        }
    }

    private static void schedule(ServerPlayer player) {
        PENDING.put(player.getUUID(), TweaksCommonConfig.thaumaturgeResyncDelayTicks());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (active() && event.getEntity() instanceof ServerPlayer player) {
            resync(player);
            schedule(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (active() && event.getEntity() instanceof ServerPlayer player) {
            resync(player);
            schedule(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (active() && event.getEntity() instanceof ServerPlayer player) {
            resync(player);
            schedule(player);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!active()) {
            return;
        }
        MinecraftServer server = event.getServer();
        Iterator<Map.Entry<UUID, Integer>> iterator = PENDING.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int left = entry.getValue() - 1;
            if (left > 0) {
                entry.setValue(left);
                continue;
            }
            iterator.remove();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player != null) {
                resync(player);
            }
        }
        int interval = TweaksCommonConfig.thaumaturgeSaveIntervalTicks();
        if (interval <= 0) {
            saveCountdown = 0;
            return;
        }
        if (++saveCountdown >= interval) {
            saveCountdown = 0;
            server.getPlayerList().saveAll();
        }
    }
}
