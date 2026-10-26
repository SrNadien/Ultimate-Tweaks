package nadiendev.ultimatetweaks.integration.lootr;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LootrStructureRules extends SimpleJsonResourceReloadListener<LootrStructureRules.Raw> {

    public static final String DIRECTORY = "ultimatetweaks/lootr_structures";

    public record Raw(List<String> structures, Identifier lootTable, boolean replaceItems) {

        static final Codec<Raw> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.listOf().fieldOf("structures").forGetter(Raw::structures),
                Identifier.CODEC.fieldOf("loot_table").forGetter(Raw::lootTable),
                Codec.BOOL.optionalFieldOf("replace_items", true).forGetter(Raw::replaceItems)
        ).apply(instance, Raw::new));
    }

    public record Rule(Identifier id, Set<Identifier> structures, List<TagKey<Structure>> tags,
                       ResourceKey<LootTable> lootTable, boolean replaceItems) {

        boolean matches(Registry<Structure> registry, Structure structure) {
            Identifier key = registry.getKey(structure);
            if (key != null && structures.contains(key)) {
                return true;
            }
            if (tags.isEmpty()) {
                return false;
            }
            return registry.getResourceKey(structure)
                    .flatMap(registry::get)
                    .map(holder -> tags.stream().anyMatch(holder::is))
                    .orElse(false);
        }
    }

    private static volatile List<Rule> rules = List.of();

    public LootrStructureRules() {
        super(Raw.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Nullable
    public static Rule find(Registry<Structure> registry, Structure structure) {
        for (Rule rule : rules) {
            if (rule.matches(registry, structure)) {
                return rule;
            }
        }
        return null;
    }

    @Override
    protected void apply(Map<Identifier, Raw> files, ResourceManager manager, ProfilerFiller profiler) {
        List<Rule> loaded = new ArrayList<>();
        files.forEach((id, raw) -> {
            try {
                loaded.add(build(id, raw));
            } catch (RuntimeException e) {
                UltimateTweaksCore.LOGGER.error("Regla de Lootr {} invalida: {}", id, e.getMessage());
            }
        });
        rules = List.copyOf(loaded);
        UltimateTweaksCore.LOGGER.info("Reglas de Lootr por estructura cargadas: {}", rules.size());
    }

    private static Rule build(Identifier id, Raw raw) {
        Set<Identifier> structures = new HashSet<>();
        List<TagKey<Structure>> tags = new ArrayList<>();
        for (String text : raw.structures()) {
            if (text.startsWith("#")) {
                tags.add(TagKey.create(Registries.STRUCTURE, Identifier.parse(text.substring(1))));
            } else {
                structures.add(Identifier.parse(text));
            }
        }
        return new Rule(id, Set.copyOf(structures), List.copyOf(tags),
                ResourceKey.create(Registries.LOOT_TABLE, raw.lootTable()), raw.replaceItems());
    }
}
