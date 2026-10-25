package nadiendev.ultimatetweaks.worldgen;

import java.util.List;

public final class AstralStructures {

    public static final String MOD_ID = "astralsorcery";

    public record Entry(String name, int spacing, int separation) {

        public String structureSetPath() {
            return "data/" + MOD_ID + "/worldgen/structure_set/" + name + "_structures.json";
        }

        public String biomeTagPath() {
            return "data/" + MOD_ID + "/tags/worldgen/biome/" + name + "_biomes.json";
        }
    }

    public static final List<Entry> ALL = List.of(
            new Entry("focal_point", 10, 4),
            new Entry("obliteration", 80, 14),
            new Entry("dig_site", 24, 6),
            new Entry("moon_dial", 20, 16),
            new Entry("column", 24, 12),
            new Entry("rotunda", 40, 32));

    private AstralStructures() {
    }
}
