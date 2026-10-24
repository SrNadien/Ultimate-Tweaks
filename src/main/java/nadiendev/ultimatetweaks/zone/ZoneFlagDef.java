package nadiendev.ultimatetweaks.zone;

import java.util.List;

public enum ZoneFlagDef {

    RIDE("ride", true,
            "Allow Riding", "Whether players can mount vehicles and animals in the zone.",
            "Permitir montar", "Si los jugadores pueden montar veh\u00edculos y animales en la zona."),
    SLEEP("sleep", true,
            "Allow Sleeping", "Whether players can sleep in beds in the zone.",
            "Permitir dormir", "Si los jugadores pueden dormir en camas en la zona."),
    LIGHTER("lighter", true,
            "Allow Flint and Steel", "Whether flint and steel and fire charges can be used in the zone.",
            "Permitir mechero", "Si se pueden usar el mechero y las bolas de fuego en la zona."),
    BLOCK_TRAMPLING("block_trampling", true,
            "Allow Trampling", "Whether farmland can be trampled in the zone.",
            "Permitir pisotear", "Si se puede pisotear la tierra de cultivo en la zona."),
    ITEM_FRAME_ROTATION("item_frame_rotation", true,
            "Allow Rotating Item Frames", "Whether items in item frames can be rotated in the zone.",
            "Permitir girar marcos", "Si se pueden girar los \u00edtems de los marcos en la zona."),
    USE_ANVIL("use_anvil", true,
            "Allow Anvils", "Whether anvils can be used in the zone.",
            "Permitir yunques", "Si se pueden usar yunques en la zona."),
    USE("use", true,
            "Allow Using Doors and Levers", "Whether doors, trapdoors, fence gates, buttons and levers can be used in the zone.",
            "Permitir usar puertas y palancas", "Si se pueden usar puertas, trampillas, portones, botones y palancas en la zona."),
    FIREWORK_DAMAGE("firework_damage", true,
            "Firework Damage", "Whether fireworks can hurt players in the zone.",
            "Da\u00f1o de fuegos artificiales", "Si los fuegos artificiales pueden da\u00f1ar a los jugadores en la zona."),
    PISTONS("pistons", true,
            "Allow Pistons", "Whether pistons in the zone can move.",
            "Permitir pistones", "Si los pistones de la zona pueden moverse."),

