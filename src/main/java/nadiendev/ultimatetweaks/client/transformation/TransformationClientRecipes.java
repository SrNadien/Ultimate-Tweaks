package nadiendev.ultimatetweaks.client.transformation;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.Collection;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID, value = Dist.CLIENT)
public final class TransformationClientRecipes {

    private static RecipeMap received = RecipeMap.EMPTY;

    private TransformationClientRecipes() {
    }

    public static Collection<RecipeHolder<?>> recipes() {
        return received.values();
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        received = event.getRecipeMap();
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        received = RecipeMap.EMPTY;
    }
}
