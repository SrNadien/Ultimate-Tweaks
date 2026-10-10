package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.registry.SonidosReproducibles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateTweaksCore.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        addSound(SonidosReproducibles.HAPPY_MEAL_EAT, "cajitafeli");
        addSound(SonidosReproducibles.CAJITA_FELIZ_MCCACO, "cajitafelizmccaco");
        addSound(SonidosReproducibles.GALAXY_SOUND, "galaxysound");
        addSound(SonidosReproducibles.LIGHT_RED_AND_GREEN, "luzverdeluzrojamakdonald");
        addSound(SonidosReproducibles.AVENGERS, "the_avengers_theme_song");
    }

    private void addSound(net.neoforged.neoforge.registries.DeferredHolder<SoundEvent, SoundEvent> soundEvent, String soundName) {
        add(soundEvent, SoundDefinition.definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, soundName)))
                .subtitle("sound_event.ultimatetweaks." + soundName)
        );
    }

    private void addSoundWithPitch(net.neoforged.neoforge.registries.DeferredHolder<SoundEvent, SoundEvent> soundEvent, String soundName, float pitch) {
        add(soundEvent, SoundDefinition.definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, soundName))
                        .pitch(pitch))
                .subtitle("sound_event.ultimatetweaks." + soundName)
        );
    }
}
