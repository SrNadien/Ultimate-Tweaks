package nadiendev.ultimatetweaks.client.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class PresenceActivity {

    private static final int MAX_TEXT = 128;
    private static final int MAX_URL = 512;
    private static final int MAX_BUTTON_LABEL = 32;

    private PresenceActivity() {
    }

    public static PackInfo resolvePack(PresenceSettings settings, PackInfo curseforge, String minecraftVersion) {
        PackInfo source = settings.curseforgeSource() ? curseforge : PackInfo.EMPTY;

        return new PackInfo(
                firstNonBlank(settings.manualName(), source.name(),
                        ("Minecraft " + (minecraftVersion == null ? "" : minecraftVersion)).trim()),
                firstNonBlank(settings.manualVersion(), source.version()),
                firstNonBlank(settings.manualAuthor(), source.author()),
                firstNonBlank(settings.manualUrl(), source.url()),
                source.thumbnail());
    }

    public static JsonObject build(PresenceSettings settings, PackInfo pack, PresenceStatus status, String dimension,
                                   int mods, QuestProgress quests, String minecraftVersion, long startSeconds,
                                   int frame) {
        return build(settings, pack, status, dimension, mods, quests, minecraftVersion, startSeconds, frame, true,
                PresenceSettings.DEFAULT_MAX_LINE_LENGTH);
    }

    public static JsonObject build(PresenceSettings settings, PackInfo pack, PresenceStatus status, String dimension,
                                   int mods, QuestProgress quests, String minecraftVersion, long startSeconds,
                                   int frame, boolean smallIcons) {
        return build(settings, pack, status, dimension, mods, quests, minecraftVersion, startSeconds, frame,
                smallIcons, PresenceSettings.DEFAULT_MAX_LINE_LENGTH);
    }

    public static JsonObject build(PresenceSettings settings, PackInfo pack, PresenceStatus status, String dimension,
                                   int mods, QuestProgress quests, String minecraftVersion, long startSeconds,
                                   int frame, boolean smallIcons, int maxLine) {
        Function<String, String> icons = smallIcons
                ? name -> PresenceSettings.DIMENSION_ICON_BASE + name + ".png"
                : name -> "";
        return build(settings, pack, status, dimension, mods, quests, minecraftVersion, startSeconds, frame, icons,
                maxLine);
    }

    public static JsonObject build(PresenceSettings settings, PackInfo pack, PresenceStatus status, String dimension,
                                   int mods, QuestProgress quests, String minecraftVersion, long startSeconds,
                                   int frame, Function<String, String> smallImage, int maxLine) {
        boolean questsAvailable = quests != null && quests.isAvailable();

        Map<String, String> variables = new LinkedHashMap<>();
        variables.put("pack", pack.name());
        variables.put("version", pack.version());
        variables.put("author", pack.author());
        variables.put("mods", Integer.toString(mods));
        variables.put("dimension", dimension == null ? "" : dimension);
        variables.put("status", render(settings.statusText(status), variables));
        variables.put("minecraft", minecraftVersion == null ? "" : minecraftVersion);
        variables.put("questsShort", questsAvailable ? quests.done() + "/" + quests.total() : "");
        variables.put("quests", questsAvailable ? "Misiones: " + variables.get("questsShort") : "");

        JsonObject activity = new JsonObject();
        activity.addProperty("type", 0);
        putText(activity, "name", pack.name());
        putText(activity, "details", pickDetails(settings, variables, pack.name(), maxLine));
        putText(activity, "state", pickState(settings, variables, frame, maxLine));

        if (settings.showElapsed() && startSeconds > 0) {
            JsonObject timestamps = new JsonObject();
            timestamps.addProperty("start", startSeconds);
            activity.add("timestamps", timestamps);
        }

        String image = resolveImage(settings.largeImage(), pack);
        if (!image.isBlank()) {
            JsonObject assets = new JsonObject();
            assets.addProperty("large_image", truncate(image, MAX_URL));
            String imageText = renderOrDefault(settings.largeImageText(), PresenceSettings.DEFAULT_LARGE_IMAGE_TEXT,
                    variables, pack.name());
            assets.addProperty("large_text", truncate(imageText, MAX_TEXT));

            String smallKey = PresenceDimension.assetKey(dimension);
            String small = smallKey.isEmpty() ? "" : smallImage.apply(PresenceDimension.iconName(dimension));
            if (small != null && !small.isBlank()) {
                String smallText = variables.get("status").length() >= 2 ? variables.get("status") : dimension;
                assets.addProperty("small_image", truncate(small, MAX_URL));
                assets.addProperty("small_text", truncate(smallText, MAX_TEXT));
            }
            activity.add("assets", assets);
        }

        JsonArray buttons = new JsonArray();
        if (settings.curseforgeButton() && isWebUrl(pack.url())) {
            buttons.add(button("CurseForge", pack.url()));
        }
        if (isWebUrl(settings.inviteUrl())) {
            buttons.add(button("Discord", settings.inviteUrl()));
        }
        if (!buttons.isEmpty()) {
            activity.add("buttons", buttons);
        }

        return activity;
    }

    private static String pickDetails(PresenceSettings settings, Map<String, String> variables, String lastResort,
                                      int maxLine) {
        List<String> templates = new ArrayList<>();
        templates.add(settings.details());
        templates.add(PresenceSettings.DEFAULT_DETAILS);
        templates.addAll(PresenceSettings.DETAILS_FALLBACKS);

        List<String> texts = renderedCandidates(templates, variables);
        return texts.isEmpty() ? lastResort : firstThatFits(texts, maxLine);
    }

    private static String pickState(PresenceSettings settings, Map<String, String> variables, int frame,
                                    int maxLine) {
        List<String> texts = renderedCandidates(settings.effectiveStates(), variables);
        if (texts.isEmpty()) {
            return variables.get("mods") + " Mods";
        }
        if (frame >= 0) {
            return texts.get(Math.floorMod(frame, texts.size()));
        }
        return firstThatFits(texts, maxLine);
    }

    private static List<String> renderedCandidates(List<String> templates, Map<String, String> variables) {
        List<String> texts = new ArrayList<>();
        for (String template : templates) {
            if (usesEmptyVariable(template, variables)) {
                continue;
            }
            String text = render(template, variables);
            if (text.length() >= 2 && !texts.contains(text)) {
                texts.add(text);
            }
        }
        return texts;
    }

    private static String firstThatFits(List<String> texts, int maxLine) {
        String shortest = texts.get(0);
        for (String text : texts) {
            if (text.length() <= maxLine) {
                return text;
            }
            if (text.length() < shortest.length()) {
                shortest = text;
            }
        }
        return shortest;
    }

    private static String resolveImage(String configured, PackInfo pack) {
        if (configured == null || configured.isBlank()) {
            return PresenceSettings.MOD_LOGO_URL;
        }
        if (PresenceSettings.PACK_IMAGE.equalsIgnoreCase(configured.trim())) {
            return firstNonBlank(pack.thumbnail(), PresenceSettings.MOD_LOGO_URL);
        }
        return configured.trim();
    }

    private static JsonObject button(String label, String url) {
        JsonObject button = new JsonObject();
        button.addProperty("label", truncate(label, MAX_BUTTON_LABEL));
        button.addProperty("url", url);
        return button;
    }

    private static void putText(JsonObject activity, String key, String text) {
        if (text.length() >= 2) {
            activity.addProperty(key, truncate(text, MAX_TEXT));
        }
    }

    private static String renderOrDefault(String template, String defaultTemplate, Map<String, String> variables,
                                          String lastResort) {
        String text = render(template, variables);
        if (text.length() >= 2) {
            return text;
        }
        text = render(defaultTemplate, variables);
        return text.length() >= 2 ? text : lastResort;
    }

    private static boolean usesEmptyVariable(String template, Map<String, String> variables) {
        if (template == null) {
            return true;
        }
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (template.contains("{" + entry.getKey() + "}") && entry.getValue().isBlank()) {
                return true;
            }
        }
        return false;
    }

    private static String render(String template, Map<String, String> variables) {
        String result = template == null ? "" : template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result.replaceAll("\\s+", " ").replaceAll("^[\\s:|-]+|[\\s:|-]+$", "").trim();
    }

    private static boolean isWebUrl(String value) {
        return value != null && value.length() <= MAX_URL
                && (value.startsWith("https://") || value.startsWith("http://"));
    }

    private static String truncate(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }
}
