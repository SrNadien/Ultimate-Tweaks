package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateTweaks.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ULTIMATE_TWEAKS_TAB =
            CREATIVE_MODE_TABS.register("ultimate_tweaks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultimatetweaks"))
                    .icon(() -> new ItemStack(ItemsAdds.GALACTIC_STAR.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ItemsAdds.GALACTIC_STAR.get());
                        output.accept(ItemsAdds.PATRICIO.get());
                        output.accept(ItemsAdds.BOB_ESPONJA.get());
                        output.accept(ItemsAdds.SEMILLA.get());
                        output.accept(ItemsAdds.RADIOACTIVE_FUEL.get());
                        output.accept(ItemsAdds.PORTALMINI.get());
                        output.accept(ItemsAdds.RADIOACTIVE_ANTIMATTER.get());
                        output.accept(ItemsAdds.PLANOS.get());
                        output.accept(ItemsAdds.REACTORMINI.get());
                        output.accept(ItemsAdds.SUPER_SEED.get());
                        output.accept(ItemsAdds.GAMEBOY.get());
                        output.accept(ItemsAdds.GALACTIC_GLOBE.get());
                        output.accept(ItemsAdds.DRAGONSOUL.get());
                        output.accept(ItemsAdds.DOLL.get());
                        output.accept(ItemsAdds.CATALIZADOR.get());
                        output.accept(ItemsAdds.CALAVERA.get());
                        output.accept(ItemsAdds.AGUJERONEGRO.get());
                        output.accept(ItemsAdds.ABSOLUTE_REACTION_PLATING.get());
                        output.accept(ItemsAdds.BRUJULAMALDITA.get());
                        output.accept(ItemsAdds.UNIVERSE_SEARCH.get());
                        output.accept(ItemsAdds.TRANSMISOR.get());
                        output.accept(ItemsAdds.DISC_MCCACAO.get());
                        output.accept(ItemsAdds.DISC_AVENGERS.get());
                        output.accept(ItemsAdds.PRINTED_PROGNOSIS_CIRCUIT.get());
                        output.accept(ItemsAdds.PRINTED_PROTOCOL_CIRCUIT.get());
                        output.accept(ItemsAdds.PRINTED_CHRONO_CIRCUIT.get());
                        output.accept(ItemsAdds.PRINTED_AUTHORITY_CIRCUIT.get());
                        output.accept(ItemsAdds.PRINTED_EXECUTION_CIRCUIT.get());
                        output.accept(ItemsAdds.PROGNOSIS_PROCESSOR.get());
                        output.accept(ItemsAdds.PROTOCOL_PROCESSOR.get());
                        output.accept(ItemsAdds.CHRONO_PROCESSOR.get());
                        output.accept(ItemsAdds.AUTHORITY_PROCESSOR.get());
                        output.accept(ItemsAdds.EXECUTION_PROCESSOR.get());
                        output.accept(ItemsAdds.STELLAR_ALLOY.get());
                        output.accept(ItemsAdds.CRYSTAL_CLUSTER.get());
                        output.accept(ItemsAdds.ENERGIZED_CRYSTAL_CLUSTER.get());
                        output.accept(ItemsAdds.AURELIAN_CRYSTAL.get());
                        output.accept(ItemsAdds.ENERGIZED_AURELIAN_CRYSTAL.get());
                        output.accept(ItemsAdds.ZINC_CLAW.get());
                        output.accept(ItemsAdds.BRASS_CLAW.get());
                        output.accept(ItemsAdds.ANDESITE_CLAW.get());
                        for (MaterialItems.Material material : MaterialItems.MATERIALS) {
                            for (MaterialItems.Kind kind : MaterialItems.Kind.values()) {
                                output.accept(MaterialItems.holder(material, kind).get());
                            }
                        }
                    })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
