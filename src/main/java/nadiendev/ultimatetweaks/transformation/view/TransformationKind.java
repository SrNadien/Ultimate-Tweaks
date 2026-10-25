package nadiendev.ultimatetweaks.transformation.view;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public enum TransformationKind {
    ITEM_IN_FLUID("item_in_fluid", Items.WATER_BUCKET),
    FLUID_TO_FLUID("fluid_to_fluid", Items.LAVA_BUCKET),
    BURNING("burning", Items.FLINT_AND_STEEL),
    ITEM_EXPLOSION("item_explosion", Items.GUNPOWDER),
    BLOCK_EXPLOSION("block_explosion", Items.TNT);

    private final String path;
    private final ItemStack icon;

    TransformationKind(String path, Item icon) {
        this.path = path;
        this.icon = new ItemStack(icon);
    }

    public String path() {
        return path;
    }

    public ItemStack icon() {
        return icon.copy();
    }

    public ResourceLocation id() {
        return ResourceLocation.fromNamespaceAndPath(UltimateTweaksCore.MOD_ID, path);
    }

    public List<ItemStack> catalystItems() {
        return switch (this) {
            case BURNING -> List.of(new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.FLINT_AND_STEEL),
                    new ItemStack(Items.FIRE_CHARGE));
            case ITEM_EXPLOSION, BLOCK_EXPLOSION -> List.of(new ItemStack(Items.TNT));
            default -> List.of();
        };
    }

    public List<Fluid> catalystFluids() {
        return this == BURNING ? List.of(Fluids.LAVA) : List.of();
    }

    public Component title() {
        return Component.translatable("category." + UltimateTweaksCore.MOD_ID + ".transformation." + path);
    }
}
