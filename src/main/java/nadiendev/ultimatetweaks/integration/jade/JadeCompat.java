package nadiendev.ultimatetweaks.integration.jade;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {

    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {

    }

    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "jade_plugin");
    }
}
