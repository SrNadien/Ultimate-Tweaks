package nadiendev.ultimatetweaks.transformation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ultimatetweaks.transformation.Transformations;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockExplosionRecipe extends TransformationRecipe {

    public static final MapCodec<BlockExplosionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("block").forGetter(BlockExplosionRecipe::block),
            TransformationCodecs.RESULTS.fieldOf("results").forGetter(BlockExplosionRecipe::results),
            Codec.BOOL.optionalFieldOf("replace_drops", true).forGetter(BlockExplosionRecipe::replaceDrops)
    ).apply(instance, BlockExplosionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockExplosionRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BlockExplosionRecipe::block,
            TransformationCodecs.RESULTS_STREAM, BlockExplosionRecipe::results,
            ByteBufCodecs.BOOL, BlockExplosionRecipe::replaceDrops,
            BlockExplosionRecipe::new);

    private final String block;
    private final List<ItemStack> results;
    private final boolean replaceDrops;
    private final TagKey<Block> tag;
    private final Block single;

    public BlockExplosionRecipe(String block, List<ItemStack> results, boolean replaceDrops) {
        this.block = block;
        this.results = List.copyOf(results);
        this.replaceDrops = replaceDrops;

        boolean isTag = block.startsWith("#");
        Identifier id = Identifier.tryParse(isTag ? block.substring(1) : block);
        this.tag = isTag && id != null ? TagKey.create(Registries.BLOCK, id) : null;
        this.single = !isTag && id != null ? BuiltInRegistries.BLOCK.getOptional(id).orElse(null) : null;
    }

    public String block() {
        return block;
    }

    public List<ItemStack> results() {
        return results;
    }

    public boolean replaceDrops() {
        return replaceDrops;
    }

    public TagKey<Block> tag() {
        return tag;
    }

    public Block single() {
        return single;
    }

    public boolean matches(BlockState state) {
        if (tag != null) {
            return state.is(tag);
        }
        return single != null && state.is(single);
    }

    @Override
    public RecipeSerializer<BlockExplosionRecipe> getSerializer() {
        return Transformations.BLOCK_EXPLOSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<BlockExplosionRecipe> getType() {
        return Transformations.BLOCK_EXPLOSION.get();
    }
}
