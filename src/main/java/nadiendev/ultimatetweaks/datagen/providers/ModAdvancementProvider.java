package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.registry.ItemsAdds;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {

        AdvancementHolder installMod = Advancement.Builder.advancement()
                .display(
                        ItemsAdds.GALACTIC_STAR.get(),
                        Component.translatable("advancements.ultimatetweaks.install_mod.title"),
                        Component.translatable("advancements.ultimatetweaks.install_mod.description"),
                        ResourceLocation.parse("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "install_mod").toString());

        Advancement.Builder.advancement()
                .parent(installMod)
                .display(
                        ItemsAdds.GALACTIC_STAR.get(),
                        Component.translatable("advancements.ultimatetweaks.eresunviciado.title"),
                        Component.translatable("advancements.ultimatetweaks.eresunviciado.description"),
                        null, AdvancementType.CHALLENGE, true, true, false
                )
                .addCriterion("has_galactic_star",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsAdds.GALACTIC_STAR.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "eresunviciado").toString());

    }
}
