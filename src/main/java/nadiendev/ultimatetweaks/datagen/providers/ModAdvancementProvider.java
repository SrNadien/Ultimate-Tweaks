package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.registry.ItemsAdds;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.minecraft.data.advancements.AdvancementSubProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {

        AdvancementHolder installMod = Advancement.Builder.advancement()
                .display(
                        ItemsAdds.GALACTIC_STAR.get(),
                        Component.translatable("advancements.ultimatetweaks.install_mod.title"),
                        Component.translatable("advancements.ultimatetweaks.install_mod.description"),
                        Identifier.parse("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "install_mod").toString());

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
                .save(saver, Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "eresunviciado").toString());

    }
}
