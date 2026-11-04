package nadiendev.ultimatetweaks.client.hearts;

import nadiendev.ultimatetweaks.config.TweaksClientConfig;

import java.util.List;

public final class HeartColors {

    private static final float[] FALLBACK = {1.0F, 0.07F, 0.07F};

    private static List<? extends String> source;
    private static float[][] parsed = {FALLBACK};

    private HeartColors() {
    }

    public static float[] of(int layer) {
        List<? extends String> configured = TweaksClientConfig.HEARTS_COLORS.get();
        if (configured != source) {
            source = configured;
            parsed = parse(configured);
        }
        return parsed[Math.floorMod(layer, parsed.length)];
    }

    private static float[][] parse(List<? extends String> colors) {
        if (colors.isEmpty()) {
            return new float[][]{FALLBACK};
        }
        float[][] result = new float[colors.size()][];
        for (int i = 0; i < result.length; i++) {
            result[i] = parseColor(colors.get(i));
        }
        return result;
    }

    private static float[] parseColor(String text) {
        String hex = text.startsWith("#") ? text.substring(1) : text;
        try {
            int value = Integer.parseInt(hex, 16);
            return new float[]{(value >> 16 & 0xFF) / 255.0F, (value >> 8 & 0xFF) / 255.0F, (value & 0xFF) / 255.0F};
        } catch (NumberFormatException e) {
            return FALLBACK;
        }
    }
}
