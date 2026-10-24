package nadiendev.ultimatetweaks.mixin;

import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl", remap = false)
public abstract class FtbTeamsClientManagerMixin {

    @Redirect(method = "initSelfDetails",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/client/User;getProfileId()Ljava/util/UUID;"),
              remap = false, require = 0)
    private UUID ultimatetweaks$serverProfileId(User user) {
        if (TweaksClientConfig.FTB_TEAMS_OFFLINE_FIX.get() && Minecraft.getInstance().player != null) {
            return Minecraft.getInstance().player.getUUID();
        }
        return user.getProfileId();
    }
}
