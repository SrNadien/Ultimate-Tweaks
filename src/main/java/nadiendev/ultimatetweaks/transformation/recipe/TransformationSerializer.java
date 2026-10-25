package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public record TransformationSerializer<T extends Recipe<?>>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec)
        implements RecipeSerializer<T> {

    public static final Codec<List<ItemStack>> RESULTS = ItemStack.CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> RESULTS_STREAM =
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list());
}
