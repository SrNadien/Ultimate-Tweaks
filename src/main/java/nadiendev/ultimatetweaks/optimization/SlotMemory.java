package nadiendev.ultimatetweaks.optimization;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SlotMemory {

    private static final int SLOTS = 64;

    private static final class Slots {
        private final Item[] items = new Item[SLOTS];
        private final int[] counts = new int[SLOTS];
    }

    private static final Map<UUID, Slots> PLAYERS = new HashMap<>();

    private SlotMemory() {
    }

    public static boolean unchangedThenRemember(UUID player, int slot, ItemStack stack) {
        if (slot < 0 || slot >= SLOTS) {
            return false;
        }
        Slots slots = PLAYERS.computeIfAbsent(player, id -> new Slots());
        boolean same = slots.items[slot] == stack.getItem() && slots.counts[slot] == stack.getCount();
        slots.items[slot] = stack.getItem();
        slots.counts[slot] = stack.getCount();
        return same;
    }

    public static void forget(UUID player) {
        PLAYERS.remove(player);
    }
}
