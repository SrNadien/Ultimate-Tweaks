package nadiendev.ultimatetweaks.trash;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public final class TrashCanMenu extends ChestMenu {

    private static final int ROWS = 4;

    public TrashCanMenu(int containerId, Inventory inventory) {
        super(MenuType.GENERIC_9x4, containerId, inventory, new SimpleContainer(ROWS * 9), ROWS);
    }

    public void clearTrash() {
        getContainer().clearContent();
        broadcastChanges();
    }
}
