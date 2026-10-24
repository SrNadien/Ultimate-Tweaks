package nadiendev.ultimatetweaks.integration.ftbchunks;

import dev.ftb.mods.ftbteams.api.Team;
import nadiendev.ultimatetweaks.config.TweaksCommonConfig;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.List;

public final class ZoneDamageEvents {

    @SubscribeEvent
    public void onFinalizeSpawn(FinalizeSpawnEvent event) {
        MobSpawnType type = event.getSpawnType();
        Team team = ZoneLookup.zoneAt(event.getLevel().getLevel(),
                BlockPos.containing(event.getX(), event.getY(), event.getZ()));
        if (team == null) {
            return;
        }
        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType()).toString();
        List<String> denied = ZoneFlags.list(team, ZoneFlagDef.DENY_SPAWN);
        if (denied.stream().anyMatch(id -> id.trim().equals(entityId))) {
            event.setSpawnCancelled(true);
            return;
        }
        if (TweaksCommonConfig.zoneBlockedSpawnTypes().contains(type.name())
                && !ZoneFlags.state(team, ZoneFlagDef.MOB_SPAWNING)) {
            event.setSpawnCancelled(true);
        }
    }

    @SubscribeEvent
    public void onDetonate(ExplosionEvent.Detonate event) {
        ZoneFlagDef flag = explosionFlag(event.getExplosion());
        event.getAffectedBlocks().removeIf(pos -> ZoneLookup.denies(event.getLevel(), pos, flag));
        event.getAffectedEntities().removeIf(entity -> ZoneLookup.denies(event.getLevel(), entity.blockPosition(), flag));
    }

    private static ZoneFlagDef explosionFlag(Explosion explosion) {
        Entity source = explosion.getDirectSourceEntity();
        if (source instanceof Creeper) {
            return ZoneFlagDef.CREEPER_EXPLOSION;
        }
        if (source instanceof PrimedTnt) {
            return ZoneFlagDef.TNT;
        }
        if (source instanceof Fireball || source instanceof WitherSkull) {
            return ZoneFlagDef.GHAST_FIREBALL;
        }
        if (source instanceof WitherBoss) {
            return ZoneFlagDef.WITHER_DAMAGE;
        }
        if (source instanceof BreezeWindCharge) {
            return ZoneFlagDef.BREEZE_CHARGE_EXPLOSION;
        }
        if (source instanceof AbstractWindCharge) {
            return ZoneFlagDef.WIND_CHARGE_BURST;
        }
        return ZoneFlagDef.OTHER_EXPLOSION;
    }

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        Team team = ZoneLookup.zoneAt(event.getEntity());
        if (team == null) {
            return;
        }
        if (event.getSource().is(DamageTypes.FIREWORKS) && !ZoneFlags.state(team, ZoneFlagDef.FIREWORK_DAMAGE)) {
            event.setCanceled(true);
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }
        if (event.getSource().getEntity() instanceof Mob && !ZoneFlags.state(team, ZoneFlagDef.MOB_DAMAGE)) {
            event.setCanceled(true);
            return;
        }
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && ZoneFlags.state(team, ZoneFlagDef.INVINCIBLE)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Team team = ZoneLookup.zoneAt(player);
            if (team != null && !ZoneFlags.state(team, ZoneFlagDef.FALL_DAMAGE)) {
                event.setDamageMultiplier(0.0F);
            }
        }
    }

    @SubscribeEvent
    public void onExperience(LivingExperienceDropEvent event) {
        Team team = ZoneLookup.zoneAt(event.getEntity());
        if (team != null && !ZoneFlags.state(team, ZoneFlagDef.EXP_DROPS)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LightningBolt bolt
                && ZoneLookup.denies(event.getLevel(), bolt.blockPosition(), ZoneFlagDef.LIGHTNING)) {
            bolt.setVisualOnly(true);
        }
    }

    @SubscribeEvent
    public void onLightning(EntityStruckByLightningEvent event) {
        if (ZoneLookup.denies(event.getEntity().level(), event.getEntity().blockPosition(), ZoneFlagDef.LIGHTNING)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCropGrow(CropGrowEvent.Pre event) {
        if (ZoneLookup.denies(event.getLevel(), event.getPos(), ZoneFlagDef.CROP_GROWTH)) {
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingIncomingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof ServerPlayer player)) {
            return;
        }
        Entity victim = event.getEntity();
        ZoneFlagDef flag = victim instanceof ServerPlayer ? ZoneFlagDef.PVP
                : victim instanceof Animal ? ZoneFlagDef.DAMAGE_ANIMALS : null;
        if (flag == null || victim == attacker) {
            return;
        }
        if (ZoneLookup.deniesPlayer(player, victim.blockPosition(), flag)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMobGriefing(EntityMobGriefingEvent event) {
        ZoneFlagDef flag = event.getEntity() instanceof EnderMan ? ZoneFlagDef.ENDERMAN_GRIEF
                : event.getEntity() instanceof SnowGolem ? ZoneFlagDef.SNOWMAN_TRAILS
                : event.getEntity() instanceof Ravager ? ZoneFlagDef.RAVAGER_GRIEF : null;
        if (flag != null && ZoneLookup.denies(event.getEntity().level(), event.getEntity().blockPosition(), flag)) {
            event.setCanGrief(false);
        }
    }

    @SubscribeEvent
    public void onDestroyBlock(LivingDestroyBlockEvent event) {
        ZoneFlagDef flag = event.getEntity() instanceof EnderDragon ? ZoneFlagDef.ENDERDRAGON_BLOCK_DAMAGE
                : event.getEntity() instanceof WitherBoss ? ZoneFlagDef.WITHER_DAMAGE : null;
        if (flag != null && ZoneLookup.denies(event.getEntity().level(), event.getPos(), flag)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onInvulnerabilityCheck(EntityInvulnerabilityCheckEvent event) {
        Entity target = event.getEntity();
        ZoneFlagDef flag = target instanceof Painting ? ZoneFlagDef.ENTITY_PAINTING_DESTROY
                : target instanceof ItemFrame ? ZoneFlagDef.ENTITY_ITEM_FRAME_DESTROY : null;
        if (flag != null && !(event.getSource().getEntity() instanceof Player)
                && ZoneLookup.denies(target.level(), target.blockPosition(), flag)) {
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        if (ZoneLookup.denies(event.getLevel(), event.getPos(), ZoneFlagDef.LAVA_HARDEN)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Team team = ZoneLookup.zoneAt(player);
            if (team != null && !ZoneFlags.state(team, ZoneFlagDef.NATURAL_HEALTH_REGEN)) {
                event.setCanceled(true);
            }
        }
    }
}
