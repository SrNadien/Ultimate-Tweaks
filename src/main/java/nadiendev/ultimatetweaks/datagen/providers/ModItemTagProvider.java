package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.registry.MaterialItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, UltimateTweaks.MOD_ID);
    }

    private static TagKey<Item> common(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
            for (MaterialItems.Kind kind : MaterialItems.Kind.values()) {
                Item item = MaterialItems.holder(material, kind).get();
                if (kind == MaterialItems.Kind.HAMMER) {
                    tag(MaterialItems.HAMMERS).add(item);
                    tag(common("tools/hammers")).add(item);
                    continue;
                }
                if (kind == MaterialItems.Kind.CUTTER) {
                    tag(MaterialItems.CUTTERS).add(item);
                    tag(common("tools/cutters")).add(item);
                    continue;
                }
                tag(common(kind.commonTag())).add(item);
                tag(common(kind.commonTag() + "/" + material.tagId())).add(item);
            }
        }
        getOrCreateRawBuilder(MaterialItems.ANDESITE_ALLOY_SOURCE)
                .addOptionalElement(Identifier.fromNamespaceAndPath("create", "andesite_alloy"))
                .addOptionalTag(Identifier.fromNamespaceAndPath("c", "ingots/andesite_alloy"));
        getOrCreateRawBuilder(MaterialItems.CHOCOLATE_SOURCE)
                .addOptionalElement(Identifier.fromNamespaceAndPath("create", "bar_of_chocolate"));
    }
}