    BLOCK_BREAK("block_break", true,
            "Allow Breaking Blocks", "Whether players who are not members of the team can break blocks in the zone.",
            "Permitir romper bloques", "Si los jugadores que no son del equipo pueden romper bloques en la zona."),
    BLOCK_PLACE("block_place", true,
            "Allow Placing Blocks", "Whether players who are not members of the team can place blocks in the zone.",
            "Permitir colocar bloques", "Si los jugadores que no son del equipo pueden colocar bloques en la zona."),
    INTERACT("interact", true,
            "Allow Interacting", "Whether players who are not members of the team can interact with blocks and entities in the zone (not inventories).",
            "Permitir interactuar", "Si los jugadores que no son del equipo pueden interactuar con bloques y entidades en la zona (no con inventarios)."),
    CHEST_ACCESS("chest_access", true,
            "Allow Opening Inventories", "Whether players who are not members of the team can open chests and other inventories in the zone.",
            "Permitir abrir inventarios", "Si los jugadores que no son del equipo pueden abrir cofres y otros inventarios en la zona."),
    DAMAGE_ANIMALS("damage_animals", true,
            "Allow Hurting Animals", "Whether players who are not members of the team can hurt animals in the zone.",
            "Permitir da\u00f1ar animales", "Si los jugadores que no son del equipo pueden da\u00f1ar animales en la zona."),
    PVP("pvp", false,
            "Allow PvP", "Whether players can hurt each other in the zone.",
            "Permitir PvP", "Si los jugadores pueden da\u00f1arse entre ellos en la zona."),
    RESPAWN_ANCHORS("respawn_anchors", true,
            "Allow Respawn Anchors", "Whether respawn anchors can be used in the zone.",
            "Permitir anclas de reaparici\u00f3n", "Si se pueden usar anclas de reaparici\u00f3n en la zona."),
    VEHICLE_PLACE("vehicle_place", true,
            "Allow Placing Vehicles", "Whether boats and minecarts can be placed in the zone.",
            "Permitir colocar veh\u00edculos", "Si se pueden colocar botes y vagonetas en la zona."),
    VEHICLE_DESTROY("vehicle_destroy", true,
            "Allow Destroying Vehicles", "Whether players who are not members of the team can destroy boats and minecarts in the zone.",
            "Permitir destruir veh\u00edculos", "Si los jugadores que no son del equipo pueden destruir botes y vagonetas en la zona."),
    WIND_CHARGE_BURST("wind_charge_burst", true,
            "Wind Charge Bursts", "Whether wind charges burst in the zone.",
            "R\u00e1fagas de carga de viento", "Si las cargas de viento estallan en la zona."),
    BREEZE_CHARGE_EXPLOSION("breeze_charge_explosion", true,
            "Breeze Charge Bursts", "Whether the wind charges of a breeze burst in the zone.",
            "R\u00e1fagas de brisa", "Si las cargas de viento de una brisa estallan en la zona."),
    ENDERDRAGON_BLOCK_DAMAGE("enderdragon_block_damage", true,
            "Ender Dragon Block Damage", "Whether the ender dragon destroys blocks in the zone.",
            "Da\u00f1o del drag\u00f3n a bloques", "Si el drag\u00f3n del End destruye bloques en la zona."),
    ENDERMAN_GRIEF("enderman_grief", true,
            "Enderman Grief", "Whether endermen pick up and place blocks in the zone.",
            "Enderman coge bloques", "Si los enderman cogen y ponen bloques en la zona."),
    SNOWMAN_TRAILS("snowman_trails", true,
            "Snow Golem Trails", "Whether snow golems leave snow behind them in the zone.",
            "Rastro de golem de nieve", "Si los golems de nieve dejan nieve a su paso en la zona."),
    RAVAGER_GRIEF("ravager_grief", true,
            "Ravager Grief", "Whether ravagers destroy blocks in the zone.",
            "Devastador rompe bloques", "Si los devastadores destruyen bloques en la zona."),
    ENTITY_PAINTING_DESTROY("entity_painting_destroy", true,
            "Mobs Destroying Paintings", "Whether anything that is not a player can destroy paintings in the zone.",
            "Destruir cuadros sin jugador", "Si algo que no sea un jugador puede destruir cuadros en la zona."),
    ENTITY_ITEM_FRAME_DESTROY("entity_item_frame_destroy", true,
            "Mobs Destroying Item Frames", "Whether anything that is not a player can destroy item frames in the zone.",
            "Destruir marcos sin jugador", "Si algo que no sea un jugador puede destruir marcos en la zona."),
    LAVA_HARDEN("lava_harden", true,
            "Lava Hardening", "Whether lava turns into stone, cobblestone or obsidian when it meets water in the zone.",
            "Lava que se endurece", "Si la lava se convierte en piedra, roca o obsidiana al tocar agua en la zona."),
    EXIT_VIA_TELEPORT("exit_via_teleport", true,
            "Allow Exit by Teleport", "If exit is denied, whether players can still leave by teleporting.",
            "Permitir salir teletransport\u00e1ndose", "Si la salida est\u00e1 denegada, si los jugadores aun pueden salir teletransport\u00e1ndose."),
    SPAWN("spawn", "",
            "Respawn Location", "Where players respawn after dying in the zone, as three numbers: x y z. Empty for the normal respawn.",
            "Lugar de reaparici\u00f3n", "D\u00f3nde reaparecen los jugadores que mueren en la zona, con tres n\u00fameros: x y z. Vac\u00edo usa la reaparici\u00f3n normal."),
    NATURAL_HEALTH_REGEN("natural_health_regen", true,
            "Natural Health Regeneration", "If off, players do not regain health from food in the zone.",
            "Regeneraci\u00f3n natural", "Si est\u00e1 desactivado, los jugadores no recuperan vida con la comida en la zona."),

