package nadiendev.ultimatetweaks.client.ftbchunks;

import com.mojang.blaze3d.platform.NativeImage;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MinimapUploadCache {

    private record Upload(WeakReference<NativeImage> image, int version, int skipX, int skipY, int width, int height,
                          boolean blur) {
    }

    private static final Map<Long, Upload> UPLOADED = new HashMap<>();
    private static int textureId = -1;

    private MinimapUploadCache() {
    }

    public static boolean isUpToDate(int minimapTextureId, NativeImage image, int destX, int destY, int skipX,
                                     int skipY, int width, int height, boolean blur) {
        if (minimapTextureId != textureId) {
            UPLOADED.clear();
            textureId = minimapTextureId;
            return false;
        }
        Upload last = UPLOADED.get(key(destX, destY));
        return last != null && last.image.get() == image && last.version == ((ImageVersion) (Object) image).ultimatetweaks$version()
                && last.skipX == skipX && last.skipY == skipY && last.width == width && last.height == height
                && last.blur == blur;
    }

    public static void uploaded(int minimapTextureId, NativeImage image, int destX, int destY, int skipX, int skipY,
                                int width, int height, boolean blur) {
        if (minimapTextureId != textureId) {
            UPLOADED.clear();
            textureId = minimapTextureId;
        }
        Iterator<Map.Entry<Long, Upload>> iterator = UPLOADED.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Upload> entry = iterator.next();
            int otherX = (int) (entry.getKey() >> 32);
            int otherY = (int) (long) entry.getKey();
            Upload other = entry.getValue();
            boolean overlaps = otherX < destX + width && destX < otherX + other.width
                    && otherY < destY + height && destY < otherY + other.height;
            if (overlaps) {
                iterator.remove();
            }
        }
        UPLOADED.put(key(destX, destY), new Upload(new WeakReference<>(image), ((ImageVersion) (Object) image).ultimatetweaks$version(),
                skipX, skipY, width, height, blur));
    }

    public static void clear() {
        UPLOADED.clear();
        textureId = -1;
    }

    private static long key(int destX, int destY) {
        return ((long) destX << 32) | (destY & 0xFFFFFFFFL);
    }
}
