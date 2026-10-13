package nadiendev.ultimatetweaks.client.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID, value = Dist.CLIENT)
public final class TrashCanSidebarPack {

    public static final String FTB_LIBRARY = "ftblibrary";
    public static final String FTB_ESSENTIALS = "ftbessentials";

    private static final Identifier PACK =
            Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "resourcepacks/trash_can_sidebar");

    private TrashCanSidebarPack() {
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        ModList mods = ModList.get();
        if (!mods.isLoaded(FTB_LIBRARY) || mods.isLoaded(FTB_ESSENTIALS)) {
            return;
        }
        event.addPackFinders(PACK, PackType.CLIENT_RESOURCES, Component.literal("Ultimate Tweaks Trash Can"),
                PackSource.BUILT_IN, true, Pack.Position.TOP);
    }
}
