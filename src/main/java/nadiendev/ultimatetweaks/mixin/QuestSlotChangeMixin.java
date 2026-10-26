package nadiendev.ultimatetweaks.mixin;

import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.optimization.SlotMemory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener", remap = false)
public abstract class QuestSlotChangeMixin {

    @Final
    @Shadow
    public ServerPlayer player;

    @Inject(method = "slotChanged(Lnet/minecraft/world/inventory/AbstractContainerMenu;ILnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void ultimatetweaks$skipUnchangedSlot(AbstractContainerMenu menu, int index, ItemStack stack, CallbackInfo ci) {
        if (!TweaksCommonConfig.questsIgnoreUnchangedSlots() || stack.isEmpty()) {
            return;
        }
        if (menu.getSlot(index).container != player.getInventory()) {
            return;
        }
        int slot = menu.getSlot(index).getContainerSlot();
        if (slot < 0 || slot >= player.getInventory().items.size()) {
            return;
        }
        if (SlotMemory.unchangedThenRemember(player.getUUID(), slot, stack)) {
            ci.cancel();
        }
    }
}
