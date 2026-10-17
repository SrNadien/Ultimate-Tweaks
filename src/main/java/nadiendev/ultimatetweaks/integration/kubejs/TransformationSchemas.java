package nadiendev.ultimatetweaks.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidIngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.SizedIngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

public interface TransformationSchemas {

    RecipeKey<List<ItemStack>> RESULTS = ItemStackComponent.ITEM_STACK.asList().outputKey("results");
    RecipeKey<SizedIngredient> INGREDIENT = SizedIngredientComponent.SIZED_INGREDIENT.inputKey("ingredient");
    RecipeKey<FluidIngredient> FLUID = FluidIngredientComponent.FLUID_INGREDIENT.inputKey("fluid");
    RecipeKey<Integer> DURATION = NumberComponent.INT.otherKey("duration").optional(20).alwaysWrite();
    RecipeKey<Float> CONSUME_FLUID_CHANCE = NumberComponent.FLOAT.otherKey("consume_fluid_chance").optional(0.0F)
            .alwaysWrite().functionNames("consumeFluidChance", "consumeChance");

    RecipeSchema ITEM_IN_FLUID = new RecipeSchema(RESULTS, INGREDIENT, FLUID, DURATION, CONSUME_FLUID_CHANCE)
            .constructor(RESULTS, INGREDIENT, FLUID);

    RecipeKey<FluidStack> RESULT_FLUID = FluidStackComponent.FLUID_STACK.outputKey("result");
    RecipeKey<List<SizedIngredient>> CATALYSTS = SizedIngredientComponent.SIZED_INGREDIENT.asList().inputKey("catalysts");

    RecipeSchema FLUID_TO_FLUID = new RecipeSchema(RESULT_FLUID, FLUID, CATALYSTS)
            .constructor(RESULT_FLUID, FLUID, CATALYSTS);

    RecipeSchema BURNING = new RecipeSchema(RESULTS, INGREDIENT)
            .constructor(RESULTS, INGREDIENT);

    RecipeSchema ITEM_EXPLOSION = new RecipeSchema(RESULTS, INGREDIENT)
            .constructor(RESULTS, INGREDIENT);

    RecipeKey<String> BLOCK = StringComponent.STRING.otherKey("block");
    RecipeKey<Boolean> REPLACE_DROPS = BooleanComponent.BOOLEAN.otherKey("replace_drops").optional(true)
            .alwaysWrite().functionNames("replaceDrops");

    RecipeSchema BLOCK_EXPLOSION = new RecipeSchema(RESULTS, BLOCK, REPLACE_DROPS)
            .constructor(RESULTS, BLOCK);
}
