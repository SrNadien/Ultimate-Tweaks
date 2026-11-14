package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaks;
import nadiendev.ultimatetweaks.registry.MaterialItems;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        public String getName() {
            return "Recipes: " + UltimateTweaks.MOD_ID;
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }
    }

    @Override
    protected void buildRecipes() {
        Ingredient hammer = tag(MaterialItems.HAMMERS);
        Ingredient cutter = tag(MaterialItems.CUTTERS);
        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
            Criterion<InventoryChangeTrigger.TriggerInstance> unlock = material.sourceTag() != null
                    ? has(material.sourceTag())
                    : has(material.item().get());
            Ingredient base = material.sourceTag() != null
                    ? tag(material.sourceTag())
                    : Ingredient.of(material.item().get());

            shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.PLATE).get(),
                    MaterialItems.Kind.PLATE.recipeCount())
                    .pattern("a  ")
                    .pattern("b  ")
                    .pattern("   ")
                    .define('a', hammer)
                    .define('b', base)
                    .unlockedBy("has_material", unlock)
                    .save(output);

            shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.GEAR).get(),
                    MaterialItems.Kind.GEAR.recipeCount())
                    .pattern(" a ")
                    .pattern("aba")
                    .pattern(" a ")
                    .define('a', base)
                    .define('b', hammer)
                    .unlockedBy("has_material", unlock)
                    .save(output);

            shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.STICK).get(),
                    MaterialItems.Kind.STICK.recipeCount())
                    .pattern(" a ")
                    .pattern(" b ")
                    .pattern(" b ")
                    .define('a', hammer)
                    .define('b', base)
                    .unlockedBy("has_material", unlock)
                    .save(output);

            shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.WIRE).get(),
                    MaterialItems.Kind.WIRE.recipeCount())
                    .pattern("a  ")
                    .pattern("b  ")
                    .pattern("   ")
                    .define('a', cutter)
                    .define('b', tag(material.plateTag()))
                    .unlockedBy("has_material", unlock)
                    .save(output);

            shaped(RecipeCategory.TOOLS, MaterialItems.holder(material, MaterialItems.Kind.HAMMER).get(),
                    MaterialItems.Kind.HAMMER.recipeCount())
                    .pattern("AAA")
                    .pattern("ABA")
                    .pattern(" B ")
                    .define('A', base)
                    .define('B', Items.STICK)
                    .unlockedBy("has_material", unlock)
                    .save(output);

            shaped(RecipeCategory.TOOLS, MaterialItems.holder(material, MaterialItems.Kind.CUTTER).get(),
                    MaterialItems.Kind.CUTTER.recipeCount())
                    .pattern("A A")
                    .pattern(" B ")
                    .pattern("B B")
                    .define('A', base)
                    .define('B', Items.STICK)
                    .unlockedBy("has_material", unlock)
                    .save(output);
        }
    }
}
