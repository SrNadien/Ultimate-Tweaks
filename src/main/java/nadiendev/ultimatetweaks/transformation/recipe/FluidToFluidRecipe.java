package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ultimatetweaks.transformation.Transformations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

public class FluidToFluidRecipe extends TransformationRecipe {

    public static final MapCodec<FluidToFluidRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidToFluidRecipe::fluid),
            SizedIngredient.NESTED_CODEC.listOf().fieldOf("catalysts").forGetter(FluidToFluidRecipe::catalysts),
            FluidStack.CODEC.fieldOf("result").forGetter(FluidToFluidRecipe::result)
    ).apply(instance, FluidToFluidRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidToFluidRecipe> STREAM_CODEC = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC, FluidToFluidRecipe::fluid,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), FluidToFluidRecipe::catalysts,
            FluidStack.STREAM_CODEC, FluidToFluidRecipe::result,
            FluidToFluidRecipe::new);

    private final FluidIngredient fluid;
    private final List<SizedIngredient> catalysts;
    private final FluidStack result;

    public FluidToFluidRecipe(FluidIngredient fluid, List<SizedIngredient> catalysts, FluidStack result) {
        this.fluid = fluid;
        this.catalysts = List.copyOf(catalysts);
        this.result = result;
    }

    public FluidIngredient fluid() {
        return fluid;
    }

    public List<SizedIngredient> catalysts() {
        return catalysts;
    }

    public FluidStack result() {
        return result;
    }

    public boolean matchesFluid(FluidState state) {
        return state.isSource() && fluid.test(new FluidStack(state.getType(), FluidType.BUCKET_VOLUME));
    }

    @Override
    public RecipeSerializer<FluidToFluidRecipe> getSerializer() {
        return Transformations.FLUID_TO_FLUID_SERIALIZER.get();
    }

    @Override
    public RecipeType<FluidToFluidRecipe> getType() {
        return Transformations.FLUID_TO_FLUID.get();
    }
}
