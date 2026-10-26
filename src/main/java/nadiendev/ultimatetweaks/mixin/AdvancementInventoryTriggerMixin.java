package nadiendev.ultimatetweaks.mixin;

import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.optimization.AdvancementThrottle;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public abstract class AdvancementInventoryTriggerMixin {

    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void ultimatetweaks$throttle(ServerPlayer player, Inventory inventory, ItemStack changed, CallbackInfo ci) {
        if (TweaksCommonConfig.advancementsEnabled() && TweaksCommonConfig.advancementsIgnoreEmpty() && changed.isEmpty()) {
            ci.cancel();
            return;
        }
        if (AdvancementThrottle.shouldDefer(player, changed)) {
            ci.cancel();
        }
    }
}
