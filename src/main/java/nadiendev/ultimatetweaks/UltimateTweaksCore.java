package nadiendev.ultimatetweaks;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatetweaks.datagen.DataGenerators;

import nadiendev.ultimatetweaks.registry.CreativeTab;
import nadiendev.ultimatetweaks.registry.ItemsAdds;
import nadiendev.ultimatetweaks.registry.SonidosReproducibles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(UltimateTweaksCore.MOD_ID)
public class UltimateTweaksCore {
    public static final String MOD_ID = "ultimatetweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateTweaksCore(IEventBus modEventBus) {
        LOGGER.info("Inicializando Ultimate Tweaks...");

        SonidosReproducibles.register(modEventBus);
        ItemsAdds.register(modEventBus);

        CreativeTab.register(modEventBus);

        modEventBus.addListener(DataGenerators::gatherData);

        LOGGER.info("Ultimate Tweaks inicializado correctamente!");
    }
}
