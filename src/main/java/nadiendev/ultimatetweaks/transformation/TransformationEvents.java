package nadiendev.ultimatetweaks.transformation;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.transformation.recipe.BlockExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.BurningRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.FluidToFluidRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemInFluidRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID)
public final class TransformationEvents {

    private static final int FLUID_INTERVAL = 5;
    private static final String RECIPE_KEY = UltimateTweaksCore.MOD_ID + "_transformation_recipe";
    private static final String TICKS_KEY = UltimateTweaksCore.MOD_ID + "_transformation_ticks";

    private TransformationEvents() {
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!Transformations.enabled() || !(event.getEntity() instanceof ItemEntity item)
                || !(item.level() instanceof ServerLevel level) || item.isRemoved()) {
            return;
        }

        if ((item.isOnFire() || item.isInLava()) && burn(item, level)) {
            return;
        }
        if (item.tickCount % FLUID_INTERVAL == 0) {
            inFluid(item, level);
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (!Transformations.enabled() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        explodeBlocks(level, event.getAffectedBlocks());
        explodeItems(level, event.getAffectedEntities());
    }

    private static boolean burn(ItemEntity item, ServerLevel level) {
        for (RecipeHolder<BurningRecipe> holder : level.getRecipeManager().getAllRecipesFor(Transformations.BURNING.get())) {
            BurningRecipe recipe = holder.value();
            if (recipe.matches(item.getItem())) {
                craftFromItem(item, level, recipe.ingredient().count(), recipe.results(), true);
                return true;
            }
        }
        return false;
    }

    private static void inFluid(ItemEntity item, ServerLevel level) {
        BlockPos pos = item.blockPosition();
        FluidState fluid = level.getFluidState(pos);
        if (fluid.isEmpty()) {
            clearProgress(item);
            return;
        }

        RecipeManager manager = level.getRecipeManager();
        for (RecipeHolder<ItemInFluidRecipe> holder : manager.getAllRecipesFor(Transformations.ITEM_IN_FLUID.get())) {
            ItemInFluidRecipe recipe = holder.value();
            if (recipe.matches(item.getItem(), fluid)) {
                if (advance(item, holder.id().toString(), recipe.duration())) {
                    craftInFluid(item, level, pos, fluid, recipe);
                }
                return;
            }
        }

        if (!fluid.isSource() || !fluidToFluid(item, level, pos, fluid)) {
            clearProgress(item);
        }
    }

    private static boolean advance(ItemEntity item, String recipeId, int duration) {
        CompoundTag data = item.getPersistentData();
        int ticks = recipeId.equals(data.getString(RECIPE_KEY)) ? data.getInt(TICKS_KEY) + FLUID_INTERVAL : FLUID_INTERVAL;
        if (ticks >= duration) {
            clearProgress(item);
            return true;
        }
        data.putString(RECIPE_KEY, recipeId);
        data.putInt(TICKS_KEY, ticks);
        return false;
    }

    private static void clearProgress(ItemEntity item) {
        CompoundTag data = item.getPersistentData();
        if (data.contains(RECIPE_KEY)) {
            data.remove(RECIPE_KEY);
            data.remove(TICKS_KEY);
        }
    }

    private static void craftInFluid(ItemEntity item, ServerLevel level, BlockPos pos, FluidState fluid,
                                     ItemInFluidRecipe recipe) {
        ItemStack stack = item.getItem();
        int perCraft = Math.max(1, recipe.ingredient().count());
        int possible = stack.getCount() / perCraft;
        int done = 0;

        while (done < possible) {
            done++;
            if (fluid.isSource() && recipe.consumeFluidChance() > 0.0F
                    && level.random.nextFloat() < recipe.consumeFluidChance()) {
                level.removeBlock(pos, false);
                break;
            }
        }

        finishCraft(item, level, done, perCraft, recipe.results(), false);
    }

    private static boolean fluidToFluid(ItemEntity item, ServerLevel level, BlockPos pos, FluidState fluid) {
        for (RecipeHolder<FluidToFluidRecipe> holder : level.getRecipeManager().getAllRecipesFor(Transformations.FLUID_TO_FLUID.get())) {
            FluidToFluidRecipe recipe = holder.value();
            if (!recipe.matchesFluid(fluid)) {
                continue;
            }

            Map<ItemEntity, Integer> used = collectCatalysts(level, pos, recipe.catalysts());
            if (used == null) {
                continue;
            }

            used.forEach((entity, amount) -> {
                ItemStack stack = entity.getItem();
                stack.shrink(amount);
                if (stack.isEmpty()) {
                    entity.discard();
                } else {
                    entity.setItem(stack);
                }
            });
            level.setBlock(pos, recipe.result().getFluid().defaultFluidState().createLegacyBlock(), 3);
            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.8F, 1.0F);
            return true;
        }
        return false;
    }

