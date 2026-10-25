package nadiendev.ultimatetweaks.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import nadiendev.ultimatetweaks.transformation.Transformations;
import nadiendev.ultimatetweaks.transformation.view.TransformationKind;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.fluids.FluidType;

@REIPluginClient
public class TransformationReiPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        if (!Transformations.enabled()) {
            return;
        }
        for (TransformationKind kind : TransformationKind.values()) {
            registry.add(new TransformationReiCategory(kind));
            kind.catalystItems().forEach(stack -> registry.addWorkstations(TransformationReiCategory.identifier(kind),
                    EntryStacks.of(stack)));
            kind.catalystFluids().forEach(fluid -> registry.addWorkstations(TransformationReiCategory.identifier(kind),
                    TransformationReiFluidRenderer.stack(fluid, FluidType.BUCKET_VOLUME)));
        }
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        if (!Transformations.enabled() || Minecraft.getInstance().level == null) {
            return;
        }
        for (TransformationKind kind : TransformationKind.values()) {
            for (TransformationView view : TransformationView.of(kind, registry.getRecipeManager())) {
                registry.add(new TransformationReiDisplay(view));
            }
        }
    }
}
