package nadiendev.ultimatetweaks.client.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import nadiendev.ultimatetweaks.trash.TrashCan;
import nadiendev.ultimatetweaks.trash.TrashCanCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.fml.ModList;

public final class TrashCanButton {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "textures/gui/trash_can_button.png");

    private static final int SIZE = 18;
    private static final int X = 2;
    private static final int Y = 2;

    private TrashCanButton() {
    }

    public static boolean ftbSidebarProvidesButton() {
        return ModList.get().isLoaded(TrashCanSidebarPack.FTB_LIBRARY);
    }

    public static boolean appliesTo(Screen screen) {
        boolean inventory = screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen;
        if (!inventory && !TrashCanClearButton.appliesTo(screen)) {
            return false;
        }
        if (!TweaksClientConfig.TRASH_CAN_BUTTON.get() || ftbSidebarProvidesButton()
                || ModList.get().isLoaded(TrashCanSidebarPack.FTB_ESSENTIALS)) {
            return false;
        }
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        return connection != null && connection.getCommands().getRoot().getChild(TrashCanCommand.ROOT) != null;
    }

    public static boolean contains(double mouseX, double mouseY) {
        return mouseX >= X && mouseX < X + SIZE && mouseY >= Y && mouseY < Y + SIZE;
    }

    public static void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        boolean hovered = contains(mouseX, mouseY);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, X, Y, 0, hovered ? SIZE : 0, SIZE, SIZE, SIZE, SIZE * 2);
        if (hovered) {
            graphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.translatable(TrashCan.TITLE_KEY),
                    mouseX, mouseY);
        }
    }

    public static void press() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener connection = minecraft.getConnection();
        if (connection == null) {
            return;
        }
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        connection.sendCommand(TrashCanCommand.LINE);
    }
}
