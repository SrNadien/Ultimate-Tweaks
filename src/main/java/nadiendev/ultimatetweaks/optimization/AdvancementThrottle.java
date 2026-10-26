package nadiendev.ultimatetweaks.optimization;

import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class AdvancementThrottle {

    private static final int MAX_PENDING = 32;

    private static final class State {
        private long lastRun = Long.MIN_VALUE / 2;
        private final List<ItemStack> pending = new ArrayList<>();
    }

    private static final Map<UUID, State> STATES = new HashMap<>();
    private static boolean replaying;

    private AdvancementThrottle() {
    }

    public static boolean shouldDefer(ServerPlayer player, ItemStack changed) {
        if (replaying || !TweaksCommonConfig.advancementsEnabled()) {
            return false;
        }
        int interval = TweaksCommonConfig.advancementIntervalTicks();
        if (interval <= 0) {
            return false;
        }
        long now = player.level().getServer().getTickCount();
        State state = STATES.computeIfAbsent(player.getUUID(), id -> new State());
        if (state.pending.isEmpty() && now - state.lastRun >= interval) {
            state.lastRun = now;
            return false;
        }
        remember(state, changed);
        return true;
    }

    private static void remember(State state, ItemStack changed) {
        for (int i = 0; i < state.pending.size(); i++) {
            if (ItemStack.isSameItem(state.pending.get(i), changed)) {
                state.pending.set(i, changed.copy());
                return;
            }
        }
        if (state.pending.size() < MAX_PENDING) {
            state.pending.add(changed.copy());
        }
    }

    public static void flush(ServerPlayer player) {
        State state = STATES.get(player.getUUID());
        if (state == null || state.pending.isEmpty()) {
            return;
        }
        long now = player.level().getServer().getTickCount();
        if (now - state.lastRun < TweaksCommonConfig.advancementIntervalTicks()) {
            return;
        }
        state.lastRun = now;
        List<ItemStack> stacks = new ArrayList<>(state.pending);
        state.pending.clear();
        replaying = true;
        try {
            for (ItemStack stack : stacks) {
                CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.getInventory(), stack);
            }
        } finally {
            replaying = false;
        }
    }

    public static void forget(UUID player) {
        STATES.remove(player);
    }
}
