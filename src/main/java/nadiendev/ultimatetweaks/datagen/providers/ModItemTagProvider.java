package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.registry.MaterialItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends TagsProvider<Item> {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, lookupProvider, UltimateTweaks.MOD_ID, existingFileHelper);
    }

    private static TagKey<Item> common(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
            for (MaterialItems.Kind kind : MaterialItems.Kind.values()) {
                var key = MaterialItems.holder(material, kind).getKey();
                if (kind == MaterialItems.Kind.HAMMER) {
                    tag(MaterialItems.HAMMERS).add(key);
                    tag(common("tools/hammers")).add(key);
                    continue;
                }
                if (kind == MaterialItems.Kind.CUTTER) {
                    tag(MaterialItems.CUTTERS).add(key);
                    tag(common("tools/cutters")).add(key);
                    continue;
                }
                tag(common(kind.commonTag())).add(key);
                tag(common(kind.commonTag() + "/" + material.tagId())).add(key);
            }
        }
        tag(MaterialItems.ANDESITE_ALLOY_SOURCE)
                .addOptional(ResourceLocation.fromNamespaceAndPath("create", "andesite_alloy"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "ingots/andesite_alloy"));
        tag(MaterialItems.CHOCOLATE_SOURCE)
                .addOptional(ResourceLocation.fromNamespaceAndPath("create", "bar_of_chocolate"));
    }
}
