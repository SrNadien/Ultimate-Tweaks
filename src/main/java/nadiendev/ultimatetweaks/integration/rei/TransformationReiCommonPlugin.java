package nadiendev.ultimatetweaks.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;
import nadiendev.ultimatetweaks.UltimateTweaks;
import net.minecraft.resources.Identifier;

@REIPluginCommon
public class TransformationReiCommonPlugin implements REICommonPlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(Identifier.fromNamespaceAndPath(UltimateTweaks.MOD_ID, "transformation"),
                TransformationReiDisplay.SERIALIZER);
    }
}
