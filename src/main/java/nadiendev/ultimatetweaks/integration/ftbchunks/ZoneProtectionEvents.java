package nadiendev.ultimatetweaks.integration.ftbchunks;

import com.mojang.brigadier.ParseResults;
import dev.ftb.mods.ftbteams.api.Team;
import nadiendev.ultimatetweaks.zone.ZoneFlagDef;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.util.TriState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.PistonEvent;

import java.util.List;
import java.util.Locale;

public final class ZoneProtectionEvents {

    @SubscribeEvent
    public void onMount(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityMounting() instanceof ServerPlayer player
                && ZoneLookup.deniesPlayer(player, event.getEntityBeingMounted().blockPosition(), ZoneFlagDef.RIDE)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onSleep(CanPlayerSleepEvent event) {
        if (ZoneLookup.deniesPlayer(event.getEntity(), event.getPos(), ZoneFlagDef.SLEEP)) {
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        boolean denied = false;
        if (!event.getItemStack().isEmpty()
                && (event.getItemStack().is(Items.FLINT_AND_STEEL) || event.getItemStack().is(Items.FIRE_CHARGE))) {
            denied = ZoneLookup.deniesPlayer(player, pos.relative(event.getFace()), ZoneFlagDef.LIGHTER);
        }
        if (!denied && state.is(BlockTags.ANVIL)) {
            denied = ZoneLookup.deniesPlayer(player, pos, ZoneFlagDef.USE_ANVIL);
        }
        if (!denied && isUsable(state)) {
            denied = ZoneLookup.deniesPlayer(player, pos, ZoneFlagDef.USE);
        }
        if (!denied && state.getBlock() instanceof RespawnAnchorBlock) {
            denied = ZoneLookup.deniesPlayer(player, pos, ZoneFlagDef.RESPAWN_ANCHORS);
        }
        if (!denied && (event.getItemStack().getItem() instanceof BoatItem
                || event.getItemStack().getItem() instanceof MinecartItem)) {
            denied = ZoneLookup.deniesPlayer(player, pos.relative(event.getFace()), ZoneFlagDef.VEHICLE_PLACE);
        }
        if (!denied && event.getLevel().getBlockEntity(pos) instanceof MenuProvider) {
            denied = ZoneLookup.deniesPlayer(player, pos, ZoneFlagDef.CHEST_ACCESS);
        } else if (!denied && !isUsable(state) && !(state.getBlock() instanceof RespawnAnchorBlock)
                && !(event.getItemStack().getItem() instanceof BlockItem)) {
            denied = ZoneLookup.deniesPlayer(player, pos, ZoneFlagDef.INTERACT);
        }
        if (denied) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    private static boolean isUsable(BlockState state) {
        return state.getBlock() instanceof DoorBlock || state.getBlock() instanceof TrapDoorBlock
                || state.getBlock() instanceof FenceGateBlock || state.getBlock() instanceof ButtonBlock
                || state.getBlock() instanceof LeverBlock;
    }

    @SubscribeEvent
    public void onVehicleItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player
                && (event.getItemStack().getItem() instanceof BoatItem || event.getItemStack().getItem() instanceof MinecartItem)
                && ZoneLookup.deniesPlayer(player, player.blockPosition(), ZoneFlagDef.VEHICLE_PLACE)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof VehicleEntity vehicle
                && ZoneLookup.deniesPlayer(player, vehicle.blockPosition(), ZoneFlagDef.VEHICLE_DESTROY)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player
                && ZoneLookup.deniesPlayer(player, event.getPos(), ZoneFlagDef.BLOCK_BREAK)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player
                && ZoneLookup.deniesPlayer(player, event.getPos(), ZoneFlagDef.BLOCK_PLACE)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof ItemFrame frame
                && !frame.getItem().isEmpty()
                && ZoneLookup.deniesPlayer(player, frame.blockPosition(), ZoneFlagDef.ITEM_FRAME_ROTATION)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        } else if (event.getEntity() instanceof ServerPlayer other && !(event.getTarget() instanceof ItemFrame)
                && ZoneLookup.deniesPlayer(other, event.getTarget().blockPosition(), ZoneFlagDef.INTERACT)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public void onTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (ZoneLookup.denies(event.getLevel(), event.getPos(), ZoneFlagDef.BLOCK_TRAMPLING)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPiston(PistonEvent.Pre event) {
        if (ZoneLookup.denies(event.getLevel(), event.getPos(), ZoneFlagDef.PISTONS)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPotionImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownPotion potion
                && ZoneLookup.denies(potion.level(), event.getRayTraceResult().getLocation(), ZoneFlagDef.POTION_SPLASH)) {
            event.setCanceled(true);
            potion.discard();
        }
    }

    @SubscribeEvent
    public void onEnderPearl(EntityTeleportEvent.EnderPearl event) {
        ServerPlayer player = event.getPlayer();
        if (teleportDenied(player, event.getTarget(), ZoneFlagDef.ENDERPEARL)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onChorusFruit(EntityTeleportEvent.ChorusFruit event) {
        if (event.getEntityLiving() instanceof ServerPlayer player
                && teleportDenied(player, event.getTarget(), ZoneFlagDef.CHORUS_FRUIT_TELEPORT)) {
            event.setCanceled(true);
        }
    }

    private static boolean teleportDenied(ServerPlayer player, Vec3 target, ZoneFlagDef def) {
        return ZoneLookup.deniesPlayer(player, BlockPos.containing(target), def)
                || ZoneLookup.deniesPlayer(player, player.blockPosition(), def);
    }

    @SubscribeEvent
    public void onPickup(ItemEntityPickupEvent.Pre event) {
        if (event.getPlayer() instanceof ServerPlayer player
                && ZoneLookup.deniesPlayerSilent(player, event.getItemEntity().blockPosition(), ZoneFlagDef.ITEM_PICKUP)) {
            event.setCanPickup(TriState.FALSE);
        }
    }

    @SubscribeEvent
    public void onToss(ItemTossEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player
                && ZoneLookup.deniesPlayer(player, player.blockPosition(), ZoneFlagDef.ITEM_DROP)) {
            event.setCanceled(true);
            player.getInventory().add(event.getEntity().getItem());
        }
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        if (ZoneLookup.deniesPlayer(event.getPlayer(), ZoneFlagDef.SEND_CHAT)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        ParseResults<CommandSourceStack> parse = event.getParseResults();
        if (!(parse.getContext().getSource().getEntity() instanceof ServerPlayer player)
                || ZoneLookup.bypasses(player)) {
            return;
        }
        Team team = ZoneLookup.zoneAt(player);
        if (team == null) {
            return;
        }
        String line = parse.getReader().getString().trim();
        if (line.startsWith("/")) {
            line = line.substring(1);
        }
        String root = line.split(" ", 2)[0].toLowerCase(Locale.ROOT);
        List<String> allowed = ZoneFlags.list(team, ZoneFlagDef.ALLOWED_CMDS);
        List<String> blocked = ZoneFlags.list(team, ZoneFlagDef.BLOCKED_CMDS);
        boolean denied = !allowed.isEmpty() && allowed.stream().noneMatch(c -> matches(c, root))
                || blocked.stream().anyMatch(c -> matches(c, root));
        if (denied) {
            event.setCanceled(true);
            ZoneLookup.notifyDenied(player, team);
        }
    }

    private static boolean matches(String configured, String root) {
        String command = configured.trim().toLowerCase(Locale.ROOT);
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        return command.equals(root);
    }
}
