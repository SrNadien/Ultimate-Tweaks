package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateTweaksCore.MOD_ID);

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
                    })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
