package nadiendev.ultimatetweaks.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransformationReiDisplay extends BasicDisplay {

    private final TransformationView view;

    public TransformationReiDisplay(TransformationView view) {
        super(inputs(view), outputs(view), Optional.of(view.id()));
        this.view = view;
    }

    public TransformationView view() {
        return view;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TransformationReiCategory.identifier(view.kind());
    }

    private static List<EntryIngredient> inputs(TransformationView view) {
        List<EntryIngredient> inputs = new ArrayList<>();
        view.itemInputs().forEach(stacks -> inputs.add(EntryIngredients.ofItemStacks(stacks)));
        view.fluidInputs().forEach(stacks -> inputs.add(fluids(stacks)));
        return inputs;
    }

    private static List<EntryIngredient> outputs(TransformationView view) {
        List<EntryIngredient> outputs = new ArrayList<>();
        view.itemOutputs().forEach(stack -> outputs.add(EntryIngredients.of(stack)));
        view.fluidOutputs().forEach(stack -> outputs.add(fluids(List.of(stack))));
        return outputs;
    }

    private static EntryIngredient fluids(List<FluidStack> stacks) {
        List<EntryStack<?>> entries = new ArrayList<>();
        stacks.forEach(stack -> entries.add(TransformationReiFluidRenderer.stack(stack.getFluid(), stack.getAmount())));
        return EntryIngredient.of(entries);
    }
}
