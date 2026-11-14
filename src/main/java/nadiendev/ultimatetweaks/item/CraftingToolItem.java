package nadiendev.ultimatetweaks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingToolItem extends Item {

    public CraftingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        int damage = stack.getDamageValue() + 1;
        if (damage >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        ItemStack remaining = stack.copy();
        remaining.setCount(1);
        remaining.setDamageValue(damage);
        return remaining;
    }
}
