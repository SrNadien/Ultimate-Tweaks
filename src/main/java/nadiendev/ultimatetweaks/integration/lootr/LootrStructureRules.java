package nadiendev.ultimatetweaks.integration.lootr;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LootrStructureRules extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "ultimatetweaks/lootr_structures";

    public record Rule(ResourceLocation id, Set<ResourceLocation> structures, List<TagKey<Structure>> tags,
                       ResourceKey<LootTable> lootTable, boolean replaceItems) {

        boolean matches(Registry<Structure> registry, Structure structure) {
            ResourceLocation key = registry.getKey(structure);
            if (key != null && structures.contains(key)) {
                return true;
            }
            if (tags.isEmpty()) {
                return false;
            }
            return registry.getResourceKey(structure)
                    .flatMap(registry::getHolder)
                    .map(holder -> tags.stream().anyMatch(holder::is))
                    .orElse(false);
        }
    }

    private static volatile List<Rule> rules = List.of();

    public LootrStructureRules() {
        super(new Gson(), DIRECTORY);
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
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        List<Rule> loaded = new ArrayList<>();
        files.forEach((id, element) -> {
            try {
                loaded.add(parse(id, GsonHelper.convertToJsonObject(element, id.toString())));
            } catch (RuntimeException e) {
                UltimateTweaksCore.LOGGER.error("Regla de Lootr {} invalida: {}", id, e.getMessage());
            }
        });
        rules = List.copyOf(loaded);
        UltimateTweaksCore.LOGGER.info("Reglas de Lootr por estructura cargadas: {}", rules.size());
    }

    private static Rule parse(ResourceLocation id, JsonObject json) {
        Set<ResourceLocation> structures = new HashSet<>();
        List<TagKey<Structure>> tags = new ArrayList<>();
        JsonArray list = GsonHelper.getAsJsonArray(json, "structures");
        for (JsonElement entry : list) {
            String text = GsonHelper.convertToString(entry, "structures");
            if (text.startsWith("#")) {
                tags.add(TagKey.create(Registries.STRUCTURE, ResourceLocation.parse(text.substring(1))));
            } else {
                structures.add(ResourceLocation.parse(text));
            }
        }
        ResourceKey<LootTable> table = ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.parse(GsonHelper.getAsString(json, "loot_table")));
        boolean replaceItems = GsonHelper.getAsBoolean(json, "replace_items", true);
        return new Rule(id, Set.copyOf(structures), List.copyOf(tags), table, replaceItems);
    }
}
