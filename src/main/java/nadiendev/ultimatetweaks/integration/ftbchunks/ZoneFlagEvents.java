package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.neoforge.FTBTeamsEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class ZoneFlagEvents {

    private ZoneFlagEvents() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(FTBTeamsEvent.CollectTeamProperties.class, ZoneFlags::collect);
        NeoForge.EVENT_BUS.register(new ZoneProtectionEvents());
        NeoForge.EVENT_BUS.register(new ZoneDamageEvents());
        NeoForge.EVENT_BUS.register(new ZoneMovementEvents());
    }
}
