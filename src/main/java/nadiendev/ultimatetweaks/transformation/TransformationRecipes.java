package nadiendev.ultimatetweaks.transformation;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;
import java.util.List;

public final class TransformationRecipes {

    private TransformationRecipes() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeHolder<T>> of(Collection<RecipeHolder<?>> holders, RecipeType<T> type) {
        return holders.stream()
                .filter(holder -> holder.value().getType() == type)
                .map(holder -> (RecipeHolder<T>) holder)
                .toList();
    }
}
