package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaks;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemsModelProvider extends ItemModelProvider {

    public ItemsModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UltimateTweaks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
