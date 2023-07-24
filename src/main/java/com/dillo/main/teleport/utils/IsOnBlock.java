package com.dillo.main.teleport.utils;

import static com.dillo.armadillomacro.mobKiller;
import static com.dillo.armadillomacro.vertexMover;
import static com.dillo.calls.CurrentState.*;
import static com.dillo.config.config.reTeleport;
import static com.dillo.main.teleport.Enums.FailsafesOnBlock.*;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.*;
import static com.dillo.main.teleport.utils.LookWhileGoingDown.stopLook;
import static com.dillo.main.teleport.utils.TeleportToBlock.tpWalkOverride;
import static com.dillo.main.utils.looks.DriveLook.addYaw;
import static com.dillo.main.utils.looks.LookAt.reset;
import static com.dillo.main.utils.looks.LookYaw.curRotation;
import static com.dillo.utils.BlockUtils.getNextBlock;
import static com.dillo.utils.BlockUtils.getPrevBlockPos;
import static com.dillo.utils.BlockUtils.isOnBlockInRoute;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.failsafes.RestartMacroFailsafe;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.Enums.FailsafesOnBlock;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetter;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetterConfig;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.RayTracingUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
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
  public static FailsafesOnBlock curFailsafe;
  int timesTriggered = 0;

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

              yaw = 0.0F;
              alrMoved = false;
              LookAt.smoothLook(new LookAt.Rotation(yaw, curRotation()), 100);

              isClearing = false;

              if (RestartMacroFailsafe.isRestart) {
                ArmadilloStates.offlineState = KillSwitch.ONLINE;
                RestartMacroFailsafe.isRestart = false;
              }

              isTeleporting = true;

              timesTriggered = 0;

              currentRoute.currentBlock = blockPos;
              mobKiller.killMobsAround(6, nextState);
            }
          }
        } else {
          reset();
          startCheck = false;
          curTicks = 0;
          stopLook();

          BlockPos nextBlock = getNextBlock();
          if (nextBlock == null || ids.mc.theWorld.getBlockState(nextBlock).getBlock() == Blocks.air) {
            ArmadilloStates.offlineState = KillSwitch.OFFLINE;
            SendChat.chat(prefix.prefix + "Failed to tp! No cobblestone detected :/.");
            return;
          }

          if (timesTriggered < 5) {
            curFailsafe = FAILSAFE_TPBACKANDNEXT;
            initiateFailSafes();
            timesTriggered++;
          }
        }
      }

      if (startCheck) curTicks++;
    }
  }

  void initiateFailSafes() {
    ArmadilloStates.currentState = null;

    switch (curFailsafe) {
      case FAILSAFE_TPBACKANDNEXT:
        if (!isOnBlockInRoute(ids.mc.thePlayer.getPosition())) {
          BlockPos prevBlock = getPrevBlockPos(blockPos);
          if (prevBlock != null) {
            tpWalkOverride = true;

            boolean result = TeleportToBlock.teleportToBlock(
              prevBlock,
              config.tpHeadMoveSpeed,
              config.tpWait,
              ARMADILLO
            );

            if (result) {
              curFailsafe = null;
            } else {
              SendChat.chat(prefix.prefix + "Failed to restart!");
              curFailsafe = null;
              ArmadilloStates.offlineState = KillSwitch.OFFLINE;
            }

            tpWalkOverride = false;
          }
        } else {
          curFailsafe = FAILSAFE_RETP;
          initiateFailSafes();
        }

        return;
      case FAILSAFE_RETP:
        SendChat.chat(prefix.prefix + "Failed to teleport!" + (reTeleport ? "Re-Teleporting!" : ""));

        if (isOnBlockInRoute(ids.mc.thePlayer.getPosition())) {
          if (curReTps < config.reTpTimes) {
            curReTps++;
            addYaw(300, 90);

            new Thread(() -> {
              try {
                Thread.sleep(300);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }

              if (!ArmadilloStates.isOnline()) return;

              tpWalkOverride = true;

              boolean result = TeleportToBlock.teleportToBlock(
                blockPos,
                config.tpHeadMoveSpeed,
                config.tpWait,
                ARMADILLO
              );

              if (result) {
                curFailsafe = null;
              } else {
                SendChat.chat(prefix.prefix + "Failed to restart!");
                // curFailsafe = null;
                // ArmadilloStates.offlineState = KillSwitch.OFFLINE;
                curFailsafe = FAILSAFE_GEMPREVENTING;
                initiateFailSafes();
              }

              tpWalkOverride = false;
            })
              .start();
          }
        }

        return;
      case FAILSAFE_GEMPREVENTING:
        VertexGetter getVertex = new VertexGetter();
        VertexGetterConfig vertConfig = new VertexGetterConfig(
          ids.mc.thePlayer.getPositionVector(),
          nextBlockInList,
          1.54F
        );
        VertexGetter.VertexGetterClass vertex = getVertex.getVertex(vertConfig);
        if (vertex != null) {
          vertexMover.moveToVertex(vertex, TPSTAGEWALK, true, 60);
          alrMoved = true;
        } else {
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        }

        return;
    }
  }
}
