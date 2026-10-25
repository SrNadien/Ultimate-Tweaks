package nadiendev.ultimatetweaks.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public final class AstralStructureOverrides {

    public record Settings(boolean enabled, int spacing, int separation, List<String> biomes) {

        public static final Settings DEFAULT = new Settings(true, -1, -1, List.of());

        public boolean isDefault() {
            return enabled && spacing < 0 && separation < 0 && biomes.isEmpty();
        }
    }

    private AstralStructureOverrides() {
    }

    public static Optional<String> structureSet(String originalJson, Settings settings) {
        if (!settings.enabled() || (settings.spacing() < 0 && settings.separation() < 0)) {
            return Optional.empty();
        }

        JsonObject root = JsonParser.parseString(originalJson).getAsJsonObject();
        JsonObject placement = root.getAsJsonObject("placement");
        if (placement == null || !placement.has("spacing") || !placement.has("separation")) {
            return Optional.empty();
        }

        int spacing = settings.spacing() >= 0 ? settings.spacing() : placement.get("spacing").getAsInt();
        int separation = settings.separation() >= 0 ? settings.separation() : placement.get("separation").getAsInt();
        spacing = Math.max(1, spacing);
        separation = Math.min(separation, spacing - 1);

        placement.addProperty("spacing", spacing);
        placement.addProperty("separation", separation);
        return Optional.of(root.toString());
    }

    public static Optional<String> biomeTag(Settings settings) {
        JsonArray values = new JsonArray();

        if (settings.enabled()) {
            if (settings.biomes().isEmpty()) {
                return Optional.empty();
            }
            for (String entry : settings.biomes()) {
                String id = entry.trim();
                if (isValidEntry(id)) {
                    JsonObject value = new JsonObject();
                    value.addProperty("id", id);
                    value.addProperty("required", false);
                    values.add(value);
                }
            }
        }

        JsonObject root = new JsonObject();
        root.addProperty("replace", true);
        root.add("values", values);
        return Optional.of(root.toString());
    }

    public static boolean isValidEntry(String entry) {
        String id = entry.startsWith("#") ? entry.substring(1) : entry;
        return ResourceLocation.tryParse(id) != null;
    }
}
