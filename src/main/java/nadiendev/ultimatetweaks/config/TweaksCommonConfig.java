package nadiendev.ultimatetweaks.config;

import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TweaksCommonConfig {

    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue ZONE_ENABLED;
    private static final ModConfigSpec.BooleanValue ZONE_ONLY_SERVER_TEAMS;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ZONE_SPAWN_TYPES;
    private static final ModConfigSpec.IntValue ZONE_BYPASS_LEVEL;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ZONE_ENABLED_FLAGS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Flags de zonas protegidas de FTB Chunks, al estilo de WorldGuard y ProtectionStones.",
                        "Cada equipo con chunks reclamados tiene los flags entre sus ajustes (boton Settings del equipo",
                        "o /ftbteams server settings). Aqui solo se configura como se aplican.",
                        "Hace falta FTB Chunks; sin el, esta seccion no hace nada.")
               .push("zoneFlags");

        ZONE_ENABLED = builder
                .comment("false desactiva todos los flags.")
                .define("enabled", true);
        ZONE_ONLY_SERVER_TEAMS = builder
                .comment("true aplica los flags solo a los chunks de equipos de servidor (zonas de admin).",
                         "false los aplica tambien a los chunks de jugadores y partys.")
                .define("onlyServerTeams", true);
        ZONE_SPAWN_TYPES = builder
                .comment("Tipos de aparicion que bloquea el flag mob_spawning (nombres de EntitySpawnReason).",
                         "Los spawners, huevos y comandos no se bloquean salvo que los pongas aqui.")
                .defineListAllowEmpty("blockedSpawnTypes",
                        List.of("NATURAL", "CHUNK_GENERATION", "PATROL", "REINFORCEMENT"), () -> "NATURAL",
                        value -> value instanceof String);
        ZONE_ENABLED_FLAGS = builder
                .comment("Flags que salen en los ajustes de los equipos. Los que no esten en esta lista no existen en el juego.",
                         "Por defecto solo los que ProtectionStones activa de serie para el dueno de la proteccion.",
                         "Flags disponibles: " + Arrays.stream(ZoneFlagDef.values()).map(ZoneFlagDef::key)
                                 .collect(Collectors.joining(", ")) + ".",
                         "Un flag que se quita de la lista vuelve a su comportamiento normal aunque el equipo lo tuviera cambiado.")
                .defineListAllowEmpty("enabledFlags",
                        List.of("pvp", "greeting", "farewell", "entry", "creeper_explosion", "mob_spawning", "use"), () -> "pvp",
                        value -> value instanceof String);
        ZONE_BYPASS_LEVEL = builder
                .comment("Nivel de permisos (0 a 4) a partir del cual un jugador se salta los flags que le limitan",
                         "(entrar, salir, usar, comandos...). 2 son los operadores; 5 desactiva la excepcion.")
                .defineInRange("bypassPermissionLevel", 2, 0, 5);

        builder.pop();

        SPEC = builder.build();
    }

    public static boolean zoneEnabled() {
        return ZONE_ENABLED.get();
    }

    public static boolean zoneOnlyServerTeams() {
        return ZONE_ONLY_SERVER_TEAMS.get();
    }

    public static List<? extends String> zoneBlockedSpawnTypes() {
        return ZONE_SPAWN_TYPES.get();
    }

    public static List<? extends String> zoneEnabledFlags() {
        return ZONE_ENABLED_FLAGS.get();
    }

    public static int zoneBypassLevel() {
        return ZONE_BYPASS_LEVEL.get();
    }

    private TweaksCommonConfig() {
    }
}
