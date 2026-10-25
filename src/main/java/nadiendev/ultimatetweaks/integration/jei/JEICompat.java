package nadiendev.ultimatetweaks.integration.jei;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import nadiendev.ultimatetweaks.transformation.Transformations;
import nadiendev.ultimatetweaks.transformation.view.TransformationKind;
import nadiendev.ultimatetweaks.transformation.view.TransformationView;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.EnumMap;
import java.util.Map;

@JeiPlugin
public class JEICompat implements IModPlugin {

    private final Map<TransformationKind, TransformationJeiCategory> categories = new EnumMap<>(TransformationKind.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (!Transformations.enabled()) {
            return;
        }
        for (TransformationKind kind : TransformationKind.values()) {
            TransformationJeiCategory category = new TransformationJeiCategory(registration.getJeiHelpers().getGuiHelper(), kind);
            categories.put(kind, category);
            registration.addRecipeCategories(category);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!Transformations.enabled() || Minecraft.getInstance().level == null) {
            return;
        }
        for (TransformationKind kind : TransformationKind.values()) {
            TransformationJeiCategory category = categories.get(kind);
            if (category != null) {
                registration.addRecipes(category.type(),
                        TransformationView.of(kind, Minecraft.getInstance().level.getRecipeManager()));
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (!Transformations.enabled()) {
            return;
        }
        for (TransformationKind kind : TransformationKind.values()) {
            TransformationJeiCategory category = categories.get(kind);
            if (category == null) {
                continue;
            }
            for (ItemStack stack : kind.catalystItems()) {
                registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, stack, category.type());
            }
            for (Fluid fluid : kind.catalystFluids()) {
                registration.addRecipeCatalyst(NeoForgeTypes.FLUID_STACK, new FluidStack(fluid, FluidType.BUCKET_VOLUME),
                        category.type());
            }
        }
    }
}
