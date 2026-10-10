package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UltimateTweaksCore.MOD_ID);

    public static final DeferredHolder<Item, Item> GALACTIC_STAR = reg(
        "galactic_star",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PATRICIO = reg(
        "patricio",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BOB_ESPONJA = reg(
        "bob_esponja",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SEMILLA = reg(
        "semilla",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_FUEL = reg(
        "radioactive_fuel",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PORTALMINI = reg(
        "portalmini",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_ANTIMATTER = reg(
        "radioactive_antimatter",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PLANOS = reg(
        "planos",
       () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> REACTORMINI = reg(
        "reactormini",
        () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SUPER_SEED = reg(
        "super_seed",
       () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GAMEBOY = reg(
         "gameboy",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GALACTIC_GLOBE = reg(
         "galactic_globe",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DRAGONSOUL = reg(
         "dragonsoul",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DOLL = reg(
         "doll",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CATALIZADOR = reg(
         "catalizador",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CALAVERA = reg(
         "calavera",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> AGUJERONEGRO = reg(
         "agujeronegro",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> ABSOLUTE_REACTION_PLATING = reg(
         "absolute_reaction_plating",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BRUJULAMALDITA = reg(
         "brujulamaldita",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> UNIVERSE_SEARCH = reg(
         "universe_search",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> TRANSMISOR = reg(
         "transmisor",
          () -> new Item(props()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DISC_MCCACAO = reg(
        "disc_mccacao",
        () -> new Item(props()
                .jukeboxPlayable(SonidosReproducibles.DISC_MCCACAO_KEY)
                .stacksTo(1)
                .rarity(Rarity.RARE))
    );

    public static final DeferredHolder<Item, Item> DISC_AVENGERS = reg(
        "disc_avengers",
        () -> new Item(props()
                .jukeboxPlayable(SonidosReproducibles.DISC_AVENGERS_KEY)
                .stacksTo(1)
                .rarity(Rarity.RARE))
    );

    private static final ThreadLocal<Item.Properties> CURRENT_PROPERTIES = new ThreadLocal<>();

    private static Item.Properties props() {
        Item.Properties properties = CURRENT_PROPERTIES.get();
        return properties != null ? properties : new Item.Properties();
    }

    private static <T extends Item> DeferredHolder<Item, T> reg(String name, Supplier<T> supplier) {
        return ITEMS.registerItem(name, properties -> {
            CURRENT_PROPERTIES.set(properties);
            try {
                return supplier.get();
            } finally {
                CURRENT_PROPERTIES.remove();
            }
        });
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
