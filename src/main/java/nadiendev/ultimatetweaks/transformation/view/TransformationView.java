package nadiendev.ultimatetweaks.transformation.view;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.transformation.Transformations;
import nadiendev.ultimatetweaks.transformation.recipe.BlockExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.BurningRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.FluidToFluidRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemInFluidRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record TransformationView(TransformationKind kind, ResourceLocation id, List<List<ItemStack>> itemInputs,
                                 List<List<FluidStack>> fluidInputs, List<ItemStack> itemOutputs,
                                 List<FluidStack> fluidOutputs, List<Component> notes) {

    public static final int WIDTH = 150;
    public static final int HEIGHT = 58;
    public static final int SLOT = 18;
    public static final int COLUMNS = 3;
    public static final int INPUT_X = 4;
    public static final int OUTPUT_X = 92;
    public static final int SLOTS_Y = 4;
    public static final int ARROW_X = 62;
    public static final int ARROW_Y = 12;
    public static final int NOTES_Y = 42;
    public static final int NOTE_HEIGHT = 9;
    private static final int TAG_BLOCK_LIMIT = 12;

    public static int slotX(int base, int index) {
        return base + (index % COLUMNS) * SLOT;
    }

    public static int slotY(int index) {
        return SLOTS_Y + (index / COLUMNS) * SLOT;
    }

    public static List<TransformationView> of(TransformationKind kind, RecipeManager manager) {
        if (!Transformations.enabled()) {
            return List.of();
        }

        List<TransformationView> views = new ArrayList<>();
        switch (kind) {
            case ITEM_IN_FLUID -> {
                for (RecipeHolder<ItemInFluidRecipe> holder : manager.getAllRecipesFor(Transformations.ITEM_IN_FLUID.get())) {
                    views.add(itemInFluid(holder.id(), holder.value()));
                }
            }
            case FLUID_TO_FLUID -> {
                for (RecipeHolder<FluidToFluidRecipe> holder : manager.getAllRecipesFor(Transformations.FLUID_TO_FLUID.get())) {
                    views.add(fluidToFluid(holder.id(), holder.value()));
                }
            }
            case BURNING -> {
                for (RecipeHolder<BurningRecipe> holder : manager.getAllRecipesFor(Transformations.BURNING.get())) {
                    views.add(itemToItems(kind, holder.id(), holder.value().ingredient(), holder.value().results(),
                            note("burning")));
                }
            }
            case ITEM_EXPLOSION -> {
                for (RecipeHolder<ItemExplosionRecipe> holder : manager.getAllRecipesFor(Transformations.ITEM_EXPLOSION.get())) {
                    views.add(itemToItems(kind, holder.id(), holder.value().ingredient(), holder.value().results(),
                            note("item_explosion")));
                }
            }
            case BLOCK_EXPLOSION -> {
                for (RecipeHolder<BlockExplosionRecipe> holder : manager.getAllRecipesFor(Transformations.BLOCK_EXPLOSION.get())) {
                    views.add(blockExplosion(holder.id(), holder.value()));
                }
            }
        }
        return views;
    }

    private static TransformationView itemInFluid(ResourceLocation id, ItemInFluidRecipe recipe) {
        List<Component> notes = new ArrayList<>();
        notes.add(Component.translatable(noteKey("duration"), String.format("%.1f", recipe.duration() / 20.0F)));
        if (recipe.consumeFluidChance() > 0.0F) {
            notes.add(Component.translatable(noteKey("consume_chance"), Math.round(recipe.consumeFluidChance() * 100)));
        }
        return new TransformationView(TransformationKind.ITEM_IN_FLUID, id, List.of(stacks(recipe.ingredient())),
                List.of(fluidStacks(recipe.fluid())), recipe.results(), List.of(), notes);
    }

    private static TransformationView fluidToFluid(ResourceLocation id, FluidToFluidRecipe recipe) {
        List<List<ItemStack>> catalysts = recipe.catalysts().stream().map(TransformationView::stacks).toList();
        return new TransformationView(TransformationKind.FLUID_TO_FLUID, id, catalysts,
                List.of(fluidStacks(recipe.fluid())), List.of(), List.of(recipe.result()), List.of(note("fluid_to_fluid")));
    }

    private static TransformationView itemToItems(TransformationKind kind, ResourceLocation id, SizedIngredient ingredient,
                                                  List<ItemStack> results, Component note) {
        return new TransformationView(kind, id, List.of(stacks(ingredient)), List.of(), results, List.of(), List.of(note));
    }

    private static TransformationView blockExplosion(ResourceLocation id, BlockExplosionRecipe recipe) {
        List<ItemStack> blocks = new ArrayList<>();
        if (recipe.single() != null) {
            blocks.add(new ItemStack(recipe.single()));
        } else if (recipe.tag() != null) {
            BuiltInRegistries.BLOCK.getTag(recipe.tag()).ifPresent(tag -> tag.stream().limit(TAG_BLOCK_LIMIT)
                    .map(holder -> new ItemStack(holder.value())).forEach(blocks::add));
        }
        blocks.removeIf(ItemStack::isEmpty);

        List<Component> notes = new ArrayList<>();
        notes.add(note("block_explosion"));
        if (recipe.replaceDrops()) {
            notes.add(note("replace_drops"));
        }
        return new TransformationView(TransformationKind.BLOCK_EXPLOSION, id, blocks.isEmpty() ? List.of() : List.of(blocks),
                List.of(), recipe.results(), List.of(), notes);
    }

    private static List<ItemStack> stacks(SizedIngredient ingredient) {
        return Arrays.stream(ingredient.getItems()).filter(stack -> !stack.isEmpty()).toList();
    }

    private static List<FluidStack> fluidStacks(FluidIngredient ingredient) {
        return Arrays.stream(ingredient.getStacks()).filter(stack -> !stack.isEmpty())
                .map(stack -> stack.copyWithAmount(FluidType.BUCKET_VOLUME)).toList();
    }

    private static String noteKey(String name) {
        return "view." + UltimateTweaksCore.MOD_ID + ".transformation." + name;
    }

    private static Component note(String name) {
        return Component.translatable(noteKey(name));
    }
}
