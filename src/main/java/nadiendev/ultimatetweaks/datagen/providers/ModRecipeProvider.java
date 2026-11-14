package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.registry.MaterialItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput writer) {
        Ingredient hammer = Ingredient.of(MaterialItems.HAMMERS);
        Ingredient cutter = Ingredient.of(MaterialItems.CUTTERS);
        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
            Criterion<InventoryChangeTrigger.TriggerInstance> unlock = material.sourceTag() != null
                    ? has(material.sourceTag())
                    : has(material.item().get());
            Ingredient base = material.ingredient();

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.PLATE).get(),
                            MaterialItems.Kind.PLATE.recipeCount())
                    .pattern("a  ")
                    .pattern("b  ")
                    .pattern("   ")
                    .define('a', hammer)
                    .define('b', base)
                    .unlockedBy("has_material", unlock)
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.GEAR).get(),
                            MaterialItems.Kind.GEAR.recipeCount())
                    .pattern(" a ")
                    .pattern("aba")
                    .pattern(" a ")
                    .define('a', base)
                    .define('b', hammer)
                    .unlockedBy("has_material", unlock)
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.STICK).get(),
                            MaterialItems.Kind.STICK.recipeCount())
                    .pattern(" a ")
                    .pattern(" b ")
                    .pattern(" b ")
                    .define('a', hammer)
                    .define('b', base)
                    .unlockedBy("has_material", unlock)
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MaterialItems.holder(material, MaterialItems.Kind.WIRE).get(),
                            MaterialItems.Kind.WIRE.recipeCount())
                    .pattern("a  ")
                    .pattern("b  ")
                    .pattern("   ")
                    .define('a', cutter)
                    .define('b', Ingredient.of(material.plateTag()))
                    .unlockedBy("has_material", unlock)
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MaterialItems.holder(material, MaterialItems.Kind.HAMMER).get(),
                            MaterialItems.Kind.HAMMER.recipeCount())
                    .pattern("AAA")
                    .pattern("ABA")
                    .pattern(" B ")
                    .define('A', base)
                    .define('B', Items.STICK)
                    .unlockedBy("has_material", unlock)
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MaterialItems.holder(material, MaterialItems.Kind.CUTTER).get(),
                            MaterialItems.Kind.CUTTER.recipeCount())
                    .pattern("A A")
                    .pattern(" B ")
                    .pattern("B B")
                    .define('A', base)
                    .define('B', Items.STICK)
                    .unlockedBy("has_material", unlock)
                    .save(writer);
        }
    }
}
