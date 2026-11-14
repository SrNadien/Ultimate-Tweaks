package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.item.CraftingToolItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class MaterialItems {

    public enum Kind {
        PLATE("plate", "%s Plate", "Placa de %s", "plates", 4),
        STICK("stick", "%s Stick", "Palo de %s", "rods", 4),
        GEAR("gear", "%s Gear", "Engranaje de %s", "gears", 4),
        WIRE("wire", "%s Wire", "Alambre de %s", "wires", 4),
        HAMMER("hammer", "%s Hammer", "Martillo de %s", null, 1),
        CUTTER("cutter", "%s Wire Cutter", "Cortacables de %s", null, 1);

        private final String id;
        private final String english;
        private final String spanish;
        private final String commonTag;
        private final int recipeCount;

        Kind(String id, String english, String spanish, String commonTag, int recipeCount) {
            this.id = id;
            this.english = english;
            this.spanish = spanish;
            this.commonTag = commonTag;
            this.recipeCount = recipeCount;
        }

        public String id() {
            return id;
        }

        public String english() {
            return english;
        }

        public String spanish() {
            return spanish;
        }

        public String commonTag() {
            return commonTag;
        }

        public int recipeCount() {
            return recipeCount;
        }
    }

    public record Material(String id, String tagId, String english, String spanish, Supplier<ItemLike> item,
                           TagKey<Item> sourceTag, int toolDurability) {

        public Material(String id, String english, String spanish, Supplier<ItemLike> item, int toolDurability) {
            this(id, id, english, spanish, item, null, toolDurability);
        }

        public TagKey<Item> plateTag() {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "plates/" + tagId));
        }
    }

    public static final TagKey<Item> HAMMERS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "hammers"));

    public static final TagKey<Item> CUTTERS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "cutters"));

    public static final TagKey<Item> ANDESITE_ALLOY_SOURCE =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "materials/andesite_alloy"));

    public static final TagKey<Item> CHOCOLATE_SOURCE =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "materials/chocolate"));

    public static final List<Material> MATERIALS = List.of(
            new Material("copper", "Copper", "Cobre", () -> Items.COPPER_INGOT, 190),
            new Material("iron", "Iron", "Hierro", () -> Items.IRON_INGOT, 250),
            new Material("gold", "Gold", "Oro", () -> Items.GOLD_INGOT, 64),
            new Material("emerald", "Emerald", "Esmeralda", () -> Items.EMERALD, 750),
            new Material("netherite", "Netherite", "Netherite", () -> Items.NETHERITE_INGOT, 2031),
            new Material("amethyst", "Amethyst", "Amatista", () -> Items.AMETHYST_SHARD, 300),
            new Material("obsidian", "Obsidian", "Obsidiana", () -> Items.OBSIDIAN, 1000),
            new Material("crying_obsidian", "Crying Obsidian", "Obsidiana Llorona", () -> Items.CRYING_OBSIDIAN, 1200),
            new Material("andesite", "andesite_alloy", "Andesite", "Andesita", null, ANDESITE_ALLOY_SOURCE, 120),
            new Material("chocolate", "chocolate", "Chocolate", "Chocolate", null, CHOCOLATE_SOURCE, 40)
    );

    private static final Map<String, DeferredHolder<Item, Item>> HOLDERS = new LinkedHashMap<>();

    static {
        for (Material material : MATERIALS) {
            for (Kind kind : Kind.values()) {
                String name = material.id() + "_" + kind.id();
                HOLDERS.put(name, ItemsAdds.ITEMS.registerItem(name, properties -> create(material, kind, properties)));
            }
        }
    }

    private MaterialItems() {
    }

    private static Item create(Material material, Kind kind, Item.Properties properties) {
        if (kind == Kind.HAMMER || kind == Kind.CUTTER) {
            return new CraftingToolItem(properties.durability(material.toolDurability()));
        }
        return new Item(properties);
    }

    public static void init() {
    }

    public static DeferredHolder<Item, Item> holder(Material material, Kind kind) {
        return HOLDERS.get(material.id() + "_" + kind.id());
    }

    public static List<DeferredHolder<Item, Item>> all() {
        return new ArrayList<>(HOLDERS.values());
    }
}
