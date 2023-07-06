package com.dillo.ArmadilloMain;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.FailSafes.AnswerPPL.answerAccusation;
import static com.dillo.dilloUtils.NewSpinDrive.random;
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
import com.dillo.utils.GetAngleToBlock;
import com.dillo.utils.StartMacro;
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
        List<BlockPos> blocks1 = getBlocksLayer(
          new BlockPos(
            currentRoute.curPlayerPos.getX(),
            currentRoute.curPlayerPos.getY() + 2,
            currentRoute.curPlayerPos.getZ()
          )
        );

        List<BlockPos> blocks2 = getBlocksLayer(
          new BlockPos(
            currentRoute.curPlayerPos.getX(),
            currentRoute.curPlayerPos.getY() + 1,
            currentRoute.curPlayerPos.getZ()
          )
        );

        List<BlockPos> blocks3 = getBlocksLayer(
          new BlockPos(
            currentRoute.curPlayerPos.getX(),
            currentRoute.curPlayerPos.getY(),
            currentRoute.curPlayerPos.getZ()
          )
        );

        List<BlockPos> combined = new ArrayList<>();
        combined.addAll(blocks1);
        combined.addAll(blocks2);
        combined.addAll(blocks3);

        List<DilloDriveBlockDetection.BlockAngle> angles = new ArrayList<>();

        for (BlockPos block : combined) {
          float angle = GetAngleToBlock.calcAngle(block);

          DilloDriveBlockDetection.BlockAngle blockAngle = new DilloDriveBlockDetection.BlockAngle(angle, block);
          angles.add(blockAngle);
        }

        angles.sort((a, b) -> {
          return a.angle < b.angle ? -1 : 1;
        });

        if (angles.size() > 0) {
          float angleMax = angles.get(angles.size() - 1).angle;

          if (angleMax > 180) {
            LookYaw.lookToYaw(config.headMovement * 10L, config.headMovement * 3 + random.nextFloat() * 10);
          } else {
            LookYaw.lookToYaw(config.headMovement * 10L, -config.headMovement * 3 + random.nextFloat() * 10);
          }

          blockTime++;
        }
      }
    }
  }
}
