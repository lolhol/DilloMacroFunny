package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import com.dillo.utils.throwRod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Objects;

import static com.dillo.data.config.fasterDillo;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.utils.keyBindings.rightClick;

public class StateDillo {

  private static final KeyBinding rightClick = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
  public static float playerYBe4;
  public static boolean canCheckIfOnDillo = false;
  public static int tickDilloCheckCount = 0;
  public static boolean isNoTp = false;
  public static int checkedNumber = 0;

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
      Objects.equals(ArmadilloStates.offlineState, "online")
    ) {
      throwRod.throwRodInv();
      ArmadilloStates.currentState = null;
      swapToSlot.swapToSlot(GetSBItems.getDrillSlot());

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
            checkedNumber = 0;
            tickDilloCheckCount = 0;

            canCheckIfOnDillo = false;

            NewSpinDrive.putAllTogether();

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
        } else {
          tickDilloCheckCount++;
          checkedNumber++;
        }
      } else {
        checkedNumber = 0;
        tickDilloCheckCount = 0;
        canCheckIfOnDillo = false;
      }
    }
  }
}
