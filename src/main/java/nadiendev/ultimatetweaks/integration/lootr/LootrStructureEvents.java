package nadiendev.ultimatetweaks.integration.lootr;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;
import noobanidus.mods.lootr.common.block.entity.BlockEntityTicker;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LootrStructureEvents {

    private record Task(long due, ServerLevel level, ChunkPos pos) {
    }

    private final ArrayDeque<Task> queue = new ArrayDeque<>();
    private final Set<String> processed = new HashSet<>();

    private LootrStructureEvents() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(new LootrStructureEvents());
    }

    @SubscribeEvent
    public void onReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new LootrStructureRules());
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!TweaksCommonConfig.lootrEnabled() || !(event.getLevel() instanceof ServerLevel level)
                || !(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }
        if (chunk.getAllReferences().isEmpty() && !TweaksCommonConfig.lootrScanAllChunks()) {
            return;
        }
        queue.add(new Task(level.getServer().getTickCount() + TweaksCommonConfig.lootrDelayTicks(), level, chunk.getPos()));
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        int tick = event.getServer().getTickCount();
        while (!queue.isEmpty() && queue.peek().due() <= tick) {
            Task task = queue.poll();
            try {
                process(task.level(), task.pos());
            } catch (RuntimeException e) {
                UltimateTweaksCore.LOGGER.error("Lootr: error en el chunk {}: {}", task.pos(), e.toString());
            }
        }
    }

    private void process(ServerLevel level, ChunkPos pos) {
        if (!level.getChunkSource().hasChunk(pos.x, pos.z)) {
            return;
        }
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x, pos.z);
        if (chunk == null) {
            return;
        }
        String key = level.dimension().location() + ":" + pos.x + ":" + pos.z;
        if (!processed.add(key)) {
            return;
        }
        if (processed.size() > 50000) {
            processed.clear();
        }
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        List<StructureStart> starts = level.structureManager().startsForStructure(pos, structure -> true);
        List<BlockEntity> entities = new ArrayList<>(chunk.getBlockEntities().values());
        for (BlockEntity entity : entities) {
            if (!(entity instanceof RandomizableContainer container) || entity instanceof ILootrBlockEntity) {
                continue;
            }
            LootrStructureRules.Rule rule = isConvertible(entity) ? ruleAt(registry, starts, entity.getBlockPos()) : null;
            if (rule != null && (rule.replaceItems() || container.getLootTable() == null)) {
                if (container instanceof Clearable clearable) {
                    clearable.clearContent();
                }
                container.setLootTable(rule.lootTable(), level.random.nextLong());
                entity.setChanged();
                report(level, entity, "con la tabla " + rule.lootTable().location() + " (regla " + rule.id() + ")");
            } else if (container.getLootTable() != null && TweaksCommonConfig.lootrForceTabledChests()
                    && BlockEntityTicker.isValidEntityFull(entity)) {
                BlockEntityTicker.addEntity(entity, level, pos);
                report(level, entity, "con su tabla " + container.getLootTable().location());
            }
        }
    }

    private static void report(ServerLevel level, BlockEntity entity, String detail) {
        if (TweaksCommonConfig.lootrLogConversions()) {
            UltimateTweaksCore.LOGGER.info("Lootr: {} en {} ({}) pasa a ser de Lootr {}",
                    BuiltInRegistries.BLOCK.getKey(entity.getBlockState().getBlock()),
                    entity.getBlockPos().toShortString(), level.dimension().location(), detail);
        }
    }

    private static boolean isConvertible(BlockEntity entity) {
        return entity instanceof ChestBlockEntity || entity instanceof BarrelBlockEntity;
    }

    @Nullable
    private static LootrStructureRules.Rule ruleAt(Registry<Structure> registry, List<StructureStart> starts, BlockPos pos) {
        for (StructureStart start : starts) {
            for (StructurePiece piece : start.getPieces()) {
                if (piece.getBoundingBox().isInside(pos)) {
                    LootrStructureRules.Rule rule = LootrStructureRules.find(registry, start.getStructure());
                    if (rule != null) {
                        return rule;
                    }
                    break;
                }
            }
        }
        return null;
    }
}
