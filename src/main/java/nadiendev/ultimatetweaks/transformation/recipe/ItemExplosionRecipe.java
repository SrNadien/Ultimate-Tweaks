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

public class ItemExplosionRecipe extends TransformationRecipe {

    public static final MapCodec<ItemExplosionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(ItemExplosionRecipe::ingredient),
            TransformationCodecs.RESULTS.fieldOf("results").forGetter(ItemExplosionRecipe::results)
    ).apply(instance, ItemExplosionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemExplosionRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC, ItemExplosionRecipe::ingredient,
            TransformationCodecs.RESULTS_STREAM, ItemExplosionRecipe::results,
            ItemExplosionRecipe::new);

    private final SizedIngredient ingredient;
    private final List<ItemStack> results;

    public ItemExplosionRecipe(SizedIngredient ingredient, List<ItemStack> results) {
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
    public RecipeSerializer<ItemExplosionRecipe> getSerializer() {
        return Transformations.ITEM_EXPLOSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<ItemExplosionRecipe> getType() {
        return Transformations.ITEM_EXPLOSION.get();
    }
}
