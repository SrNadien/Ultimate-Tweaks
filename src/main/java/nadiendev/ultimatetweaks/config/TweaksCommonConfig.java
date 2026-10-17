package nadiendev.ultimatetweaks.config;

import nadiendev.ultimatetweaks.worldgen.AstralStructureOverrides;
import nadiendev.ultimatetweaks.worldgen.AstralStructures;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TweaksCommonConfig {

    public static final ModConfigSpec SPEC;

    private record StructureValues(ModConfigSpec.BooleanValue enabled, ModConfigSpec.IntValue spacing,
                                   ModConfigSpec.IntValue separation,
                                   ModConfigSpec.ConfigValue<List<? extends String>> biomes) {

        AstralStructureOverrides.Settings read() {
            return new AstralStructureOverrides.Settings(enabled.get(), spacing.get(), separation.get(),
                    List.copyOf(biomes.get()));
        }
    }

    private static final Map<String, StructureValues> ASTRAL_STRUCTURES = new LinkedHashMap<>();

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Estructuras de Astral Sorcery 2.0.2 o superior.",
                        "Los cambios se aplican al abrir el mundo y solo afectan a chunks que aun no se han generado.",
                        "Hace falta Astral Sorcery; sin el, esta seccion no hace nada.")
               .push("astralStructures");

        for (AstralStructures.Entry entry : AstralStructures.ALL) {
            builder.comment("Estructura " + entry.name() + " (Astral Sorcery: spacing " + entry.spacing()
                            + ", separation " + entry.separation() + ").")
                   .push(entry.name());

            ModConfigSpec.BooleanValue enabled = builder
                    .comment("false hace que no se genere nunca.")
                    .define("enabled", true);
            ModConfigSpec.IntValue spacing = builder
                    .comment("Distancia media en chunks entre intentos de generarla; mas alto = mas rara.",
                             "-1 usa el valor de Astral Sorcery.")
                    .defineInRange("spacing", -1, -1, 4096);
            ModConfigSpec.IntValue separation = builder
                    .comment("Distancia minima en chunks entre dos; siempre menor que spacing.",
                             "-1 usa el valor de Astral Sorcery.")
                    .defineInRange("separation", -1, -1, 4096);
            ModConfigSpec.ConfigValue<List<? extends String>> biomes = builder
                    .comment("Biomas donde se genera: ids (minecraft:plains) o tags con # (#c:is_forest).",
                             "Vacio usa los de Astral Sorcery.")
                    .defineListAllowEmpty("biomes", List.of(), () -> "", value -> value instanceof String);

            ASTRAL_STRUCTURES.put(entry.name(), new StructureValues(enabled, spacing, separation, biomes));
            builder.pop();
        }

        builder.pop();

        SPEC = builder.build();
    }

    public static AstralStructureOverrides.Settings astralStructure(String name) {
        StructureValues values = ASTRAL_STRUCTURES.get(name);
        return values == null ? AstralStructureOverrides.Settings.DEFAULT : values.read();
    }

    private TweaksCommonConfig() {
    }
}
