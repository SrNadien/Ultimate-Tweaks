package nadiendev.ultimatetweaks.optimization;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID)
public final class OptimizationEvents {

    private OptimizationEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AdvancementThrottle.flush(player);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        AdvancementThrottle.forget(event.getEntity().getUUID());
        SlotMemory.forget(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        ItemListEpoch.invalidate();
    }
}
