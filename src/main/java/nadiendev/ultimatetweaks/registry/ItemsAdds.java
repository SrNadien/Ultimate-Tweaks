package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateTweaksCore.MOD_ID);

    public static final DeferredHolder<Item, Item> GALACTIC_STAR = ITEMS.register(
        "galactic_star",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PATRICIO = ITEMS.register(
        "patricio",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BOB_ESPONJA = ITEMS.register(
        "bob_esponja",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SEMILLA = ITEMS.register(
        "semilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_FUEL = ITEMS.register(
        "radioactive_fuel",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PORTALMINI = ITEMS.register(
        "portalmini",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> RADIOACTIVE_ANTIMATTER = ITEMS.register(
        "radioactive_antimatter",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> PLANOS = ITEMS.register(
        "planos",
       () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> REACTORMINI = ITEMS.register(
        "reactormini",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> SUPER_SEED = ITEMS.register(
        "super_seed",
       () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GAMEBOY = ITEMS.register(
         "gameboy",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> GALACTIC_GLOBE = ITEMS.register(
         "galactic_globe",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DRAGONSOUL = ITEMS.register(
         "dragonsoul",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> DOLL = ITEMS.register(
         "doll",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CATALIZADOR = ITEMS.register(
         "catalizador",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> CALAVERA = ITEMS.register(
         "calavera",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> AGUJERONEGRO = ITEMS.register(
         "agujeronegro",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> ABSOLUTE_REACTION_PLATING = ITEMS.register(
         "absolute_reaction_plating",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> BRUJULAMALDITA = ITEMS.register(
         "brujulamaldita",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> UNIVERSE_SEARCH = ITEMS.register(
         "universe_search",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

    public static final DeferredHolder<Item, Item> TRANSMISOR = ITEMS.register(
         "transmisor",
          () -> new Item(new Item.Properties()
                .stacksTo(64)
                .fireResistant()
                .rarity(Rarity.COMMON)
        )
    );

public static final DeferredHolder<Item, Item> DISC_MCCACAO = ITEMS.register(
    "disc_mccacao",
    () -> new Item(new Item.Properties()
            .jukeboxPlayable(SonidosReproducibles.DISC_MCCACAO_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

public static final DeferredHolder<Item, Item> DISC_AVENGERS = ITEMS.register(
    "disc_avengers",
    () -> new Item(new Item.Properties()
            .jukeboxPlayable(SonidosReproducibles.DISC_AVENGERS_KEY)
            .stacksTo(1)
            .rarity(Rarity.RARE))
);

    public static DeferredHolder<Item, Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
