package nadiendev.ultimatetweaks.client.hearts;

import nadiendev.ultimatetweaks.UltimateTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = UltimateTweaks.MOD_ID, value = Dist.CLIENT)
public final class HeartClientEvents {

    private HeartClientEvents() {
    }

    @SubscribeEvent
    public static void onGuiLayer(RenderGuiLayerEvent.Pre event) {
        if (!VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName())) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || !(minecraft.getCameraEntity() instanceof Player player)
                || !HeartRenderer.applies(minecraft, player)) {
            return;
        }
        HeartRenderer.render(event.getGuiGraphics(), minecraft.gui, player);
        event.setCanceled(true);
    }
}
