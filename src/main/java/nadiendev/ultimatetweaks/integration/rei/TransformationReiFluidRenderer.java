package nadiendev.ultimatetweaks.integration.rei;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public final class TransformationReiFluidRenderer implements EntryRenderer<FluidStack> {

    public static final TransformationReiFluidRenderer INSTANCE = new TransformationReiFluidRenderer();

    private static final int WATER_COLOR = 0x3F76E4;
    private static final float CHANNEL_MAX = 255.0F;

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

        Fluid fluid = stack.getFluid();
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(extensions.getStillTexture());

        int color = extensions.getTintColor() & 0xFFFFFF;
        if (color == 0) {
            color = fluid == Fluids.WATER ? WATER_COLOR : 0xFFFFFF;
        }

        graphics.blit(bounds.x + 1, bounds.y + 1, 0, bounds.width - 2, bounds.height - 2, sprite,
                (color >> 16 & 0xFF) / CHANNEL_MAX, (color >> 8 & 0xFF) / CHANNEL_MAX, (color & 0xFF) / CHANNEL_MAX, 1.0F);
    }

    @Override
    public Tooltip getTooltip(EntryStack<FluidStack> entry, TooltipContext context) {
        return Tooltip.create(context.getPoint(), entry.getValue().getName());
    }
}
