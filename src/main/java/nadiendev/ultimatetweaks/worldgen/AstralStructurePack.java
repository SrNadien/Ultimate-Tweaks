package nadiendev.ultimatetweaks.worldgen;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.minecraft.server.packs.PathPackResources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID)
public final class AstralStructurePack {

    private static final String PACK_ID = "mod/" + UltimateTweaksCore.MOD_ID + ":astral_structures";
    private static final int DATA_PACK_FORMAT = 48;

    private AstralStructurePack() {
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }

        IModFileInfo astral = ModList.get().getModFileById(AstralStructures.MOD_ID);
        if (astral == null) {
            return;
        }

        Path directory = FMLPaths.CONFIGDIR.get().resolve(UltimateTweaksCore.MOD_ID).resolve("astral_structures_pack");
        Map<String, String> files = buildOverrides(astral);

        try {
            deleteDirectory(directory);
            if (files.isEmpty()) {
                return;
            }
            write(directory, files);
        } catch (IOException exception) {
            UltimateTweaksCore.LOGGER.error("No se pudo escribir el pack de estructuras de Astral Sorcery", exception);
            return;
        }

        Pack pack = Pack.readMetaAndCreate(
                new PackLocationInfo(PACK_ID, Component.literal("Ultimate Tweaks: Astral Sorcery structures"),
                        PackSource.BUILT_IN, Optional.empty()),
                BuiltInPackSource.fromName(path -> new PathPackResources(path, directory)),
                PackType.SERVER_DATA,
                new PackSelectionConfig(true, Pack.Position.TOP, false));
        if (pack == null) {
            UltimateTweaksCore.LOGGER.error("El pack de estructuras de Astral Sorcery no tiene un pack.mcmeta valido");
            return;
        }

        UltimateTweaksCore.LOGGER.info("Ajustes de estructuras de Astral Sorcery aplicados: {}", files.keySet());
        event.addRepositorySource(consumer -> consumer.accept(pack));
    }

    private static Map<String, String> buildOverrides(IModFileInfo astral) {
        Map<String, String> files = new LinkedHashMap<>();

        for (AstralStructures.Entry entry : AstralStructures.ALL) {
            AstralStructureOverrides.Settings settings = TweaksCommonConfig.astralStructure(entry.name());
            if (settings.isDefault()) {
                continue;
            }

            Path original = astral.getFile().findResource(entry.structureSetPath().split("/"));
            if (Files.isRegularFile(original)) {
                try {
                    AstralStructureOverrides.structureSet(Files.readString(original, StandardCharsets.UTF_8), settings)
                            .ifPresent(json -> files.put(entry.structureSetPath(), json));
                } catch (IOException | RuntimeException exception) {
                    UltimateTweaksCore.LOGGER.warn("No se pudo leer {} de Astral Sorcery", entry.structureSetPath(),
                            exception);
                }
            } else {
                UltimateTweaksCore.LOGGER.warn("Esta version de Astral Sorcery no trae la estructura {}", entry.name());
                continue;
            }

            AstralStructureOverrides.biomeTag(settings).ifPresent(json -> files.put(entry.biomeTagPath(), json));
        }
        return files;
    }

    private static void write(Path directory, Map<String, String> files) throws IOException {
        Files.createDirectories(directory);
        Files.writeString(directory.resolve("pack.mcmeta"),
                "{\"pack\":{\"description\":\"Ultimate Tweaks: Astral Sorcery structures\",\"pack_format\":"
                        + DATA_PACK_FORMAT + "}}", StandardCharsets.UTF_8);
        for (Map.Entry<String, String> file : files.entrySet()) {
            Path target = directory.resolve(file.getKey());
            Files.createDirectories(target.getParent());
            Files.writeString(target, file.getValue(), StandardCharsets.UTF_8);
        }
    }

    private static void deleteDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }
}
