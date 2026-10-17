package nadiendev.ultimatetweaks.transformation.view;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.transformation.TransformationRecipes;
import nadiendev.ultimatetweaks.transformation.Transformations;
import nadiendev.ultimatetweaks.transformation.recipe.BlockExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.BurningRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.FluidToFluidRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemExplosionRecipe;
import nadiendev.ultimatetweaks.transformation.recipe.ItemInFluidRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record TransformationView(TransformationKind kind, Identifier id, List<List<ItemStack>> itemInputs,
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

    public static List<TransformationView> of(TransformationKind kind, Collection<RecipeHolder<?>> manager) {
        if (!Transformations.enabled()) {
            return List.of();
        }

        List<TransformationView> views = new ArrayList<>();
        switch (kind) {
            case ITEM_IN_FLUID -> {
                for (RecipeHolder<ItemInFluidRecipe> holder : TransformationRecipes.of(manager, Transformations.ITEM_IN_FLUID.get())) {
                    views.add(itemInFluid(holder.id().identifier(), holder.value()));
                }
            }
            case FLUID_TO_FLUID -> {
                for (RecipeHolder<FluidToFluidRecipe> holder : TransformationRecipes.of(manager, Transformations.FLUID_TO_FLUID.get())) {
                    views.add(fluidToFluid(holder.id().identifier(), holder.value()));
                }
            }
            case BURNING -> {
                for (RecipeHolder<BurningRecipe> holder : TransformationRecipes.of(manager, Transformations.BURNING.get())) {
                    views.add(itemToItems(kind, holder.id().identifier(), holder.value().ingredient(), holder.value().results(),
                            note("burning")));
                }
            }
            case ITEM_EXPLOSION -> {
                for (RecipeHolder<ItemExplosionRecipe> holder : TransformationRecipes.of(manager, Transformations.ITEM_EXPLOSION.get())) {
                    views.add(itemToItems(kind, holder.id().identifier(), holder.value().ingredient(), holder.value().results(),
                            note("item_explosion")));
                }
            }
            case BLOCK_EXPLOSION -> {
                for (RecipeHolder<BlockExplosionRecipe> holder : TransformationRecipes.of(manager, Transformations.BLOCK_EXPLOSION.get())) {
                    views.add(blockExplosion(holder.id().identifier(), holder.value()));
                }
            }
        }
        return views;
    }

    private static TransformationView itemInFluid(Identifier id, ItemInFluidRecipe recipe) {
        List<Component> notes = new ArrayList<>();
        notes.add(Component.translatable(noteKey("duration"), String.format("%.1f", recipe.duration() / 20.0F)));
        if (recipe.consumeFluidChance() > 0.0F) {
            notes.add(Component.translatable(noteKey("consume_chance"), Math.round(recipe.consumeFluidChance() * 100)));
        }
        return new TransformationView(TransformationKind.ITEM_IN_FLUID, id, List.of(stacks(recipe.ingredient())),
                List.of(fluidStacks(recipe.fluid())), recipe.results(), List.of(), notes);
    }

    private static TransformationView fluidToFluid(Identifier id, FluidToFluidRecipe recipe) {
        List<List<ItemStack>> catalysts = recipe.catalysts().stream().map(TransformationView::stacks).toList();
        return new TransformationView(TransformationKind.FLUID_TO_FLUID, id, catalysts,
                List.of(fluidStacks(recipe.fluid())), List.of(), List.of(recipe.result()), List.of(note("fluid_to_fluid")));
    }

    private static TransformationView itemToItems(TransformationKind kind, Identifier id, SizedIngredient ingredient,
                                                  List<ItemStack> results, Component note) {
        return new TransformationView(kind, id, List.of(stacks(ingredient)), List.of(), results, List.of(), List.of(note));
    }

    private static TransformationView blockExplosion(Identifier id, BlockExplosionRecipe recipe) {
        List<ItemStack> blocks = new ArrayList<>();
        if (recipe.single() != null) {
            blocks.add(new ItemStack(recipe.single()));
        } else if (recipe.tag() != null) {
            BuiltInRegistries.BLOCK.getTagOrEmpty(recipe.tag()).forEach(holder -> {
                if (blocks.size() < TAG_BLOCK_LIMIT) {
                    blocks.add(new ItemStack(holder.value()));
                }
            });
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
        return ingredient.ingredient().items().map(holder -> new ItemStack(holder, ingredient.count()))
                .filter(stack -> !stack.isEmpty()).toList();
    }

    private static List<FluidStack> fluidStacks(FluidIngredient ingredient) {
        return ingredient.fluids().stream().map(holder -> new FluidStack(holder, FluidType.BUCKET_VOLUME))
                .filter(stack -> !stack.isEmpty()).toList();
    }

    private static String noteKey(String name) {
        return "view." + UltimateTweaksCore.MOD_ID + ".transformation." + name;
    }

    private static Component note(String name) {
        return Component.translatable(noteKey(name));
    }
}
