package com.dillo.dilloUtils;

import static com.dillo.data.config.fasterDillo;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.NewSpinDrive.isLeft;
import static com.dillo.dilloUtils.SpinDrive.jump;
import static com.dillo.dilloUtils.Teleport.TeleportToNextBlock.isThrowRod;
import static com.dillo.dilloUtils.Utils.LookYaw.lookToPitch;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.dilloUtils.Utils.LookYaw;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import com.dillo.utils.throwRod;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class StateDillo {

  public static float playerYBe4;
  public static boolean canCheckIfOnDillo = false;
  public static int tickDilloCheckCount = 0;
  public static boolean isNoTp = false;
  public static int checkedNumber = 0;
  private static boolean look = true;
  public static boolean isSmartTP = false;
  private static float lastPitch = 0;

  public static void stateDilloNoGettingOn() {
    if (
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ)).size() >
      0 &&
      Objects.equals(ArmadilloStates.offlineState, "online")
    ) {
      ArmadilloStates.currentState = null;
      swapToSlot.swapToSlot(GetSBItems.getDrillSlot());
      ArmadilloStates.currentState = "spinDrive";
    } else {
      if (Objects.equals(ArmadilloStates.offlineState, "online")) {
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public static void stateDillo() {
    if (
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ)).size() >
      0 &&
      Objects.equals(ArmadilloStates.offlineState, "online") &&
      !isSmartTP
    ) {
      throwRod.throwRodInv();
      ArmadilloStates.currentState = null;
      swapToSlot.swapToSlot(GetSBItems.getDrillSlot());

      lookToPitch(1, 10);

      NewSpinDrive.putAllTogether();

      if (isLeft) {
        LookYaw.lookToYaw(config.rod_drill_switch_time + 150, 20);
      } else {
        LookYaw.lookToYaw(config.rod_drill_switch_time + 150, -20);
      }

      new Thread(() -> {
        try {
          Thread.sleep(config.rod_drill_switch_time);

          playerYBe4 = (float) ids.mc.thePlayer.posY;
          ids.mc.playerController.sendUseItem(
            ids.mc.thePlayer,
            ids.mc.theWorld,
            ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem)
          );

          if (Objects.equals(ArmadilloStates.offlineState, "online")) {
            canCheckIfOnDillo = true;
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      })
        .start();
    } else {
      if (Objects.equals(ArmadilloStates.offlineState, "online")) {
        isThrowRod = false;
        ArmadilloStates.currentState = null;
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public boolean isDilloSummoned() {
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

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (canCheckIfOnDillo && Objects.equals(ArmadilloStates.offlineState, "online")) {
        if (tickDilloCheckCount >= 4) {
          if (ids.mc.thePlayer.isRiding()) {
            KeyBinding.setKeyBindState(jump.getKeyCode(), true);
            checkedNumber = 0;
            tickDilloCheckCount = 0;

            lookToPitch(20, -lastPitch);

            canCheckIfOnDillo = false;

            look = true;

            if (Objects.equals(ArmadilloStates.offlineState, "online")) {
              if (isNoTp) {
                ArmadilloStates.currentState = "routeObstructedClear";
                isNoTp = false;
              } else {
                ArmadilloStates.currentState = "spinDrive";
              }
            }
          } else {
            if (!fasterDillo) {
              if (isDilloSummoned()) {
                rightClick();
              }
            } else {
              rightClick();
            }
          }

          tickDilloCheckCount = 0;
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
