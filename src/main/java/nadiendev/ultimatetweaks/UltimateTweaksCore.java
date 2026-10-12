package nadiendev.ultimatetweaks;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatetweaks.datagen.DataGenerators;

import nadiendev.ultimatetweaks.registry.CreativeTab;
import nadiendev.ultimatetweaks.registry.ItemsAdds;
import nadiendev.ultimatetweaks.registry.SonidosReproducibles;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(UltimateTweaksCore.MOD_ID)
public class UltimateTweaksCore {
    public static final String MOD_ID = "ultimatetweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateTweaksCore(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Inicializando Ultimate Tweaks...");

        modContainer.registerConfig(ModConfig.Type.CLIENT, TweaksClientConfig.SPEC);

        SonidosReproducibles.register(modEventBus);
        ItemsAdds.register(modEventBus);

        CreativeTab.register(modEventBus);

        modEventBus.addListener(DataGenerators::gatherData);

        LOGGER.info("Ultimate Tweaks inicializado correctamente!");
    }
}
