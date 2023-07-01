package com.dillo.dilloUtils;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.BlockUtils;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.SetStatesNull;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import com.dillo.utils.throwRod;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (canCheckIfOnDillo && Objects.equals(ArmadilloStates.offlineState, "online")) {
        if (tickDilloCheckCount >= 2) {
          if (playerYBe4 + (config.onDilloDist / 10) < ids.mc.thePlayer.posY) {
            checkedNumber = 0;
            tickDilloCheckCount = 0;
            swapToSlot.swapToSlot(GetSBItems.getDrillSlot());

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
            rightClick();
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
