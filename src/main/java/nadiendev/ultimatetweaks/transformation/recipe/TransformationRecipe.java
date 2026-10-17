package nadiendev.ultimatetweaks.transformation.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class TransformationRecipe implements Recipe<TransformationInput> {

    @Override
    public boolean matches(TransformationInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(TransformationInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
