package nadiendev.ultimatetweaks.registry;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SonidosReproducibles {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, UltimateTweaksCore.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_MEAL_EAT = registerSound("cajitafeli");

    public static final DeferredHolder<SoundEvent, SoundEvent> CAJITA_FELIZ_MCCACO = registerSound("cajitafelizmccaco");

    public static final DeferredHolder<SoundEvent, SoundEvent> GALAXY_SOUND = registerSound("galaxysound");

    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHT_RED_AND_GREEN = registerSound("luzverdeluzrojamakdonald");

    public static final DeferredHolder<SoundEvent, SoundEvent> AVENGERS = registerSound("the_avengers_theme_song");

    public static final ResourceKey<JukeboxSong> DISC_MCCACAO_KEY = createSong("cajitafeli");
    public static final ResourceKey<JukeboxSong> DISC_AVENGERS_KEY = createSong("the_avengers_theme_song");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, name));
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
