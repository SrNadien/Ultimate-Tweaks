package nadiendev.ultimatetweaks.integration.lootr;

import net.neoforged.fml.ModList;

public final class LootrIntegration {

    private LootrIntegration() {
    }

    public static void init() {
        if (ModList.get().isLoaded("lootr")) {
            LootrStructureEvents.register();
        }
    }
}
