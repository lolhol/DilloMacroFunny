package com.dillo.dilloUtils.Teleport;

import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;
import static com.dillo.ArmadilloMain.CurrentState.RETELEPORTING;
import static com.dillo.data.config.reTeleport;
import static com.dillo.dilloUtils.LookAt.reset;
import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;
import static com.dillo.dilloUtils.NewSpinDrive.random;
import static com.dillo.dilloUtils.StateDillo.canDillo;
import static com.dillo.dilloUtils.Teleport.TeleportToNextBlock.*;
import static com.dillo.dilloUtils.TpUtils.LookWhileGoingDown.stopLook;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.isClear;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.FailSafes.RestartMacroFailsafe;
import com.dillo.dilloUtils.LookAt;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.RayTracingUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class IsOnBlock {

  private static boolean startCheck = false;
  private static int checkTime = 0;
  private static BlockPos blockPos = null;
  private static CurrentState nextState = null;
  private static int curTicks = 0;
  private static int curReTps = 0;
  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  public static float yaw = 0;
  private static boolean isFail = false;

  public static void isOnBlock(int checkTimeTicks, BlockPos nextBlock, CurrentState newString) {
    blockPos = nextBlock;
    nextState = newString;
    checkTime = checkTimeTicks;
    startCheck = true;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startCheck) {
        if (curTicks <= checkTime) {
          if (blockPos != null) {
            if (
              Math.abs(ids.mc.thePlayer.posX - blockPos.getX() - 0.5) < 0.0001 &&
              Math.abs(ids.mc.thePlayer.posZ - blockPos.getZ() - 0.5) < 0.0001
            ) {
              reset();
              RayTracingUtils.foundCollisions.clear();
              isThrowRod = true;
              startCheck = false;
              curReTps = 0;
              clearAttempts = 0;
              curTicks = 0;

              SendChat.chat(prefix.prefix + "Teleported successfully!");
              KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

              float p = 10 + (random.nextFloat() < 0.5 ? random.nextFloat() * 5 : -(random.nextFloat() * 5));
              yaw = p;
              LookAt.smoothLook(new LookAt.Rotation(p, curRotation()), 100);

              if (RestartMacroFailsafe.isRestart) {
                ArmadilloStates.offlineState = KillSwitch.ONLINE;
                RestartMacroFailsafe.isRestart = false;
              }

              isTeleporting = true;

              currentRoute.currentBlock = blockPos;
              ArmadilloStates.currentState = nextState;
            }
          }
        } else {
          reset();
          SendChat.chat(prefix.prefix + "Failed to teleport!! " + (reTeleport ? "Re-Teleporting!" : ""));
          startCheck = false;

          curTicks = 0;

          stopLook();
          if (
            canDillo() &&
            clearAttempts < 4 &&
            currentRoute.currentRoute.contains(makeNewBlock(0, -1, 0, ids.mc.thePlayer.getPosition())) &&
            canClear(RayTracingUtils.foundCollisions) &&
            !SmartTP.smartTpBlocks.contains(blockPos)
          ) {
            yaw = 30.2F;
            KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
            isClear = true;
            ArmadilloStates.currentState = ARMADILLO;
            clearAttempts++;
          } else {
            if (!config.smartTeleport) {
              if (reTeleport && curReTps <= config.reTpTimes) {
                curReTps++;
                ArmadilloStates.currentState = RETELEPORTING;
                TeleportToNextBlock.teleportToNextBlock();
              }
            } else {
              if (reTeleport) {
                if (curReTps <= config.reTpTimes) {
                  curReTps++;
                  ArmadilloStates.currentState = RETELEPORTING;
                  isThrowRod = false;
                  TeleportToNextBlock.teleportToNextBlock();
                } else {
                  SmartTP.smartTP(blockPos, false);
                }
              } else {
                SmartTP.smartTP(blockPos, false);
              }
            }
          }
        }
      }

      if (startCheck) curTicks++;
    }
  }

  private static boolean canClear(List<BlockPos> blocks) {
    for (BlockPos block : blocks) {
      double distance = DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition());

      if (
        distance <= 5.09901951359 &&
        (
          ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass ||
          ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane
        )
      ) {
        return true;
      }
    }

    return false;
  }
}
