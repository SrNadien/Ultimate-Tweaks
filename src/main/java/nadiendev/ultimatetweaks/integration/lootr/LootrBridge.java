package nadiendev.ultimatetweaks.integration.lootr;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.reflect.Method;

public final class LootrBridge {

    private static final String TICKER = "noobanidus.mods.lootr.common.block.entity.BlockEntityTicker";

    private static Method isValid;
    private static Method add;
    private static boolean resolved;

    private LootrBridge() {
    }

    private static void resolve() {
        if (resolved) {
            return;
        }
        resolved = true;
        try {
            Class<?> ticker = Class.forName(TICKER);
            isValid = ticker.getMethod("isValidEntityFull", BlockEntity.class);
            add = ticker.getMethod("addEntity", BlockEntity.class, Level.class, ChunkPos.class);
        } catch (ReflectiveOperationException e) {
            UltimateTweaksCore.LOGGER.warn("Lootr: no se encontro el aviso de conversion, solo se usaran las reglas por estructura: {}",
                    e.toString());
        }
    }

    public static boolean isAlreadyLootr(BlockEntity entity) {
        return "lootr".equals(BuiltInRegistries.BLOCK.getKey(entity.getBlockState().getBlock()).getNamespace());
    }

    public static boolean queue(BlockEntity entity, Level level, ChunkPos pos) {
        resolve();
        if (isValid == null || add == null) {
            return false;
        }
        try {
            if (!(Boolean) isValid.invoke(null, entity)) {
                return false;
            }
            add.invoke(null, entity, level, pos);
            return true;
        } catch (ReflectiveOperationException e) {
            UltimateTweaksCore.LOGGER.warn("Lootr: no se pudo avisar de un contenedor: {}", e.toString());
            return false;
        }
    }
}
