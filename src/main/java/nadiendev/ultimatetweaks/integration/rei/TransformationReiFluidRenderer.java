package nadiendev.ultimatetweaks.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.client.fluid.ClientFluidStackHooks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;

public final class TransformationReiFluidRenderer implements EntryRenderer<FluidStack> {

    public static final TransformationReiFluidRenderer INSTANCE = new TransformationReiFluidRenderer();

    private static final int OPAQUE = 0xFF000000;

    private TransformationReiFluidRenderer() {
    }

    public static EntryStack<FluidStack> stack(Fluid fluid, long amount) {
        return EntryStacks.of(fluid, amount).setting(EntryStack.Settings.RENDERER, ignored -> INSTANCE);
    }

    @Override
    public void render(EntryStack<FluidStack> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY,
                       float delta) {
        FluidStack stack = entry.getValue();
        if (stack.isEmpty()) {
            return;
        }

        TextureAtlasSprite sprite = ClientFluidStackHooks.getStillTexture(stack);
        int color = ClientFluidStackHooks.getColor(stack) | OPAQUE;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, bounds.x + 1, bounds.y + 1, bounds.width - 2,
                bounds.height - 2, color);
    }

    @Override
    public Tooltip getTooltip(EntryStack<FluidStack> entry, TooltipContext context) {
        return Tooltip.create(context.getPoint(), entry.getValue().getName());
    }
}