    CREEPER_EXPLOSION("creeper_explosion", false,
            "Creeper Explosions", "Whether creeper explosions damage blocks and entities in the zone.",
            "Explosiones de creeper", "Si las explosiones de creeper da\u00f1an bloques y entidades en la zona."),
    GHAST_FIREBALL("ghast_fireball", true,
            "Ghast Fireballs", "Whether ghast fireballs and wither skulls damage blocks and entities in the zone.",
            "Bolas de fuego de ghast", "Si las bolas de fuego de ghast y las calaveras de wither da\u00f1an bloques y entidades en la zona."),
    WITHER_DAMAGE("wither_damage", true,
            "Wither Explosions", "Whether the wither's own explosions damage blocks and entities in the zone.",
            "Explosiones del wither", "Si las explosiones del propio wither da\u00f1an bloques y entidades en la zona."),
    TNT("tnt", true,
            "TNT", "Whether TNT damages blocks and entities in the zone.",
            "TNT", "Si el TNT da\u00f1a bloques y entidades en la zona."),
    OTHER_EXPLOSION("other_explosion", true,
            "Other Explosions", "Whether any other explosion damages blocks and entities in the zone.",
            "Otras explosiones", "Si cualquier otra explosi\u00f3n da\u00f1a bloques y entidades en la zona."),
    MOB_DAMAGE("mob_damage", true,
            "Mob Damage", "Whether mobs can hurt players in the zone.",
            "Da\u00f1o de mobs", "Si los mobs pueden da\u00f1ar a los jugadores en la zona."),
    MOB_SPAWNING("mob_spawning", true,
            "Allow Mob Spawning", "Whether mobs can spawn in the zone. The spawn types it blocks are set in the common config.",
            "Permitir aparici\u00f3n de mobs", "Si los mobs pueden aparecer en la zona. Los tipos de aparici\u00f3n que bloquea se eligen en la configuraci\u00f3n com\u00fan."),
    DENY_SPAWN("deny_spawn", List.of(),
            "Denied Spawns", "Entity types that can never spawn in the zone, one per line (minecraft:zombie).",
            "Apariciones prohibidas", "Tipos de entidad que nunca aparecen en la zona, uno por l\u00ednea (minecraft:zombie)."),

    LIGHTNING("lightning", true,
            "Lightning", "If off, lightning in the zone is only visual: no fire and no damage.",
            "Rayos", "Si est\u00e1 desactivado, los rayos de la zona son solo visuales: sin fuego ni da\u00f1o."),
    CROP_GROWTH("crop_growth", true,
            "Crop Growth", "Whether crops and saplings can grow in the zone.",
            "Crecimiento de cultivos", "Si los cultivos y los \u00e1rboles j\u00f3venes pueden crecer en la zona."),

    ENTRY("entry", true,
            "Allow Entry", "If off, players who are not members of the team cannot enter the zone.",
            "Permitir entrar", "Si est\u00e1 desactivado, los jugadores que no son del equipo no pueden entrar en la zona."),
    EXIT("exit", true,
            "Allow Exit", "If off, players who are not members of the team cannot leave the zone.",
            "Permitir salir", "Si est\u00e1 desactivado, los jugadores que no son del equipo no pueden salir de la zona."),
    ENTRY_DENY_MESSAGE("entry_deny_message", "&cYou cannot enter this area.",
            "Entry Denied Message", "Message shown when entering is denied.",
            "Mensaje de entrada denegada", "Mensaje que se muestra cuando se impide entrar."),
    EXIT_DENY_MESSAGE("exit_deny_message", "&cYou cannot leave this area.",
            "Exit Denied Message", "Message shown when leaving is denied.",
            "Mensaje de salida denegada", "Mensaje que se muestra cuando se impide salir."),
    GREETING("greeting", "&lEntrando al rancho de &b&l{team}",
            "Entry Message", "Chat message shown on entering. Use & for colors and {team} for the team name. Empty for none.",
            "Mensaje de entrada", "Mensaje de chat al entrar. Usa & para colores y {team} para el nombre del equipo. Vac\u00edo no muestra nada."),
    GREETING_TITLE("greeting_title", "",
            "Entry Title", "Title shown on entering; a subtitle can follow after \\n. Empty for none.",
            "T\u00edtulo de entrada", "T\u00edtulo al entrar; puede llevar un subt\u00edtulo despu\u00e9s de \\n. Vac\u00edo no muestra nada."),
    FAREWELL("farewell", "&lSaliendo del rancho de &b&l{team}",
            "Exit Message", "Chat message shown on leaving. Use & for colors and {team} for the team name. Empty for none.",
            "Mensaje de salida", "Mensaje de chat al salir. Usa & para colores y {team} para el nombre del equipo. Vac\u00edo no muestra nada."),
    FAREWELL_TITLE("farewell_title", "",
            "Exit Title", "Title shown on leaving; a subtitle can follow after \\n. Empty for none.",
            "T\u00edtulo de salida", "T\u00edtulo al salir; puede llevar un subt\u00edtulo despu\u00e9s de \\n. Vac\u00edo no muestra nada."),
    ENDERPEARL("enderpearl", true,
            "Allow Ender Pearls", "Whether ender pearls can be used to teleport into or inside the zone.",
            "Permitir perlas de ender", "Si se pueden usar perlas de ender para teletransportarse a la zona o dentro de ella."),
    CHORUS_FRUIT_TELEPORT("chorus_fruit_teleport", true,
            "Allow Chorus Fruit", "Whether chorus fruit can teleport players into or inside the zone.",
            "Permitir fruta de chorus", "Si la fruta de chorus puede teletransportar a la zona o dentro de ella."),

