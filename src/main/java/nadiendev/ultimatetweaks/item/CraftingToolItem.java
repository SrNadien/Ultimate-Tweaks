package nadiendev.ultimatetweaks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public class CraftingToolItem extends Item {

    public CraftingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        if (!(instance instanceof ItemStack stack)) {
            return null;
        }
        int damage = stack.getDamageValue() + 1;
        if (damage >= stack.getMaxDamage()) {
            return null;
        }
        ItemStack remaining = stack.copy();
        remaining.setCount(1);
        remaining.setDamageValue(damage);
        return ItemStackTemplate.fromNonEmptyStack(remaining);
    }
}
