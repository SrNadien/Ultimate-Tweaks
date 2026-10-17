package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ultimatetweaks.transformation.Transformations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

public class ItemInFluidRecipe extends TransformationRecipe {

    public static final MapCodec<ItemInFluidRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(ItemInFluidRecipe::ingredient),
            FluidIngredient.CODEC.fieldOf("fluid").forGetter(ItemInFluidRecipe::fluid),
            TransformationCodecs.RESULTS.fieldOf("results").forGetter(ItemInFluidRecipe::results),
            Codec.INT.optionalFieldOf("duration", 20).forGetter(ItemInFluidRecipe::duration),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("consume_fluid_chance", 0.0F)
                    .forGetter(ItemInFluidRecipe::consumeFluidChance)
    ).apply(instance, ItemInFluidRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInFluidRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC, ItemInFluidRecipe::ingredient,
            FluidIngredient.STREAM_CODEC, ItemInFluidRecipe::fluid,
            TransformationCodecs.RESULTS_STREAM, ItemInFluidRecipe::results,
            ByteBufCodecs.VAR_INT, ItemInFluidRecipe::duration,
            ByteBufCodecs.FLOAT, ItemInFluidRecipe::consumeFluidChance,
            ItemInFluidRecipe::new);

    private final SizedIngredient ingredient;
    private final FluidIngredient fluid;
    private final List<ItemStack> results;
    private final int duration;
    private final float consumeFluidChance;

    public ItemInFluidRecipe(SizedIngredient ingredient, FluidIngredient fluid, List<ItemStack> results, int duration,
                             float consumeFluidChance) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.results = List.copyOf(results);
        this.duration = Math.max(1, duration);
        this.consumeFluidChance = consumeFluidChance;
    }

    public SizedIngredient ingredient() {
        return ingredient;
    }

    public FluidIngredient fluid() {
        return fluid;
    }

    public List<ItemStack> results() {
        return results;
    }

    public int duration() {
        return duration;
    }

    public float consumeFluidChance() {
        return consumeFluidChance;
    }

    public boolean matches(ItemStack stack, FluidState state) {
        return ingredient.test(stack) && fluid.test(new FluidStack(state.getType(), FluidType.BUCKET_VOLUME));
    }

    @Override
    public RecipeSerializer<ItemInFluidRecipe> getSerializer() {
        return Transformations.ITEM_IN_FLUID_SERIALIZER.get();
    }

    @Override
    public RecipeType<ItemInFluidRecipe> getType() {
        return Transformations.ITEM_IN_FLUID.get();
    }
}
