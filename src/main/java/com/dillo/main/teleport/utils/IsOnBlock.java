package com.dillo.main.teleport.utils;

import static com.dillo.armadillomacro.mobKiller;
import static com.dillo.armadillomacro.vertexMover;
import static com.dillo.calls.CurrentState.ARMADILLO;
import static com.dillo.calls.CurrentState.TPSTAGEWALK;
import static com.dillo.config.config.reTeleport;
import static com.dillo.gui.GUIUtils.totalveins.TotalVeinsMain.totalVeinsCur;
import static com.dillo.main.teleport.Enums.FailsafesOnBlock.*;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.*;
import static com.dillo.main.teleport.utils.LookWhileGoingDown.stopLook;
import static com.dillo.main.teleport.utils.TeleportToBlock.tpWalkOverride;
import static com.dillo.main.utils.looks.DriveLook.addYaw;
import static com.dillo.main.utils.looks.LookAt.reset;
import static com.dillo.main.utils.looks.LookYaw.curRotation;
import static com.dillo.utils.BlockUtils.*;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.events.PlayerLocChangeEvent;
import com.dillo.main.failsafes.RestartMacroFailsafe;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.Enums.FailsafesOnBlock;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetter;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetterConfig;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.RandomisationUtils;
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

  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  public static float yaw = 0;
  public static FailsafesOnBlock curFailsafe;
  public static boolean alrDoing;
  private static boolean startCheck = false;
  private static int checkTime = 0;
  private static BlockPos blockPos = null;
  private static CurrentState nextState = null;
  private static int curTicks = 0;
  private static int curReTps = 0;
  private static boolean isFail = false;
  int timesTriggered = 0;

  public static void isOnBlock(int checkTimeTicks, BlockPos nextBlock, CurrentState newString) {
    blockPos = nextBlock;
    nextState = newString;
    checkTime = checkTimeTicks;
    startCheck = true;
    //df
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startCheck) {
        if (
          Math.abs(ids.mc.thePlayer.getPosition().getX() - blockPos.getX() - 1) < 0.001 &&
          Math.abs(ids.mc.thePlayer.getPosition().getZ() - blockPos.getZ() - 1) < 0.001 &&
          !alrDoing
        ) {
          doneTp();
        }

        if (curTicks >= config.checkOnBlockTime) {
          notOnBlock();
        }
      }

      if (startCheck) curTicks++;
    }
  }

  @SubscribeEvent
  public void onChange(PlayerLocChangeEvent event) {
    if (startCheck && ArmadilloStates.isOnline()) {
      startCheck = false;
      new Thread(() -> {
        if (DistanceFromTo.distanceFromTo(event.newPos, blockPos) > 100) {
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          ArmadilloStates.currentState = null;
          SendChat.chat(
            prefix.prefix + "You may be admin checked but you are over 100 blocks away from ur destination block :L"
          );
          return;
        }

        if (
          Math.abs(event.newPos.getX() - blockPos.getX() - 1) < 0.001 &&
          Math.abs(event.newPos.getZ() - blockPos.getZ() - 1) < 0.001
        ) {
          doneTp();
        } else {
          try {
            Thread.sleep(400 + RandomisationUtils.randomNumberBetweenInt(1, 300));
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }

          notOnBlock();
        }

        alrDoing = true;
      })
        .start();
    }
  }

  void notOnBlock() {
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

  void doneTp() {
    totalVeinsCur++;
    reset();
    RayTracingUtils.foundCollisions.clear();
    isThrowRod = true;
    startCheck = false;
    curReTps = 0;
    clearAttempts = 0;
    curTicks = 0;
    attemptedToSmartTP = false;
    alrDoing = false;

    SendChat.chat(prefix.prefix + "Teleported successfully!");
    if (SNEAK.isPressed()) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

    yaw = 10F + (float) RandomisationUtils.randomNumberBetweenDouble(-1, 1);
    alrMoved = false;
    LookAt.smoothLook(new LookAt.Rotation(yaw, curRotation()), 40 + RandomisationUtils.randomNumberBetweenInt(0, 40));

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
              curFailsafe = FAILSAFE_RETP;
              initiateFailSafes();
            }

            tpWalkOverride = false;
          }
        } else {
          curFailsafe = FAILSAFE_RETP;
          initiateFailSafes();
        }

        return;
      case FAILSAFE_RETP:
        SendChat.chat(prefix.prefix + "Failed to teleport!" + (reTeleport ? " Re-Teleporting!" : ""));

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
