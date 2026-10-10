package nadiendev.ultimatetweaks.datagen;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.datagen.providers.ItemsModelProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModAdvancementProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModJukeboxSongProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModRecipeProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModSoundProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataGenerators {

    private DataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        UltimateTweaksCore.LOGGER.info("=== INICIANDO GENERACION DE DATOS ===");

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(),
                new AdvancementProvider(output, lookupProvider, existingFileHelper,
                        List.of(new ModAdvancementProvider())));
        generator.addProvider(event.includeServer(), new ModJukeboxSongProvider(output, lookupProvider));

        generator.addProvider(event.includeClient(), new ItemsModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));

        UltimateTweaksCore.LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}
