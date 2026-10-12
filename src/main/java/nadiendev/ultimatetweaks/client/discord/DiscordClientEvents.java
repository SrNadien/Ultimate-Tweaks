package nadiendev.ultimatetweaks.client.discord;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID, value = Dist.CLIENT)
public final class DiscordClientEvents {

    private DiscordClientEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(DiscordPresence::start);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        DiscordPresence.onClientTick();
    }
}