    private static Map<ItemEntity, Integer> collectCatalysts(ServerLevel level, BlockPos pos, List<SizedIngredient> catalysts) {
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));
        Map<ItemEntity, Integer> used = new IdentityHashMap<>();

        for (SizedIngredient catalyst : catalysts) {
            int needed = catalyst.count();
            for (ItemEntity entity : items) {
                ItemStack stack = entity.getItem();
                int available = stack.getCount() - used.getOrDefault(entity, 0);
                if (available > 0 && catalyst.ingredient().test(stack)) {
                    int taken = Math.min(available, needed);
                    used.merge(entity, taken, Integer::sum);
                    needed -= taken;
                    if (needed == 0) {
                        break;
                    }
                }
            }
            if (needed > 0) {
                return null;
            }
        }
        return used.isEmpty() ? null : used;
    }

    private static void explodeItems(ServerLevel level, List<Entity> affected) {
        List<RecipeHolder<ItemExplosionRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(Transformations.ITEM_EXPLOSION.get());
        if (recipes.isEmpty()) {
            return;
        }

        Iterator<Entity> iterator = affected.iterator();
        while (iterator.hasNext()) {
            if (!(iterator.next() instanceof ItemEntity item)) {
                continue;
            }
            for (RecipeHolder<ItemExplosionRecipe> holder : recipes) {
                ItemExplosionRecipe recipe = holder.value();
                if (recipe.matches(item.getItem())) {
                    craftFromItem(item, level, recipe.ingredient().count(), recipe.results(), false);
                    if (item.isRemoved()) {
                        iterator.remove();
                    }
                    break;
                }
            }
        }
    }

    private static void explodeBlocks(ServerLevel level, List<BlockPos> affected) {
        List<RecipeHolder<BlockExplosionRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(Transformations.BLOCK_EXPLOSION.get());
        if (recipes.isEmpty()) {
            return;
        }

        Iterator<BlockPos> iterator = affected.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.AIR)) {
                continue;
            }
            for (RecipeHolder<BlockExplosionRecipe> holder : recipes) {
                BlockExplosionRecipe recipe = holder.value();
                if (recipe.matches(state)) {
                    spawn(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, recipe.results(), 1, false);
                    if (recipe.replaceDrops()) {
                        level.removeBlock(pos, false);
                        iterator.remove();
                    }
                    break;
                }
            }
        }
    }

    private static void craftFromItem(ItemEntity item, ServerLevel level, int perCraft, List<ItemStack> results,
                                      boolean fireproof) {
        int per = Math.max(1, perCraft);
        finishCraft(item, level, item.getItem().getCount() / per, per, results, fireproof);
    }

    private static void finishCraft(ItemEntity item, ServerLevel level, int crafts, int perCraft, List<ItemStack> results,
                                    boolean fireproof) {
        if (crafts <= 0) {
            return;
        }
        ItemStack stack = item.getItem();
        stack.shrink(crafts * perCraft);
        spawn(level, item.getX(), item.getY(), item.getZ(), results, crafts, fireproof);
        if (stack.isEmpty()) {
            item.discard();
        } else {
            item.setItem(stack);
        }
    }

    private static void spawn(Level level, double x, double y, double z, List<ItemStack> results, int crafts,
                              boolean fireproof) {
        for (ItemStack result : results) {
            int remaining = result.getCount() * crafts;
            while (remaining > 0) {
                int size = Math.min(remaining, result.getMaxStackSize());
                ItemEntity spawned = new ItemEntity(level, x, y, z, result.copyWithCount(size));
                spawned.setInvulnerable(fireproof);
                level.addFreshEntity(spawned);
                remaining -= size;
            }
        }
    }
}
