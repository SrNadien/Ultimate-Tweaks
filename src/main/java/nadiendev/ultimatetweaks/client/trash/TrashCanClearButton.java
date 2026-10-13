package nadiendev.ultimatetweaks.client.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.trash.TrashCan;
import nadiendev.ultimatetweaks.trash.TrashCanCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public final class TrashCanClearButton {

    public static final String LABEL_KEY = TrashCan.TITLE_KEY + ".clear";

    private static final Identifier SPRITE = Identifier.withDefaultNamespace("widget/button");
    private static final Identifier SPRITE_HIGHLIGHTED =
            Identifier.withDefaultNamespace("widget/button_highlighted");

    private static final int HEIGHT = 12;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_BASE_HEIGHT = 114;
    private static final int SLOT_SIZE = 18;
    private static final int PADDING = 6;
    private static final int RIGHT_MARGIN = 8;
    private static final int TOP_OFFSET = 3;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int TEXT_COLOR_HOVER = 0xFFFFFFA0;

    private TrashCanClearButton() {
    }

    public static boolean appliesTo(Screen screen) {
        return screen instanceof ContainerScreen
                && screen.getTitle().getContents() instanceof TranslatableContents contents
                && TrashCan.TITLE_KEY.equals(contents.getKey());
    }

    public static boolean contains(ContainerScreen screen, double mouseX, double mouseY) {
        int x = x(screen);
        int y = y(screen);
        return mouseX >= x && mouseX < x + width() && mouseY >= y && mouseY < y + HEIGHT;
    }

    public static void render(GuiGraphicsExtractor graphics, ContainerScreen screen, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        boolean hovered = contains(screen, mouseX, mouseY);
        int x = x(screen);
        int y = y(screen);
        int width = width();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, hovered ? SPRITE_HIGHLIGHTED : SPRITE, x, y, width, HEIGHT);
        graphics.centeredText(font, Component.translatable(LABEL_KEY), x + width / 2,
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

    private static int x(ContainerScreen screen) {
        return (screen.width - GUI_WIDTH) / 2 + GUI_WIDTH - RIGHT_MARGIN - width();
    }

    private static int y(ContainerScreen screen) {
        int guiHeight = GUI_BASE_HEIGHT + screen.getMenu().getRowCount() * SLOT_SIZE;
        return (screen.height - guiHeight) / 2 + TOP_OFFSET;
    }
}
