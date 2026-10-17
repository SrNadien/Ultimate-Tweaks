package nadiendev.ultimatetweaks.transformation;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.transformation.recipe.BlockExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.BurningRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.FluidToFluidRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemInFluidRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class Transformations {

    public static final String KUBEJS = "kubejs";

    private static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, UltimateTweaksCore.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, UltimateTweaksCore.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemInFluidRecipe>> ITEM_IN_FLUID = type("item_in_fluid");
    public static final DeferredHolder<RecipeType<?>, RecipeType<FluidToFluidRecipe>> FLUID_TO_FLUID = type("fluid_to_fluid");
    public static final DeferredHolder<RecipeType<?>, RecipeType<BurningRecipe>> BURNING = type("burning");
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemExplosionRecipe>> ITEM_EXPLOSION = type("item_explosion");
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlockExplosionRecipe>> BLOCK_EXPLOSION = type("block_explosion");

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ItemInFluidRecipe>> ITEM_IN_FLUID_SERIALIZER =
            SERIALIZERS.register("item_in_fluid",
                    () -> new RecipeSerializer<>(ItemInFluidRecipe.CODEC, ItemInFluidRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidToFluidRecipe>> FLUID_TO_FLUID_SERIALIZER =
            SERIALIZERS.register("fluid_to_fluid",
                    () -> new RecipeSerializer<>(FluidToFluidRecipe.CODEC, FluidToFluidRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BurningRecipe>> BURNING_SERIALIZER =
            SERIALIZERS.register("burning",
                    () -> new RecipeSerializer<>(BurningRecipe.CODEC, BurningRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ItemExplosionRecipe>> ITEM_EXPLOSION_SERIALIZER =
            SERIALIZERS.register("item_explosion",
                    () -> new RecipeSerializer<>(ItemExplosionRecipe.CODEC, ItemExplosionRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlockExplosionRecipe>> BLOCK_EXPLOSION_SERIALIZER =
            SERIALIZERS.register("block_explosion",
                    () -> new RecipeSerializer<>(BlockExplosionRecipe.CODEC, BlockExplosionRecipe.STREAM_CODEC));

    private static boolean enabled;

    private Transformations() {
    }

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> type(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, name);
        return TYPES.register(name, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    public static void register(IEventBus modEventBus) {
        if (!ModList.get().isLoaded(KUBEJS)) {
            return;
        }
        enabled = true;
        TYPES.register(modEventBus);
        SERIALIZERS.register(modEventBus);
    }

    public static boolean enabled() {
        return enabled;
    }
}
