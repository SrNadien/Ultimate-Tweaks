package nadiendev.ultimatetweaks.config;

import nadiendev.ultimatetweaks.client.discord.PresenceSettings;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class TweaksClientConfig {

    public static final String SOURCE_CURSEFORGE = "curseforge";
    public static final String SOURCE_MANUAL = "manual";

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue TRASH_CAN_BUTTON;
    public static final ModConfigSpec.BooleanValue FTB_TEAMS_OFFLINE_FIX;
    public static final ModConfigSpec.BooleanValue DISCORD_ENABLED;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_APPLICATION_ID;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_SOURCE;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_DETAILS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DISCORD_STATES;
    public static final ModConfigSpec.IntValue DISCORD_ROTATION_SECONDS;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_LARGE_IMAGE;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_LARGE_IMAGE_TEXT;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_SMALL_IMAGE;
    public static final ModConfigSpec.BooleanValue DISCORD_SHOW_ELAPSED;
    public static final ModConfigSpec.BooleanValue DISCORD_CURSEFORGE_BUTTON;
    public static final ModConfigSpec.IntValue DISCORD_MAX_LINE_LENGTH;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_INVITE_URL;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_STATUS_STARTING;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_STATUS_MENU;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_STATUS_SINGLEPLAYER;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_STATUS_MULTIPLAYER;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_MANUAL_NAME;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_MANUAL_VERSION;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_MANUAL_AUTHOR;
    public static final ModConfigSpec.ConfigValue<String> DISCORD_MANUAL_URL;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        TRASH_CAN_BUTTON = builder
                .comment("Boton de la papelera en la esquina de los inventarios.",
                         "Solo se usa si FTB Library no esta instalado: con FTB Library el boton va en su barra lateral.",
                         "Si otro mod ya aporta una papelera, este boton no se muestra.")
                .define("trashCanButton", true);

        FTB_TEAMS_OFFLINE_FIX = builder
                .comment("Hace que FTB Teams funcione en servidores con online-mode=false (jugadores no premium).",
                         "FTB Teams busca al jugador con el UUID de su cuenta, que en modo offline no coincide con el que",
                         "le da el servidor, y entonces los equipos, las partys y las invitaciones no funcionan.",
                         "Con true usa el UUID que recibe del servidor. Solo hace algo si FTB Teams esta instalado.")
                .define("ftbTeamsOfflineFix", true);

        builder.comment("Discord Rich Presence.",
                        "Variables en los textos: {pack} {version} {author} {mods} {status} {dimension} {minecraft}.")
               .push("discord");

        DISCORD_ENABLED = builder
                .comment("Muestra el juego en Discord.")
                .define("enabled", true);

        DISCORD_APPLICATION_ID = builder
                .comment("Application ID de la aplicacion de Discord (discord.com/developers).",
                         "Es el que da el nombre y las imagenes subidas. Vacio usa el del mod.")
                .define("applicationId", PresenceSettings.APPLICATION_ID);

        DISCORD_SOURCE = builder
                .comment("De donde sale la informacion del pack.",
                         "curseforge: lee minecraftinstance.json de la instancia de la app de CurseForge.",
                         "manual: usa solo los valores de la seccion manual.")
                .defineInList("source", SOURCE_CURSEFORGE, Arrays.asList(SOURCE_CURSEFORGE, SOURCE_MANUAL));

        DISCORD_DETAILS = builder
                .comment("Primera linea de la presencia. Vacio usa el valor por defecto.")
                .define("details", PresenceSettings.DEFAULT_DETAILS);

        DISCORD_STATES = builder
                .comment("Segunda linea de la presencia. Se muestra el primer texto que tenga datos;",
                         "si rotationSeconds es mayor que 0 se muestran de uno en uno, en este orden.",
                         "{quests} muestra el progreso de FTB Quests si esta instalado; si no, se omite.")
                .defineListAllowEmpty("states", PresenceSettings.DEFAULT_STATES, () -> "",
                        value -> value instanceof String);

        DISCORD_ROTATION_SECONDS = builder
                .comment("Segundos que se muestra cada texto de states, uno tras otro.",
                         "0 no rota: muestra siempre el primero que tenga datos.")
                .defineInRange("rotationSeconds", PresenceSettings.DEFAULT_ROTATION_SECONDS, 0, 120);

        DISCORD_LARGE_IMAGE = builder
                .comment("Imagen grande: nombre de un asset subido a la aplicacion o una URL https.",
                         "Vacio usa el logo del mod. \"pack\" usa la imagen del pack en CurseForge.")
                .define("largeImage", "");

        DISCORD_SMALL_IMAGE = builder
                .comment("Imagen pequena: nombre de un asset subido a la aplicacion o una URL https.",
                         "Vacio usa el icono de la dimension (overworld, nether, end, other) si existe como asset",
                         "de la aplicacion o en el repositorio; si no, no muestra imagen pequena.")
                .define("smallImage", "");

        DISCORD_LARGE_IMAGE_TEXT = builder
                .comment("Texto al pasar el cursor sobre la imagen grande.")
                .define("largeImageText", PresenceSettings.DEFAULT_LARGE_IMAGE_TEXT);

        DISCORD_SHOW_ELAPSED = builder
                .comment("Muestra el tiempo transcurrido desde que se abrio el juego.")
                .define("showElapsedTime", true);

        DISCORD_CURSEFORGE_BUTTON = builder
                .comment("Boton hacia la pagina del pack en CurseForge, si se conoce su URL.")
                .define("curseforgeButton", true);

        DISCORD_MAX_LINE_LENGTH = builder
                .comment("Caracteres que caben en una linea de la tarjeta (unos 26 en el popout estrecho).",
                         "Si un texto es mas largo se usa la variante mas completa que si entre.")
                .defineInRange("maxLineLength", PresenceSettings.DEFAULT_MAX_LINE_LENGTH, 10, 128);

        DISCORD_INVITE_URL = builder
                .comment("URL de invitacion a Discord para un segundo boton. Vacio no lo muestra.")
                .define("inviteUrl", "");

        builder.comment("Textos de {status}.").push("status");

        DISCORD_STATUS_STARTING = builder.define("starting", PresenceSettings.DEFAULT_STATUS_STARTING);
        DISCORD_STATUS_MENU = builder.define("menu", PresenceSettings.DEFAULT_STATUS_MENU);
        DISCORD_STATUS_SINGLEPLAYER = builder.define("singleplayer", PresenceSettings.DEFAULT_STATUS_SINGLEPLAYER);
        DISCORD_STATUS_MULTIPLAYER = builder.define("multiplayer", PresenceSettings.DEFAULT_STATUS_MULTIPLAYER);

        builder.pop();

        builder.comment("Valores manuales. Si no estan vacios mandan sobre lo que lea de CurseForge;",
                        "si source es manual se usan solo estos.").push("manual");

        DISCORD_MANUAL_NAME = builder
                .comment("Nombre del pack que se muestra como titulo.")
                .define("packName", "");
        DISCORD_MANUAL_VERSION = builder.define("packVersion", "");
        DISCORD_MANUAL_AUTHOR = builder.define("packAuthor", "");
        DISCORD_MANUAL_URL = builder
                .comment("Enlace de la pagina del pack, usado por el boton de CurseForge.")
                .define("packUrl", "");

        builder.pop();
        builder.pop();

        SPEC = builder.build();
    }

    private TweaksClientConfig() {
    }
}
