package nadiendev.ultimatetweaks;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatetweaks.datagen.DataGenerators;
import nadiendev.ultimatetweaks.integration.ftbchunks.FtbChunksIntegration;
import nadiendev.ultimatetweaks.integration.lootr.LootrIntegration;
import nadiendev.ultimatetweaks.transformation.Transformations;

import nadiendev.ultimatetweaks.registry.CreativeTab;
import nadiendev.ultimatetweaks.registry.ItemsAdds;
import nadiendev.ultimatetweaks.registry.SonidosReproducibles;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
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
        modContainer.registerConfig(ModConfig.Type.COMMON, TweaksCommonConfig.SPEC);

        SonidosReproducibles.register(modEventBus);
        ItemsAdds.register(modEventBus);

        CreativeTab.register(modEventBus);
        Transformations.register(modEventBus);
        FtbChunksIntegration.init();
        LootrIntegration.init();

        modEventBus.addListener(DataGenerators::gatherClientData);
        modEventBus.addListener(DataGenerators::gatherServerData);

        LOGGER.info("Ultimate Tweaks inicializado correctamente!");
    }
}
