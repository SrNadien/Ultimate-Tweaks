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
    private static final ModConfigSpec.BooleanValue ADVANCEMENTS_ENABLED;
    private static final ModConfigSpec.IntValue ADVANCEMENTS_INTERVAL;
    private static final ModConfigSpec.BooleanValue ADVANCEMENTS_IGNORE_EMPTY;
    private static final ModConfigSpec.BooleanValue ADVANCEMENTS_FAST_MATCH;
    private static final ModConfigSpec.BooleanValue QUESTS_ENABLED;
    private static final ModConfigSpec.BooleanValue QUESTS_IGNORE_UNCHANGED;
    private static final ModConfigSpec.BooleanValue QUESTS_CACHE_ITEMS;
    private static final ModConfigSpec.BooleanValue LOOTR_ENABLED;
    private static final ModConfigSpec.BooleanValue LOOTR_FORCE_TABLED;
    private static final ModConfigSpec.IntValue LOOTR_DELAY;
    private static final ModConfigSpec.BooleanValue LOOTR_LOG;
    private static final ModConfigSpec.BooleanValue LOOTR_SCAN_ALL;

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

        builder.comment("Optimizaciones del servidor para logros y para FTB Quests.",
                        "Con muchos mods y jugadores, comprobar el inventario en cada cambio hace que baje el tick rate.")
               .push("optimization");

        builder.comment("Logros de Minecraft (el criterio de cambio de inventario).")
               .push("advancements");
        ADVANCEMENTS_ENABLED = builder
                .comment("false desactiva todas las optimizaciones de logros.")
                .define("enabled", true);
        ADVANCEMENTS_INTERVAL = builder
                .comment("Ticks minimos entre dos comprobaciones de logros de un mismo jugador.",
                         "Los cambios que llegan en medio no se pierden: se juntan y se comprueban todos al cumplirse el intervalo.",
                         "0 comprueba en cada cambio, como Minecraft.")
                .defineInRange("intervalTicks", 5, 0, 200);
        ADVANCEMENTS_IGNORE_EMPTY = builder
                .comment("true ignora los cambios en que el ítem es vacio. Puede impedir logros que piden huecos libres.")
                .define("ignoreEmptyStacks", false);
        ADVANCEMENTS_FAST_MATCH = builder
                .comment("true comprueba los logros con varios ítems recorriendo el inventario una sola vez y sin crear listas.")
                .define("fastInventoryMatch", true);
        builder.pop();

        builder.comment("FTB Quests. Hace falta FTB Quests; sin el, esta seccion no hace nada.")
               .push("ftbQuests");
        QUESTS_ENABLED = builder
                .comment("false desactiva todas las optimizaciones de FTB Quests.")
                .define("enabled", true);
        QUESTS_IGNORE_UNCHANGED = builder
                .comment("true no vuelve a comprobar las misiones cuando un slot cambia sin cambiar de ítem ni de cantidad",
                         "(por ejemplo herramientas con energia o durabilidad que cambia todo el rato).")
                .define("ignoreUnchangedSlots", true);
        QUESTS_CACHE_ITEMS = builder
                .comment("true guarda la lista de ítems validos de cada tarea de ítem en vez de calcularla cada vez.")
                .define("cacheValidItems", true);
        builder.pop();

        builder.pop();

        builder.comment("Integracion con Lootr: los cofres y barriles de las estructuras pasan a ser de Lootr.",
                        "Incluye las estructuras de datapacks y de KubeJS (kubejs/data), que ponen sus cofres sin que Lootr se entere.",
                        "Las reglas con tabla de botin propia van en data/<namespace>/ultimatetweaks/lootr_structures/*.json.",
                        "Hace falta Lootr; sin el, esta seccion no hace nada.")
               .push("lootr");
        LOOTR_ENABLED = builder
                .comment("false desactiva toda la integracion.")
                .define("enabled", true);
        LOOTR_FORCE_TABLED = builder
                .comment("true avisa a Lootr de todos los cofres con tabla de botin de los chunks con estructuras,",
                         "para que los convierta aunque la estructura los haya puesto sin pasar por su aviso normal.")
                .define("forceStructureChests", true);
        LOOTR_DELAY = builder
                .comment("Ticks que se espera tras cargar un chunk con estructuras antes de convertir sus cofres.")
                .defineInRange("delayTicks", 20, 1, 1200);
        LOOTR_LOG = builder
                .comment("true escribe en el log cada contenedor que se pasa a Lootr, con su posicion y su tabla.")
                .define("logConversions", true);
        LOOTR_SCAN_ALL = builder
                .comment("true revisa todos los chunks que se cargan, no solo los de estructuras registradas,",
                         "para convertir tambien los cofres de features y de estructuras de mods que no se registran como estructura.")
                .define("scanAllChunks", true);
        builder.pop();

        SPEC = builder.build();
    }

    private static boolean loaded() {
        return SPEC.isLoaded();
    }

    public static boolean advancementsEnabled() {
        return loaded() && ADVANCEMENTS_ENABLED.get();
    }

    public static int advancementIntervalTicks() {
        return loaded() ? ADVANCEMENTS_INTERVAL.get() : 0;
    }

    public static boolean advancementsIgnoreEmpty() {
        return loaded() && ADVANCEMENTS_IGNORE_EMPTY.get();
    }

    public static boolean advancementsFastMatch() {
        return loaded() && ADVANCEMENTS_FAST_MATCH.get();
    }

    public static boolean questsEnabled() {
        return loaded() && QUESTS_ENABLED.get();
    }

    public static boolean questsIgnoreUnchangedSlots() {
        return questsEnabled() && QUESTS_IGNORE_UNCHANGED.get();
    }

    public static boolean questsCacheItems() {
        return questsEnabled() && QUESTS_CACHE_ITEMS.get();
    }

    public static boolean lootrEnabled() {
        return LOOTR_ENABLED.get();
    }

    public static boolean lootrForceTabledChests() {
        return LOOTR_FORCE_TABLED.get();
    }

    public static int lootrDelayTicks() {
        return LOOTR_DELAY.get();
    }

    public static boolean lootrLogConversions() {
        return LOOTR_LOG.get();
    }

    public static boolean lootrScanAllChunks() {
        return LOOTR_SCAN_ALL.get();
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