    ITEM_PICKUP("item_pickup", true,
            "Allow Item Pickup", "Whether players can pick up items in the zone.",
            "Permitir recoger \u00edtems", "Si los jugadores pueden recoger \u00edtems en la zona."),
    ITEM_DROP("item_drop", true,
            "Allow Item Drop", "Whether players can drop items in the zone.",
            "Permitir tirar \u00edtems", "Si los jugadores pueden tirar \u00edtems en la zona."),
    EXP_DROPS("exp_drops", true,
            "Experience Drops", "Whether killed mobs drop experience in the zone.",
            "Experiencia al morir", "Si los mobs sueltan experiencia al morir en la zona."),
    DENY_MESSAGE("deny_message", "&cYou cannot do that here.",
            "Denied Message", "Message shown to a player when an action is denied in the zone. Empty for none.",
            "Mensaje de acci\u00f3n denegada", "Mensaje que ve el jugador cuando se le impide una acci\u00f3n en la zona. Vac\u00edo no muestra nada."),
    INVINCIBLE("invincible", false,
            "Invincible", "If on, players take no damage in the zone.",
            "Invencible", "Si est\u00e1 activado, los jugadores no reciben da\u00f1o en la zona."),
    FALL_DAMAGE("fall_damage", true,
            "Fall Damage", "Whether players take fall damage in the zone.",
            "Da\u00f1o por ca\u00edda", "Si los jugadores reciben da\u00f1o por ca\u00edda en la zona."),
    GAME_MODE("game_mode", "",
            "Game Mode", "Game mode (survival, creative, adventure, spectator) applied to players while they are in the zone. Empty for none.",
            "Modo de juego", "Modo de juego (survival, creative, adventure, spectator) que se aplica a los jugadores mientras est\u00e1n en la zona. Vac\u00edo no cambia nada."),
    TIME_LOCK("time_lock", "",
            "Time Lock", "Time of day players see while in the zone, in ticks (0 to 24000), or day, noon, night, midnight. Empty for none.",
            "Hora fija", "Hora del d\u00eda que ven los jugadores en la zona, en ticks (0 a 24000), o day, noon, night, midnight. Vac\u00edo no cambia nada."),
    WEATHER_LOCK("weather_lock", "",
            "Weather Lock", "Weather players see while in the zone: clear or rain. Empty for none.",
            "Clima fijo", "Clima que ven los jugadores en la zona: clear o rain. Vac\u00edo no cambia nada."),
    NATURAL_HUNGER_DRAIN("natural_hunger_drain", true,
            "Natural Hunger Drain", "If off, players do not lose hunger in the zone.",
            "Hambre natural", "Si est\u00e1 desactivado, los jugadores no pierden hambre en la zona."),
    HEAL_DELAY("heal_delay", 0, 0, 3600,
            "Heal Delay", "Seconds between heals in the zone. 0 disables healing.",
            "Cada cu\u00e1nto curar", "Segundos entre curaciones en la zona. 0 desactiva la curaci\u00f3n."),
    HEAL_AMOUNT("heal_amount", 1, -40, 40,
            "Heal Amount", "Half hearts healed each time (damage if negative).",
            "Cantidad de curaci\u00f3n", "Medios corazones que se curan cada vez (da\u00f1o si es negativo)."),
    HEAL_MIN_HEALTH("heal_min_health", 0.0D, 0.0D, 1024.0D,
            "Minimum Health", "Lowest health that a negative heal amount can bring players to.",
            "Vida m\u00ednima", "Vida m\u00ednima a la que puede dejar a los jugadores una curaci\u00f3n negativa."),
    HEAL_MAX_HEALTH("heal_max_health", 20.0D, 0.0D, 1024.0D,
            "Maximum Health", "Highest health that healing can bring players to.",
            "Vida m\u00e1xima", "Vida m\u00e1xima a la que puede dejar a los jugadores la curaci\u00f3n."),
    FEED_DELAY("feed_delay", 0, 0, 3600,
            "Feed Delay", "Seconds between feedings in the zone. 0 disables feeding.",
            "Cada cu\u00e1nto alimentar", "Segundos entre alimentaciones en la zona. 0 desactiva la alimentaci\u00f3n."),
    FEED_AMOUNT("feed_amount", 1, -20, 20,
            "Feed Amount", "Food points restored each time (removed if negative).",
            "Cantidad de comida", "Puntos de comida que se reponen cada vez (se quitan si es negativo)."),
    FEED_MIN_HUNGER("feed_min_hunger", 0, 0, 20,
            "Minimum Hunger", "Lowest food level that a negative feed amount can bring players to.",
            "Hambre m\u00ednima", "Nivel de comida m\u00ednimo al que puede dejar a los jugadores una cantidad negativa."),
    FEED_MAX_HUNGER("feed_max_hunger", 20, 0, 20,
            "Maximum Hunger", "Highest food level that feeding can bring players to.",
            "Hambre m\u00e1xima", "Nivel de comida m\u00e1ximo al que puede dejar a los jugadores la alimentaci\u00f3n."),
    BLOCKED_CMDS("blocked_cmds", List.of(),
            "Blocked Commands", "Commands players cannot use in the zone, one per line, without the slash.",
            "Comandos bloqueados", "Comandos que los jugadores no pueden usar en la zona, uno por l\u00ednea y sin barra."),
    ALLOWED_CMDS("allowed_cmds", List.of(),
            "Allowed Commands", "If not empty, the only commands players can use in the zone, one per line, without the slash.",
            "Comandos permitidos", "Si no est\u00e1 vac\u00edo, los \u00fanicos comandos que los jugadores pueden usar en la zona, uno por l\u00ednea y sin barra."),

