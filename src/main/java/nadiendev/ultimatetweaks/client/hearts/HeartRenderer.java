package nadiendev.ultimatetweaks.client.hearts;

import com.mojang.blaze3d.systems.RenderSystem;
import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public final class HeartRenderer {

    private static final ResourceLocation CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container");
    private static final ResourceLocation ABSORBING_FULL = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full");
    private static final ResourceLocation ABSORBING_HALF = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_half");
    private static final ResourceLocation TINT_FULL = ResourceLocation.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "hud/heart/tint_full");
    private static final ResourceLocation TINT_HALF = ResourceLocation.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "hud/heart/tint_half");

    private static final int COLUMNS = 10;
    private static final int HEALTH_PER_LAYER = COLUMNS * 2;
    private static final int HEART_SIZE = 9;
    private static final int HEART_STEP = 8;
    private static final int ROW_HEIGHT = 10;
    private static final int MAX_ABSORPTION_ROWS = 3;
    private static final int LOW_HEALTH = 4;
    private static final int REGENERATION_PERIOD = 15;

    private HeartRenderer() {
    }

    public static boolean applies(Minecraft minecraft, Player player) {
        if (!TweaksClientConfig.HEARTS_ENABLED.get() || minecraft.gameMode == null || minecraft.level == null
                || !minecraft.gameMode.canHurtPlayer() || minecraft.level.getLevelData().isHardcore()) {
            return false;
        }
        if (player.hasEffect(MobEffects.POISON) || player.hasEffect(MobEffects.WITHER) || player.isFullyFrozen()) {
            return false;
        }
        return Mth.ceil(player.getMaxHealth()) > TweaksClientConfig.HEARTS_THRESHOLD.get();
    }

    public static void render(GuiGraphics graphics, Gui gui, Player player) {
        int left = graphics.guiWidth() / 2 - 91;
        int top = graphics.guiHeight() - gui.leftHeight;
        int health = Mth.ceil(player.getHealth());
        int layer = health <= 0 ? -1 : (health - 1) / HEALTH_PER_LAYER;
        int topHalves = layer < 0 ? 0 : health - layer * HEALTH_PER_LAYER;
        RandomSource random = RandomSource.create(player.tickCount * 312871L);
        int jumping = player.hasEffect(MobEffects.REGENERATION) ? player.tickCount % REGENERATION_PERIOD : -1;
        boolean shaking = health <= LOW_HEALTH;

        for (int column = 0; column < COLUMNS; column++) {
            int x = left + column * HEART_STEP;
            int y = top;
            if (shaking) {
                y += random.nextInt(2);
            }
            if (column == jumping) {
                y -= 2;
            }
            graphics.blitSprite(CONTAINER, x, y, HEART_SIZE, HEART_SIZE);
            if (layer > 0) {
                tinted(graphics, TINT_FULL, layer - 1, x, y);
            }
            int halves = Mth.clamp(topHalves - column * 2, 0, 2);
            if (halves == 2) {
                tinted(graphics, TINT_FULL, layer, x, y);
            } else if (halves == 1) {
                tinted(graphics, TINT_HALF, layer, x, y);
            }
        }

        int absorption = Mth.ceil(player.getAbsorptionAmount());
        int absorptionHearts = Math.min(Mth.ceil(absorption / 2.0F), MAX_ABSORPTION_ROWS * COLUMNS);
        int rows = 1 + Mth.ceil(absorptionHearts / (float) COLUMNS);
        for (int i = 0; i < absorptionHearts; i++) {
            int x = left + (i % COLUMNS) * HEART_STEP;
            int y = top - (1 + i / COLUMNS) * ROW_HEIGHT;
            boolean half = i * 2 + 1 == absorption;
            graphics.blitSprite(CONTAINER, x, y, HEART_SIZE, HEART_SIZE);
            graphics.blitSprite(half ? ABSORBING_HALF : ABSORBING_FULL, x, y, HEART_SIZE, HEART_SIZE);
        }
        gui.leftHeight += rows * ROW_HEIGHT;
    }

    private static void tinted(GuiGraphics graphics, ResourceLocation sprite, int layer, int x, int y) {
        float[] color = HeartColors.of(layer);
        RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
        graphics.blitSprite(sprite, x, y, HEART_SIZE, HEART_SIZE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
