package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.registry.ItemsAdds;
import nadiendev.ultimatetweaks.registry.MaterialItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemsModelProvider extends ItemModelProvider {

    public ItemsModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UltimateTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemsAdds.PRINTED_PROGNOSIS_CIRCUIT.get());
        basicItem(ItemsAdds.PRINTED_PROTOCOL_CIRCUIT.get());
        basicItem(ItemsAdds.PRINTED_CHRONO_CIRCUIT.get());
        basicItem(ItemsAdds.PRINTED_AUTHORITY_CIRCUIT.get());
        basicItem(ItemsAdds.PRINTED_EXECUTION_CIRCUIT.get());
        basicItem(ItemsAdds.PROGNOSIS_PROCESSOR.get());
        basicItem(ItemsAdds.PROTOCOL_PROCESSOR.get());
        basicItem(ItemsAdds.CHRONO_PROCESSOR.get());
        basicItem(ItemsAdds.AUTHORITY_PROCESSOR.get());
        basicItem(ItemsAdds.EXECUTION_PROCESSOR.get());
        basicItem(ItemsAdds.STELLAR_ALLOY.get());
        basicItem(ItemsAdds.CRYSTAL_CLUSTER.get());
        basicItem(ItemsAdds.ENERGIZED_CRYSTAL_CLUSTER.get());
        basicItem(ItemsAdds.AURELIAN_CRYSTAL.get());
        basicItem(ItemsAdds.ENERGIZED_AURELIAN_CRYSTAL.get());
        basicItem(ItemsAdds.ZINC_CLAW.get());
        basicItem(ItemsAdds.BRASS_CLAW.get());
        basicItem(ItemsAdds.ANDESITE_CLAW.get());
        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
            for (MaterialItems.Kind kind : MaterialItems.Kind.values()) {
                basicItem(MaterialItems.holder(material, kind).get());
            }
        }
    }
}
