package com.dillo.main.macro.main;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.RandomisationUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.random.ThreadUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.stream.Collectors;

import static com.dillo.calls.CurrentState.ROUTEOBSTRUCTEDCLEAR;
import static com.dillo.calls.CurrentState.SPINDRIVE;
import static com.dillo.config.config.ping;
import static com.dillo.main.macro.main.NewSpinDrive.*;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.isThrowRod;
import static com.dillo.main.utils.keybinds.AllKeybinds.JUMP;
import static com.dillo.utils.BlockUtils.getBlocksLayer;
import static com.dillo.utils.BlockUtils.getNextBlock;
import static com.dillo.utils.RayTracingUtils.adjustLook;

public class StateDillo {

  public static float playerYBe4 = 0;
  public static boolean canCheckIfOnDillo = false;
  public static int tickDilloCheckCount = 0;
  public static boolean isNoTp = false;
  public static int checkedNumber = 0;
  public static boolean isSmartTP = false;
  private static boolean look = true;
  private static float isDilloSummonedTickCount = 0;
  boolean looking = false;

  public static void stateDilloNoGettingOn() {
    if (canDillo() && ArmadilloStates.isOnline()) {
      ArmadilloStates.currentState = null;
      NewSpinDrive.putAllTogether();
      SwapToSlot.swapToSlot(GetSBItems.getDrillSlot());
      ArmadilloStates.currentState = SPINDRIVE;
    } else {
      if (ArmadilloStates.isOnline()) {
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public static void stateDillo() {
    if (canDillo() && ArmadilloStates.isOnline() && !isSmartTP) {
      new Thread(() -> {
        ArmadilloStates.currentState = null;

        int rodSlot = getItemInSlot.getItemSlot(Items.fishing_rod);
        int drillSlot = GetSBItems.getDrillSlot();

        if (rodSlot == -1 || drillSlot == -1) {
          SendChat.chat(prefix.prefix + "Found no tool cant continue.");
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        throwRodDillo(rodSlot, drillSlot);

        originalBlocks = getBlocks();
        NewSpinDrive.putAllTogether();
        ThreadUtils.threadSleepRandom(config.rod_drill_switch_time);

        if (!ArmadilloStates.isOnline()) {
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        boolean isDilloSummoned = isDilloSummoned();
        int timeChecked = 0;

        while (!isDilloSummoned && timeChecked <= 20) {
          if (timeChecked == 10) {
            throwRodDillo(rodSlot, drillSlot);
          }

          ThreadUtils.sleepThread(20);
          isDilloSummoned = isDilloSummoned();

          timeChecked++;
        }

        if (timeChecked > 20) {
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        int randomClick = RandomisationUtils.randomNumberBetweenInt(0, 1);
        Entity entity = getDilloArmorStand();

        if (entity == null || DistanceFromTo.distanceFromTo(entity.getPosition(), ids.mc.thePlayer.getPosition()) < 2) {
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        interactWithEntity(entity);

        ThreadUtils.threadSleepRandom(100);

        if (randomClick == 1) {
          interactWithEntity(entity);
        }
      })
        .start();
    } else {
      if (!ArmadilloStates.isOnline()) return;

      isThrowRod = false;
      ArmadilloStates.currentState = null;
      TeleportToNextBlock.teleportToNextBlock();
    }
  }

  public static void throwRodDillo(int rodSlot, int drillSlot) {
    SwapToSlot.swapToSlot(rodSlot);

    ids.mc.thePlayer.sendQueue.addToSendQueue(
      new C08PacketPlayerBlockPlacement(
        new BlockPos(-1, -1, -1),
        255,
        ids.mc.thePlayer.inventory.getStackInSlot(rodSlot),
        0,
        0,
        0
      )
    );

    ThreadUtils.threadSleepRandom(100);

    SwapToSlot.swapToSlot(drillSlot);
  }

  public static void interactWithEntity(Entity entity) {
    double dx = ids.mc.thePlayer.getPositionVector().xCoord - entity.getPositionVector().xCoord;
    double dy = ids.mc.thePlayer.getPositionVector().yCoord - entity.getPositionVector().yCoord;
    double dz = ids.mc.thePlayer.getPositionVector().zCoord - entity.getPositionVector().zCoord;
    Vec3 vec = new Vec3(dx, dy, dz);
    ids.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, vec));
  }

  public static Entity getDilloArmorStand() {
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayer player = mc.thePlayer;

    AxisAlignedBB boundingBox = new AxisAlignedBB(
      player.posX - 3,
      player.posY - 3,
      player.posZ - 3,
      player.posX + 3,
      player.posY + 3,
      player.posZ + 3
    );

    List<Entity> entityList = mc.theWorld
      .getEntitiesWithinAABB(Entity.class, boundingBox)
      .stream()
      .filter(a -> {
        return a instanceof EntityArmorStand;
      })
      .collect(Collectors.toList());

    for (Entity entity : entityList) {
      if (entity.getName().toLowerCase().contains(ids.mc.thePlayer.getName().toLowerCase())) {
        return entity;
      }
    }

    return null;
  }

  public static boolean isDilloSummoned() {
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayer player = mc.thePlayer;

    AxisAlignedBB boundingBox = new AxisAlignedBB(
      player.posX - 3,
      player.posY - 3,
      player.posZ - 3,
      player.posX + 3,
      player.posY + 3,
      player.posZ + 3
    );

    List<Entity> entityList = mc.theWorld.getEntitiesWithinAABB(Entity.class, boundingBox);
    for (Entity entity : entityList) {
      if (!(entity instanceof EntityPlayer)) {
        if (entity.getName().contains(player.getName())) {
          isDilloSummonedTickCount++;
          return true;
        }
      }
    }

    return false;
  }

  public static boolean canDillo() {
    return (
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ))
        .isEmpty() ||
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ))
        .isEmpty() ||
      !getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ)).isEmpty()
    );
  }

  public static boolean canDilloOn() {
    return (
      (
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 4,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty() ||
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 3,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty() ||
        !getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 2,
            currentRoute.currentBlock.getZ()
          )
        )
          .isEmpty()
      ) &&
      adjustLook(
        new Vec3(
          currentRoute.currentBlock.getX() + 0.5,
          currentRoute.currentBlock.getY() + 2,
          currentRoute.currentBlock.getZ() + 0.5
        ),
        getNextBlock(),
        new net.minecraft.block.Block[] { Blocks.air },
        false
      ) ==
      null
    );
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END || !canCheckIfOnDillo || !ArmadilloStates.isOnline()) return;

    if (tickDilloCheckCount > 200) {
      resetDillo();
      return;
    }

    if (ids.mc.thePlayer.isRiding()) {
      new Thread(() -> {
        resetDillo();

        if (ArmadilloStates.isOnline()) {
          if (isNoTp) {
            ArmadilloStates.currentState = ROUTEOBSTRUCTEDCLEAR;
            isNoTp = false;
            return;
          }

          ThreadUtils.threadSleepRandom(ping * 2L);
          newSpinDrive();
        } else {
          KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
        }
      })
        .start();
    }

    tickDilloCheckCount++;
  }

  public static void resetDillo() {
    canCheckIfOnDillo = false;
    tickDilloCheckCount = 0;
  }
}
