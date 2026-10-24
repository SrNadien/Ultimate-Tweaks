package nadiendev.ultimatetweaks.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import nadiendev.ultimatetweaks.client.ftbchunks.MinimapUploadCache;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbchunks.client.FTBChunksClient", remap = false)
public abstract class FtbChunksMinimapUploadMixin {

    @Shadow
    private int minimapTextureId;

    @Redirect(method = "updateMinimapIfNeeded",
              at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;upload(IIIIIIIZZZZ)V"),
              remap = false, require = 0)
    private void ultimatetweaks$skipUnchangedUpload(NativeImage image, int level, int destX, int destY, int skipX,
                                                    int skipY, int width, int height, boolean blur, boolean clamp,
                                                    boolean mipmap, boolean autoClose) {
        if (!TweaksClientConfig.FTB_CHUNKS_MINIMAP_BOOST.get()) {
            image.upload(level, destX, destY, skipX, skipY, width, height, blur, clamp, mipmap, autoClose);
            return;
        }
        if (MinimapUploadCache.isUpToDate(minimapTextureId, image, destX, destY, skipX, skipY, width, height, blur)) {
            return;
        }
        image.upload(level, destX, destY, skipX, skipY, width, height, blur, clamp, mipmap, autoClose);
        MinimapUploadCache.uploaded(minimapTextureId, image, destX, destY, skipX, skipY, width, height, blur);
    }
}
