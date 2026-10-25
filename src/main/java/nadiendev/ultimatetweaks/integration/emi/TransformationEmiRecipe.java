package nadiendev.ultimatetweaks.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TransformationEmiRecipe implements EmiRecipe {

    private static final int TEXT_COLOR = 0xFF808080;

    private final EmiRecipeCategory category;
    private final TransformationView view;
    private final List<EmiIngredient> inputs = new ArrayList<>();
    private final List<EmiStack> outputs = new ArrayList<>();

    public TransformationEmiRecipe(EmiRecipeCategory category, TransformationView view) {
        this.category = category;
        this.view = view;

        for (List<ItemStack> alternatives : view.itemInputs()) {
            inputs.add(EmiIngredient.of(alternatives.stream().map(EmiStack::of).toList()));
        }
        for (List<FluidStack> alternatives : view.fluidInputs()) {
            inputs.add(EmiIngredient.of(alternatives.stream()
                    .map(stack -> (EmiIngredient) EmiStack.of(stack.getFluid(), stack.getAmount())).toList()));
        }
        view.itemOutputs().forEach(stack -> outputs.add(EmiStack.of(stack)));
        view.fluidOutputs().forEach(stack -> outputs.add(EmiStack.of(stack.getFluid(), stack.getAmount())));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(view.id().getNamespace(),
                "/" + view.kind().path() + "/" + view.id().getPath());
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return TransformationView.WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return TransformationView.HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), TransformationView.slotX(TransformationView.INPUT_X, i),
                    TransformationView.slotY(i));
        }
        widgets.addTexture(EmiTexture.EMPTY_ARROW, TransformationView.ARROW_X, TransformationView.ARROW_Y);
        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), TransformationView.slotX(TransformationView.OUTPUT_X, i),
                    TransformationView.slotY(i)).recipeContext(this);
        }

        int line = 0;
        for (Component note : view.notes()) {
            widgets.addText(note, TransformationView.INPUT_X,
                    TransformationView.NOTES_Y + line * TransformationView.NOTE_HEIGHT, TEXT_COLOR, false);
            line++;
        }
    }
}
