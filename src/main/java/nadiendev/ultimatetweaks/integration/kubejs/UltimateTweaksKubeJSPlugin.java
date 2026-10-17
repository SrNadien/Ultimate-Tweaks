package nadiendev.ultimatetweaks.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeMappingRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.resources.Identifier;

public class UltimateTweaksKubeJSPlugin implements KubeJSPlugin {

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, path);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        RecipeNamespace namespace = registry.namespace(UltimateTweaksCore.MOD_ID);
        namespace.register("item_in_fluid", TransformationSchemas.ITEM_IN_FLUID);
        namespace.register("fluid_to_fluid", TransformationSchemas.FLUID_TO_FLUID);
        namespace.register("burning", TransformationSchemas.BURNING);
        namespace.register("item_explosion", TransformationSchemas.ITEM_EXPLOSION);
        namespace.register("block_explosion", TransformationSchemas.BLOCK_EXPLOSION);
    }

    @Override
    public void registerRecipeMappings(RecipeMappingRegistry registry) {
        registry.register("transformationItemInFluid", id("item_in_fluid"));
        registry.register("transformationFluidToFluid", id("fluid_to_fluid"));
        registry.register("transformationBurning", id("burning"));
        registry.register("transformationItemExplosion", id("item_explosion"));
        registry.register("transformationBlockExplosion", id("block_explosion"));
    }
}
