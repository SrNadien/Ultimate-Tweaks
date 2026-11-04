package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent;
import dev.ftb.mods.ftbteams.api.property.BooleanProperty;
import dev.ftb.mods.ftbteams.api.property.DoubleProperty;
import dev.ftb.mods.ftbteams.api.property.IntProperty;
import dev.ftb.mods.ftbteams.api.property.StringListProperty;
import dev.ftb.mods.ftbteams.api.property.StringProperty;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class ZoneFlags {

    private static final Map<ZoneFlagDef, TeamProperty<?>> PROPERTIES = new EnumMap<>(ZoneFlagDef.class);

    private static final String LEGACY_GREETING = "&lEntrando al rancho de &b&l{team}";
    private static final String LEGACY_FAREWELL = "&lSaliendo del rancho de &b&l{team}";
    private static final String LEGACY_GREETING_TITLE = "&lEntrando al rancho de\\n&b&l{team}";
    private static final String LEGACY_FAREWELL_TITLE = "&lSaliendo del rancho de\\n&b&l{team}";
    private static final Map<ZoneFlagDef, Set<String>> LEGACY_TEXT_DEFAULTS = Map.of(
            ZoneFlagDef.GREETING, Set.of(LEGACY_GREETING),
            ZoneFlagDef.GREETING_TITLE, Set.of(LEGACY_GREETING, LEGACY_GREETING_TITLE),
            ZoneFlagDef.FAREWELL, Set.of(LEGACY_FAREWELL),
            ZoneFlagDef.FAREWELL_TITLE, Set.of(LEGACY_FAREWELL, LEGACY_FAREWELL_TITLE));

    static {
        for (ZoneFlagDef def : ZoneFlagDef.values()) {
            PROPERTIES.put(def, create(def));
        }
    }

    private ZoneFlags() {
    }

    @SuppressWarnings("unchecked")
    private static TeamProperty<?> create(ZoneFlagDef def) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UltimateTweaks.MOD_ID, def.path());
        return switch (def.kind()) {
            case STATE -> new BooleanProperty(id, (Boolean) def.defaultValue());
            case TEXT -> new StringProperty(id, (String) def.defaultValue());
            case LIST -> new StringListProperty(id, new ArrayList<>((List<String>) def.defaultValue()));
            case INT -> new IntProperty(id, (Integer) def.defaultValue(), def.min().intValue(), def.max().intValue());
            case DOUBLE -> new DoubleProperty(id, (Double) def.defaultValue(), def.min().doubleValue(),
                    def.max().doubleValue());
        };
    }

    public static boolean enabled(ZoneFlagDef def) {
        List<? extends String> configured = TweaksCommonConfig.zoneEnabledFlags();
        if (configured != enabledSource) {
            Set<String> keys = new HashSet<>();
            configured.forEach(key -> keys.add(key.trim().toLowerCase(Locale.ROOT)));
            enabledKeys = keys;
            enabledSource = configured;
        }
        if (enabledKeys.contains(def.key())) {
            return true;
        }
        return switch (def) {
            case GREETING_TITLE -> enabledKeys.contains(ZoneFlagDef.GREETING.key());
            case FAREWELL_TITLE -> enabledKeys.contains(ZoneFlagDef.FAREWELL.key());
            default -> false;
        };
    }

    public static void collect(TeamCollectPropertiesEvent event) {
        PROPERTIES.forEach((def, property) -> {
            if (enabled(def)) {
                event.add(property);
            }
        });
    }

    private static List<? extends String> enabledSource;
    private static Set<String> enabledKeys = Set.of();

    @SuppressWarnings("unchecked")
    private static <T> T value(Team team, ZoneFlagDef def) {
        return team.getProperty((TeamProperty<T>) PROPERTIES.get(def));
    }

    public static boolean state(Team team, ZoneFlagDef def) {
        if (!enabled(def)) {
            return (Boolean) def.defaultValue();
        }
        Boolean value = value(team, def);
        return value != null && value;
    }

    public static String text(Team team, ZoneFlagDef def) {
        if (!enabled(def)) {
            return (String) def.defaultValue();
        }
        String value = value(team, def);
        return value == null ? "" : value;
    }

    @SuppressWarnings("unchecked")
    public static int migrateLegacyTexts(Team team) {
        int changed = 0;
        for (Map.Entry<ZoneFlagDef, Set<String>> entry : LEGACY_TEXT_DEFAULTS.entrySet()) {
            ZoneFlagDef def = entry.getKey();
            if (!enabled(def)) {
                continue;
            }
            TeamProperty<String> property = (TeamProperty<String>) PROPERTIES.get(def);
            String current = team.getProperty(property);
            if (current != null && entry.getValue().contains(current)) {
                team.setProperty(property, (String) def.defaultValue());
                changed++;
            }
        }
        if (changed > 0) {
            team.markDirty();
        }
        return changed;
    }

    public static List<String> list(Team team, ZoneFlagDef def) {
        if (!enabled(def)) {
            return List.of();
        }
        List<String> value = value(team, def);
        return value == null ? List.of() : value;
    }

    public static int integer(Team team, ZoneFlagDef def) {
        if (!enabled(def)) {
            return (Integer) def.defaultValue();
        }
        Integer value = value(team, def);
        return value == null ? 0 : value;
    }

    public static double decimal(Team team, ZoneFlagDef def) {
        if (!enabled(def)) {
            return (Double) def.defaultValue();
        }
        Double value = value(team, def);
        return value == null ? 0.0D : value;
    }
}
