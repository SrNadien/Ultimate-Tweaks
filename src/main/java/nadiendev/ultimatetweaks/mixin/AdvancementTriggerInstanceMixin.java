package nadiendev.ultimatetweaks.mixin;

import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(InventoryChangeTrigger.TriggerInstance.class)
public abstract class AdvancementTriggerInstanceMixin {

    @Shadow
    public abstract InventoryChangeTrigger.TriggerInstance.Slots slots();

    @Shadow
    public abstract List<ItemPredicate> items();

    @Inject(method = "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void ultimatetweaks$singlePass(Inventory inventory, ItemStack changed, int full, int empty, int occupied,
                                           CallbackInfoReturnable<Boolean> cir) {
        List<ItemPredicate> required = items();
        if (!TweaksCommonConfig.advancementsEnabled() || !TweaksCommonConfig.advancementsFastMatch() || required.size() < 2) {
            return;
        }
        if (!slots().matches(full, empty, occupied)) {
            cir.setReturnValue(false);
            return;
        }
        boolean[] found = new boolean[required.size()];
        int missing = required.size();
        int size = inventory.getContainerSize();
        for (int slot = 0; slot < size && missing > 0; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            for (int index = 0; index < found.length; index++) {
                if (!found[index] && required.get(index).test(stack)) {
                    found[index] = true;
                    missing--;
                }
            }
        }
        cir.setReturnValue(missing == 0);
    }
}
