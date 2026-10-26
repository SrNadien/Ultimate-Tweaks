package nadiendev.ultimatetweaks.mixin;

import dev.ftb.mods.ftbquests.integration.item_filtering.ItemMatchingSystem;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.optimization.ItemListEpoch;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbquests.quest.task.ItemTask", remap = false)
public abstract class QuestItemTaskMixin {

    @Shadow
    private ItemStack itemStack;

    @Shadow
    private ItemMatchingSystem.ComponentMatchType matchComponents;

    @Unique
    private List<ItemStack> ultimatetweaks$validItems;

    @Unique
    private int ultimatetweaks$validSignature;

    @Unique
    private int ultimatetweaks$validEpoch = -1;

    @Inject(method = "getValidDisplayItems", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void ultimatetweaks$cachedValidItems(CallbackInfoReturnable<List<ItemStack>> cir) {
        if (!TweaksCommonConfig.questsCacheItems()) {
            return;
        }
        if (ultimatetweaks$validItems != null && ultimatetweaks$validSignature == ultimatetweaks$signature()
                && ultimatetweaks$validEpoch == ItemListEpoch.current()) {
            cir.setReturnValue(ultimatetweaks$validItems);
        }
    }

    @Inject(method = "getValidDisplayItems", at = @At("RETURN"), remap = false, require = 0)
    private void ultimatetweaks$rememberValidItems(CallbackInfoReturnable<List<ItemStack>> cir) {
        if (!TweaksCommonConfig.questsCacheItems() || cir.getReturnValue() == null) {
            return;
        }
        ultimatetweaks$validItems = List.copyOf(cir.getReturnValue());
        ultimatetweaks$validSignature = ultimatetweaks$signature();
        ultimatetweaks$validEpoch = ItemListEpoch.current();
    }

    @Unique
    private int ultimatetweaks$signature() {
        int hash = System.identityHashCode(itemStack.getItem());
        hash = 31 * hash + itemStack.getComponents().hashCode();
        hash = 31 * hash + (matchComponents == null ? -1 : matchComponents.ordinal());
        return hash;
    }
}
