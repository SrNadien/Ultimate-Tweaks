package nadiendev.ultimatetweaks.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.transformation.view.TransformationKind;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TransformationJeiCategory implements IRecipeCategory<TransformationView> {

    private static final Identifier ARROW_TEXTURE =
            Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final int NOTE_COLOR = 0xFF808080;

    private final TransformationKind kind;
    private final IRecipeType<TransformationView> type;
    private final IDrawable icon;
    private final IDrawable arrow;

    public TransformationJeiCategory(IGuiHelper helper, TransformationKind kind) {
        this.kind = kind;
        this.type = IRecipeType.create(UltimateTweaksCore.MOD_ID, kind.path(), TransformationView.class);
        this.icon = helper.createDrawableItemStack(kind.icon());
        this.arrow = helper.drawableBuilder(ARROW_TEXTURE, 176, 14, 24, 17).setTextureSize(256, 256).build();
    }

    public IRecipeType<TransformationView> type() {
        return type;
    }

    @Override
    public IRecipeType<TransformationView> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return kind.title();
    }

    @Override
    public int getWidth() {
        return TransformationView.WIDTH;
    }

    @Override
    public int getHeight() {
        return TransformationView.HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public Identifier getIdentifier(TransformationView view) {
        return view.id();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransformationView view, IFocusGroup focuses) {
        int input = 0;
        for (var stacks : view.itemInputs()) {
            builder.addSlot(RecipeIngredientRole.INPUT, TransformationView.slotX(TransformationView.INPUT_X, input),
                    TransformationView.slotY(input)).addIngredients(VanillaTypes.ITEM_STACK, stacks);
            input++;
        }
        for (var stacks : view.fluidInputs()) {
            builder.addSlot(RecipeIngredientRole.INPUT, TransformationView.slotX(TransformationView.INPUT_X, input),
                    TransformationView.slotY(input)).addIngredients(NeoForgeTypes.FLUID_STACK, stacks);
            input++;
        }

        int output = 0;
        for (var stack : view.itemOutputs()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, TransformationView.slotX(TransformationView.OUTPUT_X, output),
                    TransformationView.slotY(output)).add(VanillaTypes.ITEM_STACK, stack);
            output++;
        }
        for (var stack : view.fluidOutputs()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, TransformationView.slotX(TransformationView.OUTPUT_X, output),
                    TransformationView.slotY(output)).add(NeoForgeTypes.FLUID_STACK, stack);
            output++;
        }
    }

    @Override
    public void draw(TransformationView view, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, TransformationView.ARROW_X, TransformationView.ARROW_Y);
        int line = 0;
        for (Component note : view.notes()) {
            graphics.text(Minecraft.getInstance().font, note, TransformationView.INPUT_X,
                    TransformationView.NOTES_Y + line * TransformationView.NOTE_HEIGHT, NOTE_COLOR, false);
            line++;
        }
    }
}
