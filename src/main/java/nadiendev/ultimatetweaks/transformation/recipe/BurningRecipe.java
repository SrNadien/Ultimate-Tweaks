package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ultimatetweaks.transformation.Transformations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public class BurningRecipe extends TransformationRecipe {

    public static final MapCodec<BurningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(BurningRecipe::ingredient),
            TransformationCodecs.RESULTS.fieldOf("results").forGetter(BurningRecipe::results)
    ).apply(instance, BurningRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BurningRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC, BurningRecipe::ingredient,
            TransformationCodecs.RESULTS_STREAM, BurningRecipe::results,
            BurningRecipe::new);

    private final SizedIngredient ingredient;
    private final List<ItemStack> results;

    public BurningRecipe(SizedIngredient ingredient, List<ItemStack> results) {
        this.ingredient = ingredient;
        this.results = List.copyOf(results);
    }

    public SizedIngredient ingredient() {
        return ingredient;
    }

    public List<ItemStack> results() {
        return results;
    }

    public boolean matches(ItemStack stack) {
        return ingredient.test(stack);
    }

    @Override
    public RecipeSerializer<BurningRecipe> getSerializer() {
        return Transformations.BURNING_SERIALIZER.get();
    }

    @Override
    public RecipeType<BurningRecipe> getType() {
        return Transformations.BURNING.get();
    }
}
