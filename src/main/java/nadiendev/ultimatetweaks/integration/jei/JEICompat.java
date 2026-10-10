package nadiendev.ultimatetweaks.integration.jei;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "jei_plugin");
    }
}
