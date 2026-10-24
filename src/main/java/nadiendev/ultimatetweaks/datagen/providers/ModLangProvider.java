package nadiendev.ultimatetweaks.datagen.providers;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    private final boolean spanish;

    public ModLangProvider(PackOutput output, String locale) {
        super(output, UltimateTweaksCore.MOD_ID, locale);
        this.spanish = locale.startsWith("es_");
    }

    @Override
    protected void addTranslations() {
        if (spanish) {
            spanish();
        } else {
            english();
        }
    }

    private void english() {
        zoneFlagTranslations(false);
        add("ftbteamsconfig.ultimatetweaks", "Zone Flags");
        add("itemGroup.ultimatetweaks", "Ultimate Tweaks");
        add("container.ultimatetweaks.trash_can", "Trash Can");
        add("container.ultimatetweaks.trash_can.clear", "Clear");
        add("category.ultimatetweaks.transformation.item_in_fluid", "Item in fluid");
        add("category.ultimatetweaks.transformation.fluid_to_fluid", "Fluid transformation");
        add("category.ultimatetweaks.transformation.burning", "Burning");
        add("category.ultimatetweaks.transformation.item_explosion", "Item explosion");
        add("category.ultimatetweaks.transformation.block_explosion", "Block explosion");
        add("view.ultimatetweaks.transformation.duration", "Takes %s s");
        add("view.ultimatetweaks.transformation.consume_chance", "%s%% chance to use up the fluid");
        add("view.ultimatetweaks.transformation.fluid_to_fluid", "Drop the catalysts into the fluid");
        add("view.ultimatetweaks.transformation.burning", "Drop it into fire or lava");
        add("view.ultimatetweaks.transformation.item_explosion", "Blow it up (TNT, creeper...)");
        add("view.ultimatetweaks.transformation.block_explosion", "Blow the block up");
        add("view.ultimatetweaks.transformation.replace_drops", "Replaces its normal drops");
        add("sidebar_button.ultimatetweaks.trash_can", "Trash Can");
        add("item.ultimatetweaks.galactic_star", "Galactic Star");
        add("item.ultimatetweaks.patricio", "Patrick");
        add("item.ultimatetweaks.bob_esponja", "SpongeBob");
        add("item.ultimatetweaks.semilla", "Seed");
        add("item.ultimatetweaks.radioactive_fuel", "Radioactive Fuel");
        add("item.ultimatetweaks.portalmini", "Mini Portal");
        add("item.ultimatetweaks.radioactive_antimatter", "Radioactive Antimatter");
        add("item.ultimatetweaks.planos", "Blueprints");
        add("item.ultimatetweaks.reactormini", "Mini Reactor");
        add("item.ultimatetweaks.super_seed", "Super Seed");
        add("item.ultimatetweaks.gameboy", "Game Boy");
        add("item.ultimatetweaks.galactic_globe", "Galactic Globe");
        add("item.ultimatetweaks.dragonsoul", "Dragon Soul");
        add("item.ultimatetweaks.doll", "Doll");
        add("item.ultimatetweaks.catalizador", "Catalyst");
        add("item.ultimatetweaks.calavera", "Skull");
        add("item.ultimatetweaks.agujeronegro", "Black Hole");
        add("item.ultimatetweaks.absolute_reaction_plating", "Absolute Reaction Plating");
        add("item.ultimatetweaks.brujulamaldita", "Cursed Compass");
        add("item.ultimatetweaks.universe_search", "Universe Finder");
        add("item.ultimatetweaks.transmisor", "Transmitter");
        add("item.ultimatetweaks.disc_mccacao", "Music Disc - Happy Meal");
        add("item.ultimatetweaks.disc_avengers", "Music Disc - Avengers");
        add("sound_event.ultimatetweaks.cajitafeli", "Happy Meal Sound");
        add("sound_event.ultimatetweaks.cajitafelizmccaco", "Happy Meal Sound");
        add("sound_event.ultimatetweaks.galaxysound", "Galactic Sound");
        add("sound_event.ultimatetweaks.luzverdeluzrojamakdonald", "Red Light Green Light");
        add("sound_event.ultimatetweaks.the_avengers_theme_song", "Avengers Theme");
        add("jukebox_song.ultimatetweaks.disc_mccacao", "McCaco - Happy Meal");
        add("jukebox_song.ultimatetweaks.disc_avengers", "Avengers Theme");
        add("advancements.ultimatetweaks.install_mod.title", "You Installed the Mod");
        add("advancements.ultimatetweaks.install_mod.description", "Welcome to Ultimate Tweaks. It's too late to turn back now.");
        add("advancements.ultimatetweaks.eresunviciado.title", "You're Addicted");
        add("advancements.ultimatetweaks.eresunviciado.description", "You got the Galactic Star. You have too much free time.");
    }

    private void spanish() {
        zoneFlagTranslations(true);
        add("ftbteamsconfig.ultimatetweaks", "Flags de zonas");
        add("itemGroup.ultimatetweaks", "Ultimate Tweaks");
        add("container.ultimatetweaks.trash_can", "Papelera");
        add("container.ultimatetweaks.trash_can.clear", "Vaciar");
        add("category.ultimatetweaks.transformation.item_in_fluid", "\u00cdtem en fluido");
        add("category.ultimatetweaks.transformation.fluid_to_fluid", "Transformaci\u00f3n de fluido");
        add("category.ultimatetweaks.transformation.burning", "Quemado");
        add("category.ultimatetweaks.transformation.item_explosion", "Explosi\u00f3n de \u00edtem");
        add("category.ultimatetweaks.transformation.block_explosion", "Explosi\u00f3n de bloque");
        add("view.ultimatetweaks.transformation.duration", "Tarda %s s");
        add("view.ultimatetweaks.transformation.consume_chance", "%s%% de gastar el fluido");
        add("view.ultimatetweaks.transformation.fluid_to_fluid", "Tira los catalizadores al fluido");
        add("view.ultimatetweaks.transformation.burning", "T\u00edralo al fuego o a la lava");
        add("view.ultimatetweaks.transformation.item_explosion", "Hazlo explotar (TNT, creeper...)");
        add("view.ultimatetweaks.transformation.block_explosion", "Haz explotar el bloque");
        add("view.ultimatetweaks.transformation.replace_drops", "Reemplaza sus drops normales");
        add("sidebar_button.ultimatetweaks.trash_can", "Papelera");
        add("item.ultimatetweaks.galactic_star", "Estrella Gal\u00e1ctica");
        add("item.ultimatetweaks.patricio", "Patricio");
        add("item.ultimatetweaks.bob_esponja", "Bob Esponja");
        add("item.ultimatetweaks.semilla", "Semilla");
        add("item.ultimatetweaks.radioactive_fuel", "Combustible Radiactivo");
        add("item.ultimatetweaks.portalmini", "Portal Mini");
        add("item.ultimatetweaks.radioactive_antimatter", "Antimateria Radiactiva");
        add("item.ultimatetweaks.planos", "Planos");
        add("item.ultimatetweaks.reactormini", "Reactor Mini");
        add("item.ultimatetweaks.super_seed", "Super Semilla");
        add("item.ultimatetweaks.gameboy", "Gameboy");
        add("item.ultimatetweaks.galactic_globe", "Globo Gal\u00e1ctico");
        add("item.ultimatetweaks.dragonsoul", "Alma de Drag\u00f3n");
        add("item.ultimatetweaks.doll", "Mu\u00f1eca");
        add("item.ultimatetweaks.catalizador", "Catalizador");
        add("item.ultimatetweaks.calavera", "Calavera");
        add("item.ultimatetweaks.agujeronegro", "Agujero Negro");
        add("item.ultimatetweaks.absolute_reaction_plating", "Placa de Reacci\u00f3n Absoluta");
        add("item.ultimatetweaks.brujulamaldita", "Br\u00fajula Maldita");
        add("item.ultimatetweaks.universe_search", "Buscador del Universo");
        add("item.ultimatetweaks.transmisor", "Transmisor");
        add("item.ultimatetweaks.disc_mccacao", "Disco de M\u00fasica - La cajita Feliz");
        add("item.ultimatetweaks.disc_avengers", "Disco de M\u00fasica - Vengadores");
        add("sound_event.ultimatetweaks.cajitafeli", "Sonido de Cajita Feliz");
        add("sound_event.ultimatetweaks.cajitafelizmccaco", "Sonido de Cajita Feliz");
        add("sound_event.ultimatetweaks.galaxysound", "Sonido Gal\u00e1ctico");
        add("sound_event.ultimatetweaks.luzverdeluzrojamakdonald", "Luz Roja Luz Verde");
        add("sound_event.ultimatetweaks.the_avengers_theme_song", "Tema de Los Vengadores");
        add("jukebox_song.ultimatetweaks.disc_mccacao", "McCaco - Cajita Feliz");
        add("jukebox_song.ultimatetweaks.disc_avengers", "Tema de Los Vengadores");
        add("advancements.ultimatetweaks.install_mod.title", "Instalaste El Mod");
        add("advancements.ultimatetweaks.install_mod.description", "Bienvenido a Ultimate Tweaks. Ya es tarde para arrepentirse.");
        add("advancements.ultimatetweaks.eresunviciado.title", "Eres Un Viciado");
        add("advancements.ultimatetweaks.eresunviciado.description", "Conseguiste la Galactic Star. Tienes demasiado tiempo libre.");
    }

    private void zoneFlagTranslations(boolean spanishText) {
        for (ZoneFlagDef def : ZoneFlagDef.values()) {
            add(def.langKey(), def.name(spanishText));
            add(def.langKey() + ".tooltip", def.tooltip(spanishText));
        }
    }
}
