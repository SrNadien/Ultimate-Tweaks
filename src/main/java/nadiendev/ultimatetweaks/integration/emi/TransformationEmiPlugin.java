package nadiendev.ultimatetweaks.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import nadiendev.ultimatetweaks.transformation.Transformations;
import nadiendev.ultimatetweaks.transformation.view.TransformationKind;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.network.chat.Component;

@EmiEntrypoint
public class TransformationEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (!Transformations.enabled()) {
            return;
        }

        for (TransformationKind kind : TransformationKind.values()) {
            EmiRecipeCategory category = new EmiRecipeCategory(kind.id(), EmiStack.of(kind.icon())) {
                @Override
                public Component getName() {
                    return kind.title();
                }
            };
            registry.addCategory(category);
            kind.catalystItems().forEach(stack -> registry.addWorkstation(category, EmiStack.of(stack)));
            kind.catalystFluids().forEach(fluid -> registry.addWorkstation(category, EmiStack.of(fluid)));
            for (TransformationView view : TransformationView.of(kind, registry.getRecipeManager())) {
                registry.addRecipe(new TransformationEmiRecipe(category, view));
            }
        }
    }
}
