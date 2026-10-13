package nadiendev.ultimatetweaks.client.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.trash.TrashCan;
import nadiendev.ultimatetweaks.trash.TrashCanCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public final class TrashCanClearButton {

    public static final String LABEL_KEY = TrashCan.TITLE_KEY + ".clear";

    private static final ResourceLocation SPRITE = ResourceLocation.withDefaultNamespace("widget/button");
    private static final ResourceLocation SPRITE_HIGHLIGHTED =
            ResourceLocation.withDefaultNamespace("widget/button_highlighted");

    private static final int HEIGHT = 12;
    private static final int PADDING = 6;
    private static final int RIGHT_MARGIN = 8;
    private static final int TOP_OFFSET = 3;
    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int TEXT_COLOR_HOVER = 0xFFFFA0;

    private TrashCanClearButton() {
    }

    public static boolean appliesTo(Screen screen) {
        return screen instanceof ContainerScreen
                && screen.getTitle().getContents() instanceof TranslatableContents contents
                && TrashCan.TITLE_KEY.equals(contents.getKey());
    }

    public static boolean contains(AbstractContainerScreen<?> screen, double mouseX, double mouseY) {
        int x = x(screen);
        int y = y(screen);
        return mouseX >= x && mouseX < x + width() && mouseY >= y && mouseY < y + HEIGHT;
    }

    public static void render(GuiGraphics graphics, AbstractContainerScreen<?> screen, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        boolean hovered = contains(screen, mouseX, mouseY);
        int x = x(screen);
        int y = y(screen);
        int width = width();
        graphics.blitSprite(hovered ? SPRITE_HIGHLIGHTED : SPRITE, x, y, width, HEIGHT);
        graphics.drawCenteredString(font, Component.translatable(LABEL_KEY), x + width / 2,
                y + (HEIGHT - font.lineHeight) / 2 + 1, hovered ? TEXT_COLOR_HOVER : TEXT_COLOR);
    }

    public static void press() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPacketListener connection = minecraft.getConnection();
        if (connection == null) {
            return;
        }
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        connection.sendCommand(TrashCanCommand.CLEAR_LINE);
    }

    private static int width() {
        return Minecraft.getInstance().font.width(Component.translatable(LABEL_KEY)) + PADDING * 2;
    }

    private static int x(AbstractContainerScreen<?> screen) {
        return screen.getGuiLeft() + screen.getXSize() - RIGHT_MARGIN - width();
    }

    private static int y(AbstractContainerScreen<?> screen) {
        return screen.getGuiTop() + TOP_OFFSET;
    }
}
