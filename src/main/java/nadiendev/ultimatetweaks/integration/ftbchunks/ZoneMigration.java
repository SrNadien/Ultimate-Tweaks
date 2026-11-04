package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import nadiendev.ultimatetweaks.UltimateTweaks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ZoneMigration {

    private static final String MARKER = "ultimatetweaks_zone_texts.migrated";

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        Path marker = server.getWorldPath(LevelResource.ROOT).resolve(MARKER);
        if (Files.exists(marker) || !FTBTeamsAPI.api().isManagerLoaded()) {
            return;
        }
        int teams = 0;
        for (Team team : FTBTeamsAPI.api().getManager().getTeams()) {
            if (ZoneFlags.migrateLegacyTexts(team) > 0) {
                teams++;
            }
        }
        try {
            Files.writeString(marker, "");
        } catch (IOException e) {
            UltimateTweaks.LOGGER.warn("Zonas: no se pudo guardar la marca de migracion: {}", e.toString());
        }
        if (teams > 0) {
            UltimateTweaks.LOGGER.info("Zonas: {} equipos pasan a los textos de entrada y salida actuales", teams);
        }
    }
}
