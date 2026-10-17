package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class TransformationCodecs {

    public static final Codec<List<ItemStack>> RESULTS = ItemStack.CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> RESULTS_STREAM =
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list());

    private TransformationCodecs() {
    }
}
