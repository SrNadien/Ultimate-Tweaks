package nadiendev.ultimatetweaks.datagen;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.datagen.providers.ModAdvancementProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModJukeboxSongProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModLangProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModRecipeProvider;
import nadiendev.ultimatetweaks.datagen.providers.ModSoundProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

public class DataGenerators {

    private static final List<String> LANGUAGES = List.of("en_us", "es_es", "es_ar", "es_mx");

    private DataGenerators() {
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModSoundProvider::new);
        for (String locale : LANGUAGES) {
            event.createProvider(output -> new ModLangProvider(output, locale));
        }
    }

    public static void gatherServerData(GatherDataEvent.Server event) {
        UltimateTweaksCore.LOGGER.info("=== INICIANDO GENERACION DE DATOS ===");

        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider((output, lookupProvider) ->
                new AdvancementProvider(output, lookupProvider, List.of(new ModAdvancementProvider())));
        event.createProvider(ModJukeboxSongProvider::new);

        UltimateTweaksCore.LOGGER.info("=== TODOS LOS PROVIDERS REGISTRADOS CORRECTAMENTE ===");
    }
}
