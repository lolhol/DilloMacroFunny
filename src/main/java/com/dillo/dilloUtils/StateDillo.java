package com.dillo.dilloUtils;

import static com.dillo.ArmadilloMain.CurrentState.ROUTEOBSTRUCTEDCLEAR;
import static com.dillo.ArmadilloMain.CurrentState.SPINDRIVE;
import static com.dillo.data.config.fasterDillo;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.DriveLook.addYaw;
import static com.dillo.dilloUtils.DriveLook.reset;
import static com.dillo.dilloUtils.NewSpinDrive.isLeft;
import static com.dillo.dilloUtils.NewSpinDrive.newSpinDrive;
import static com.dillo.dilloUtils.SpinDrive.jump;
import static com.dillo.dilloUtils.Teleport.TeleportToNextBlock.isThrowRod;
import static com.dillo.utils.RayTracingUtils.adjustLook;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.Teleport.GetNextBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.dilloUtils.Utils.LookYaw;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class StateDillo {

  public static float playerYBe4 = 0;
  public static boolean canCheckIfOnDillo = false;
  public static int tickDilloCheckCount = 0;
  public static boolean isNoTp = false;
  public static int checkedNumber = 0;
  private static boolean look = true;
  public static boolean isSmartTP = false;
  private static float player = 0;
  boolean looking = false;

  public static void stateDilloNoGettingOn() {
    if (canDillo() && ArmadilloStates.isOnline()) {
      ArmadilloStates.currentState = null;
      NewSpinDrive.putAllTogether();
      swapToSlot.swapToSlot(GetSBItems.getDrillSlot());
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
      ArmadilloStates.currentState = null;

      new Thread(() -> {
        int slot = getItemInSlot.getItemSlot(Items.fishing_rod);
        if (slot != -1) {
          ids.mc.thePlayer.inventory.currentItem = slot;
        } else {
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          return;
        }

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        ids.mc.thePlayer.sendQueue.addToSendQueue(
          new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1),
            255,
            ids.mc.thePlayer.inventory.getStackInSlot(slot),
            0,
            0,
            0
          )
        );

        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        swapToSlot.swapToSlot(GetSBItems.getDrillSlot());

        NewSpinDrive.putAllTogether();

        try {
          Thread.sleep(config.rod_drill_switch_time);

          playerYBe4 = (float) ids.mc.thePlayer.posY;
          ids.mc.playerController.sendUseItem(
            ids.mc.thePlayer,
            ids.mc.theWorld,
            ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem)
          );

          if (ArmadilloStates.isOnline()) {
            canCheckIfOnDillo = true;
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      })
        .start();
    } else {
      if (ArmadilloStates.isOnline()) {
        isThrowRod = false;
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
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
          return true;
        }
      }
    }

    return false;
  }

  public static boolean canDillo() {
    return (
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ)).size() >
      0 ||
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ)).size() >
      0 ||
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ)).size() > 0
    );
  }

  public static boolean canDilloOn() {
    return (
      (
        getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 4,
            currentRoute.currentBlock.getZ()
          )
        )
          .size() >
        0 ||
        getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 3,
            currentRoute.currentBlock.getZ()
          )
        )
          .size() >
        0 ||
        getBlocksLayer(
          new BlockPos(
            currentRoute.currentBlock.getX(),
            currentRoute.currentBlock.getY() + 2,
            currentRoute.currentBlock.getZ()
          )
        )
          .size() >
        0
      ) &&
      adjustLook(
        new Vec3(
          currentRoute.currentBlock.getX() + 0.5,
          currentRoute.currentBlock.getY() + 2,
          currentRoute.currentBlock.getZ() + 0.5
        ),
        GetNextBlock.getNextBlock(),
        new net.minecraft.block.Block[] { Blocks.air },
        false
      ) ==
      null
    );
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (canCheckIfOnDillo && ArmadilloStates.isOnline()) {
        if (playerYBe4 - ids.mc.thePlayer.posY + 0.01 < 0) {
          reset();
          checkedNumber = 0;
          tickDilloCheckCount = 0;

          canCheckIfOnDillo = false;

          look = true;

          if (ArmadilloStates.isOnline()) {
            if (isNoTp) {
              ArmadilloStates.currentState = ROUTEOBSTRUCTEDCLEAR;
              isNoTp = false;
            } else {
              KeyBinding.setKeyBindState(jump.getKeyCode(), true);
              if (ArmadilloStates.isOnline()) {
                newSpinDrive();
                //ArmadilloStates.currentState = SPINDRIVE;
              } else {
                KeyBinding.setKeyBindState(jump.getKeyCode(), false);
              }
            }
          }
        } else {
          if (!looking) {
            looking = true;
            if (isLeft) {
              addYaw(200, -20);
            } else {
              addYaw(200, 20);
            }
          }

          if (tickDilloCheckCount >= 4) {
            if (!fasterDillo) {
              if (isDilloSummoned()) {
                rightClick();
              }
            } else {
              rightClick();
            }

            looking = false;
            tickDilloCheckCount = 0;
          } else {
            tickDilloCheckCount++;
          }
        }

        if (checkedNumber > 300) {
          checkedNumber = 0;
          tickDilloCheckCount = 0;
          canCheckIfOnDillo = false;
          look = true;
        } else {
          tickDilloCheckCount++;
          checkedNumber++;
        }

        look = false;
      } else {
        checkedNumber = 0;
        tickDilloCheckCount = 0;
        canCheckIfOnDillo = false;
      }
    }
  }
}