    SEND_CHAT("send_chat", true,
            "Allow Chat", "Whether players can send chat messages from the zone.",
            "Permitir chat", "Si los jugadores pueden enviar mensajes de chat desde la zona."),
    POTION_SPLASH("potion_splash", true,
            "Potion Splash", "Whether splash and lingering potions take effect in the zone.",
            "Pociones arrojadizas", "Si las pociones arrojadizas y persistentes hacen efecto en la zona.");

    public enum Kind {
        STATE,
        TEXT,
        LIST,
        INT,
        DOUBLE
    }

    private final String path;
    private final Kind kind;
    private final Object defaultValue;
    private final Number min;
    private final Number max;
    private final String english;
    private final String englishTooltip;
    private final String spanish;
    private final String spanishTooltip;

    ZoneFlagDef(String path, Object defaultValue, String english, String englishTooltip, String spanish,
                String spanishTooltip) {
        this(path, defaultValue, null, null, english, englishTooltip, spanish, spanishTooltip);
    }

    ZoneFlagDef(String path, Object defaultValue, Number min, Number max, String english, String englishTooltip,
                String spanish, String spanishTooltip) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.kind = switch (defaultValue) {
            case Boolean ignored -> Kind.STATE;
            case String ignored -> Kind.TEXT;
            case List<?> ignored -> Kind.LIST;
            case Integer ignored -> Kind.INT;
            default -> Kind.DOUBLE;
        };
        this.english = english;
        this.englishTooltip = englishTooltip;
        this.spanish = spanish;
        this.spanishTooltip = spanishTooltip;
    }

    public String key() {
        return path;
    }

    public String path() {
        return "flag_" + path;
    }

    public Kind kind() {
        return kind;
    }

    public Object defaultValue() {
        return defaultValue;
    }

    public Number min() {
        return min;
    }

    public Number max() {
        return max;
    }

    public String langKey() {
        return "ftbteamsconfig.ultimatetweaks." + path();
    }

    public String name(boolean spanishText) {
        return spanishText ? spanish : english;
    }

    public String tooltip(boolean spanishText) {
        return spanishText ? spanishTooltip : englishTooltip;
    }
}
