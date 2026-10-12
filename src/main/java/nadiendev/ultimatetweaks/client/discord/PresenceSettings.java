package nadiendev.ultimatetweaks.client.discord;

import java.util.List;

public record PresenceSettings(
        boolean enabled,
        boolean curseforgeSource,
        String details,
        List<String> states,
        String largeImage,
        String largeImageText,
        boolean showElapsed,
        boolean curseforgeButton,
        String inviteUrl,
        String statusStarting,
        String statusMenu,
        String statusSingleplayer,
        String statusMultiplayer,
        String manualName,
        String manualVersion,
        String manualAuthor,
        String manualUrl) {

    public static final String APPLICATION_ID = "1559158264170553364";
    public static final String MOD_LOGO_URL =
            "https://raw.githubusercontent.com/SrNadien/Ultimate-Tweaks/Neo/26.1.2/src/main/resources/logo.png";
    public static final String PACK_IMAGE = "pack";
    public static final String DIMENSION_ICON_BASE =
            "https://raw.githubusercontent.com/SrNadien/Ultimate-Tweaks/Neo/26.1.2/src/main/resources/assets/ultimatetweaks/textures/discord/";
    public static final String QUESTS_VARIABLE = "{quests}";
    public static final String DEFAULT_DETAILS = "{mods} Mods, {minecraft}, {version}";
    public static final List<String> DEFAULT_STATES = List.of(
            "{dimension}, " + QUESTS_VARIABLE,
            "{dimension}, Quests {questsShort}",
            "{dimension}, {questsShort}",
            QUESTS_VARIABLE,
            "{status}");
    public static final List<String> DETAILS_FALLBACKS =
            List.of("{mods} Mods, {version}", "{mods} Mods, {minecraft}", "{mods} Mods");
    public static final int DEFAULT_MAX_LINE_LENGTH = 26;
    public static final String DEFAULT_LARGE_IMAGE_TEXT = "{pack} - Minecraft {minecraft}";
    public static final String DEFAULT_STATUS_STARTING = "Starting Minecraft";
    public static final String DEFAULT_STATUS_MENU = "Main Menu";
    public static final String DEFAULT_STATUS_SINGLEPLAYER = "Dimension: {dimension}";
    public static final String DEFAULT_STATUS_MULTIPLAYER = "Dimension: {dimension}";
    public static final int DEFAULT_ROTATION_SECONDS = 0;

    public static PresenceSettings defaults() {
        return new PresenceSettings(true, true, DEFAULT_DETAILS, DEFAULT_STATES,
                "", DEFAULT_LARGE_IMAGE_TEXT, true, true, "",
                DEFAULT_STATUS_STARTING, DEFAULT_STATUS_MENU, DEFAULT_STATUS_SINGLEPLAYER, DEFAULT_STATUS_MULTIPLAYER,
                "", "", "", "");
    }

    public static String resolveApplicationId(String configured) {
        String value = configured == null ? "" : configured.trim();
        return value.matches("\\d{17,20}") ? value : APPLICATION_ID;
    }

    public List<String> effectiveStates() {
        return states == null || states.stream().allMatch(state -> state == null || state.isBlank())
                ? DEFAULT_STATES
                : states;
    }

    public String statusText(PresenceStatus status) {
        return switch (status) {
            case STARTING -> orDefault(statusStarting, DEFAULT_STATUS_STARTING);
            case MENU -> orDefault(statusMenu, DEFAULT_STATUS_MENU);
            case SINGLEPLAYER -> orDefault(statusSingleplayer, DEFAULT_STATUS_SINGLEPLAYER);
            case MULTIPLAYER -> orDefault(statusMultiplayer, DEFAULT_STATUS_MULTIPLAYER);
        };
    }

    private static String orDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
