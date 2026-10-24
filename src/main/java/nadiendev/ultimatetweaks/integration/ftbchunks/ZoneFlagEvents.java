package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class ZoneFlagEvents {

    private ZoneFlagEvents() {
    }

    public static void register() {
        TeamEvent.COLLECT_PROPERTIES.register(ZoneFlags::collect);
        NeoForge.EVENT_BUS.register(new ZoneProtectionEvents());
        NeoForge.EVENT_BUS.register(new ZoneDamageEvents());
        NeoForge.EVENT_BUS.register(new ZoneMovementEvents());
    }
}
