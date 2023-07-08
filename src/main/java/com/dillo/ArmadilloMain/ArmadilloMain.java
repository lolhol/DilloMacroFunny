package com.dillo.ArmadilloMain;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.detectBlocks;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.FailSafes.AnswerPPL.answerAccusation;
import static com.dillo.dilloUtils.NewSpinDrive.random;
import static com.dillo.dilloUtils.Teleport.SmartTP.TPToNext;
import static com.dillo.dilloUtils.Teleport.TeleportToBlock.tpStageWalk;
import static com.dillo.dilloUtils.Utils.CenterPlayer.centerStage2;

import com.dillo.Pathfinding.BlockNode;
import com.dillo.Pathfinding.PathFinderV2;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.data.config;
import com.dillo.dilloUtils.*;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.dilloUtils.Utils.LookYaw;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.StartMacro;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ArmadilloMain {

  public static int newDilloCounter = 0;
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
  private static int blockTime = 0;
  public static boolean test = false;
  private static boolean testv1 = false;
  private static boolean isDone = true;
  private static float closest = 0;
  private static List<DilloDriveBlockDetection.BlockAngle> angles = new ArrayList<>();
  private static float lastBlockAngle = 0;
  private static List<DilloDriveBlockDetection.BlockAngle> returnBlocks = new ArrayList<>();

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (Objects.equals(ArmadilloStates.offlineState, "online")) {
        if (Objects.equals(ArmadilloStates.currentState, "tpStageWalk")) {
          tpStageWalk();
        }

        if (Objects.equals(ArmadilloStates.currentState, "routeObstructedClear")) {
          SpinDrive.onStateSpinDrive();
        }

        if (Objects.equals(ArmadilloStates.currentState, "centerStage2")) {
          centerStage2();
        }

        if (Objects.equals(ArmadilloStates.currentState, "armadillo")) {
          StateDillo.stateDillo();
        }

        if (Objects.equals(ArmadilloStates.currentState, "spinDrive")) {
          NewSpinDrive.newSpinDrive();
        }

        if (Objects.equals(ArmadilloStates.currentState, "tpStage2")) {
          TeleportToBlock.teleportStage2();
        }

        if (Objects.equals(ArmadilloStates.currentState, "tpStage3")) {
          TeleportToBlock.teleportStage3();
        }

        if (Objects.equals(ArmadilloStates.currentState, "startWalkingPath")) {
          WalkOnPath.startWalkingPath();
        }

        if (Objects.equals(ArmadilloStates.currentState, "startCheckDillo")) {
          GetOffArmadillo.getOffArmadillo("NextBlockStage2", currentRoute.currentBlock.getY(), 500, false);
        }

        if (Objects.equals(ArmadilloStates.currentState, "restartPathfinder")) {
          ArmadilloStates.currentState = null;
          BlockPos playerPos = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
          PathFinderV2.restartFinder(
            new BlockNode(
              playerPos,
              DistanceFromTo.distanceFromTo(PathFinderV2.lastFinalDestination.blockPos(), playerPos),
              0.0,
              null
            )
          );
        }

        if (Objects.equals(ArmadilloStates.currentState, "resumeWalking")) {
          ArmadilloStates.currentState = null;
          WalkOnPath.walkOnPath(WalkOnPath.blockRoute);
        }

        if (Objects.equals(ArmadilloStates.currentState, "NextBlockStage2")) {
          TeleportToNextBlock.teleportToNextBlockStage2();
        }

        if (Objects.equals(ArmadilloStates.currentState, "startMacro")) {
          StartMacro.startMacro();
        }

        if (Objects.equals(ArmadilloStates.currentState, "startAgainDrive")) {
          DilloDriveBlockDetection.detectBlocks();
          ArmadilloStates.currentState = "spinDrive";
        }

        if (Objects.equals(ArmadilloStates.currentState, "ANSWER_ACCUSATION")) {
          answerAccusation();
        }

        if (Objects.equals(ArmadilloStates.currentState, "SMARTTP")) {
          TPToNext();
        }
      }
    }

    if (test) {
      if (
        (
          getBlocksLayer(
            new BlockPos(
              currentRoute.curPlayerPos.getX(),
              currentRoute.curPlayerPos.getY() + 2,
              currentRoute.curPlayerPos.getZ()
            )
          )
            .size() >
          0 ||
          getBlocksLayer(
            new BlockPos(
              currentRoute.curPlayerPos.getX(),
              currentRoute.curPlayerPos.getY() + 1,
              currentRoute.curPlayerPos.getZ()
            )
          )
            .size() >
          0 ||
          getBlocksLayer(
            new BlockPos(
              currentRoute.curPlayerPos.getX(),
              currentRoute.curPlayerPos.getY(),
              currentRoute.curPlayerPos.getZ()
            )
          )
            .size() >
          0
        ) &&
        blockTime < 1000
      ) {
        if (isDone) {
          returnBlocks = detectBlocks();

          if (returnBlocks != null) {
            if (returnBlocks.size() < 1) {
              ArmadilloStates.currentState = null;
              ArmadilloStates.offlineState = "offline";
              com.dillo.utils.previous.chatUtils.SendChat.chat("NO BLOCKS FOUND....");
            } else {
              DilloDriveBlockDetection.BlockAngle lastPos = returnBlocks.get(returnBlocks.size() - 1);
              lastBlockAngle = lastPos.angle;
              closest = returnBlocks.get(0).angle;
              currentRoute.curPlayerPos = ids.mc.thePlayer.getPosition();
            }
          }

          isDone = false;
        }

        if (lastBlockAngle > 0) {
          if (!testv1) {
            float yaw = config.headMovement * 3 + random.nextFloat() * 10;

            LookYaw.lookToYaw(config.headMovement * 10L, yaw);
            lastBlockAngle -= yaw;
          }
        } else {
          testv1 = true;
        }

        if (testv1) {
          if (lastBlockAngle < closest) {
            float yaw = config.headMovement * 3 + random.nextFloat() * 10;
            LookYaw.lookToYaw(-config.headMovement * 10L, yaw);
            SendChat.chat(String.valueOf(lastBlockAngle));
            lastBlockAngle += yaw;
          } else {
            isDone = true;
            testv1 = false;
          }
        }

        blockTime++;
      }
    }
  }

  private static boolean isNegMore(List<DilloDriveBlockDetection.BlockAngle> list) {
    int negative = 0;
    int pos = 0;

    for (DilloDriveBlockDetection.BlockAngle i : list) {
      if (i.angle < 0) {
        negative++;
      } else {
        pos++;
      }
    }

    return negative > pos;
  }
}
