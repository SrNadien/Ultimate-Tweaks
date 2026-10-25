package nadiendev.ultimatetweaks.client.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID, value = Dist.CLIENT)
public final class TrashCanClientEvents {

    private static final int LEFT_MOUSE_BUTTON = 0;

    private TrashCanClientEvents() {
    }

    @SubscribeEvent
    public static void onRender(ScreenEvent.Render.Post event) {
        if (TrashCanButton.appliesTo(event.getScreen())) {
            TrashCanButton.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY());
        }
        if (TrashCanClearButton.appliesTo(event.getScreen())
                && event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            TrashCanClearButton.render(event.getGuiGraphics(), screen, event.getMouseX(), event.getMouseY());
        }
    }

    @SubscribeEvent
    public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != LEFT_MOUSE_BUTTON) {
            return;
        }
        if (TrashCanButton.appliesTo(event.getScreen())
                && TrashCanButton.contains(event.getMouseX(), event.getMouseY())) {
            TrashCanButton.press();
            event.setCanceled(true);
            return;
        }
        if (TrashCanClearButton.appliesTo(event.getScreen())
                && event.getScreen() instanceof AbstractContainerScreen<?> screen
                && TrashCanClearButton.contains(screen, event.getMouseX(), event.getMouseY())) {
            TrashCanClearButton.press();
            event.setCanceled(true);
        }
    }
}
