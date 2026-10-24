package nadiendev.ultimatetweaks.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import nadiendev.ultimatetweaks.client.ftbchunks.ImageVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntUnaryOperator;

@Mixin(NativeImage.class)
public abstract class NativeImageVersionMixin implements ImageVersion {

    @Unique
    private int ultimatetweaks$version;

    @Override
    public int ultimatetweaks$version() {
        return ultimatetweaks$version;
    }

    @Inject(method = "setPixelRGBA", at = @At("HEAD"))
    private void ultimatetweaks$setPixel(int x, int y, int color, CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "fillRect", at = @At("HEAD"))
    private void ultimatetweaks$fill(int x, int y, int width, int height, int color, CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void ultimatetweaks$copy(NativeImage source, CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "copyRect(Lcom/mojang/blaze3d/platform/NativeImage;IIIIIIZZ)V", at = @At("HEAD"))
    private void ultimatetweaks$copyRectFrom(NativeImage source, int srcX, int srcY, int dstX, int dstY, int width,
                                             int height, boolean flipX, boolean flipY, CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "copyRect(IIIIIIZZ)V", at = @At("HEAD"))
    private void ultimatetweaks$copyRectSelf(int srcX, int srcY, int dstX, int dstY, int width, int height,
                                             boolean flipX, boolean flipY, CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "flipY", at = @At("HEAD"))
    private void ultimatetweaks$flip(CallbackInfo ci) {
        ultimatetweaks$version++;
    }

    @Inject(method = "applyToAllPixels", at = @At("HEAD"))
    private void ultimatetweaks$applyAll(IntUnaryOperator operator, CallbackInfo ci) {
        ultimatetweaks$version++;
    }
}
