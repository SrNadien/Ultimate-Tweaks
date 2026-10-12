package nadiendev.ultimatetweaks.client.discord;

import java.util.Locale;

public final class PresenceDimension {

    private PresenceDimension() {
    }

    public static String displayName(String namespace, String path) {
        if ("minecraft".equals(namespace)) {
            switch (path) {
                case "overworld":
                    return "Overworld";
                case "the_nether":
                    return "Nether";
                case "the_end":
                    return "End";
                default:
                    break;
            }
        }
        return prettify(path);
    }

    public static String iconName(String displayName) {
        String key = assetKey(displayName);
        return key.equals("overworld") || key.equals("nether") || key.equals("end") ? key : "other";
    }

    public static String assetKey(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return "";
        }
        return displayName.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
    }

    private static String prettify(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (String word : path.split("[_/\\-\\s]+")) {
            if (word.isEmpty()) {
                continue;
            }
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(word.substring(0, 1).toUpperCase(Locale.ROOT)).append(word.substring(1));
        }
        return result.toString();
    }
}
