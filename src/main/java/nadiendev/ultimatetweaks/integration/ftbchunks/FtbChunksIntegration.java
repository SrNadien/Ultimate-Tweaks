package nadiendev.ultimatetweaks.integration.ftbchunks;

import net.neoforged.fml.ModList;

public final class FtbChunksIntegration {

    private FtbChunksIntegration() {
    }

    public static void init() {
        if (ModList.get().isLoaded("ftbchunks") && ModList.get().isLoaded("ftbteams")) {
            ZoneFlagEvents.register();
        }
    }
}
