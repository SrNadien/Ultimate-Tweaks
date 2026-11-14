package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UltimateTweaks.MOD_ID);

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

    public static final DeferredHolder<Item, Item> PRINTED_PROGNOSIS_CIRCUIT = ITEMS.registerItem("printed_prognosis_circuit", Item::new);
    public static final DeferredHolder<Item, Item> PRINTED_PROTOCOL_CIRCUIT = ITEMS.registerItem("printed_protocol_circuit", Item::new);
    public static final DeferredHolder<Item, Item> PRINTED_CHRONO_CIRCUIT = ITEMS.registerItem("printed_chrono_circuit", Item::new);
    public static final DeferredHolder<Item, Item> PRINTED_AUTHORITY_CIRCUIT = ITEMS.registerItem("printed_authority_circuit", Item::new);
    public static final DeferredHolder<Item, Item> PRINTED_EXECUTION_CIRCUIT = ITEMS.registerItem("printed_execution_circuit", Item::new);

    public static final DeferredHolder<Item, Item> PROGNOSIS_PROCESSOR = ITEMS.registerItem("prognosis_processor", Item::new);
    public static final DeferredHolder<Item, Item> PROTOCOL_PROCESSOR = ITEMS.registerItem("protocol_processor", Item::new);
    public static final DeferredHolder<Item, Item> CHRONO_PROCESSOR = ITEMS.registerItem("chrono_processor", Item::new);
    public static final DeferredHolder<Item, Item> AUTHORITY_PROCESSOR = ITEMS.registerItem("authority_processor", Item::new);
    public static final DeferredHolder<Item, Item> EXECUTION_PROCESSOR = ITEMS.registerItem("execution_processor", Item::new);

    public static final DeferredHolder<Item, Item> STELLAR_ALLOY = ITEMS.registerItem("stellar_alloy", Item::new);
    public static final DeferredHolder<Item, Item> CRYSTAL_CLUSTER = ITEMS.registerItem("crystal_cluster", Item::new);
    public static final DeferredHolder<Item, Item> ENERGIZED_CRYSTAL_CLUSTER = ITEMS.registerItem("energized_crystal_cluster", Item::new);
    public static final DeferredHolder<Item, Item> AURELIAN_CRYSTAL = ITEMS.registerItem("aurelian_crystal", Item::new);
    public static final DeferredHolder<Item, Item> ENERGIZED_AURELIAN_CRYSTAL = ITEMS.registerItem("energized_aurelian_crystal", Item::new);

    public static final DeferredHolder<Item, Item> ZINC_CLAW = ITEMS.registerItem("zinc_claw", Item::new);
    public static final DeferredHolder<Item, Item> BRASS_CLAW = ITEMS.registerItem("brass_claw", Item::new);
    public static final DeferredHolder<Item, Item> ANDESITE_CLAW = ITEMS.registerItem("andesite_claw", Item::new);

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
        MaterialItems.init();
        ITEMS.register(eventBus);
    }
}
