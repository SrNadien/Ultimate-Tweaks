package nadiendev.ultimatetweaks.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public final class TrashCan implements MenuProvider {

    public static final String TITLE_KEY = "container." + UltimateTweaksCore.MOD_ID + ".trash_can";

    private TrashCan() {
    }

    public static void open(ServerPlayer player) {
        if (player.isSpectator()) {
            return;
        }
        player.openMenu(new TrashCan());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(TITLE_KEY);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new TrashCanMenu(containerId, inventory);
    }
}
