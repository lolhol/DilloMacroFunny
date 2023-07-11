package com.dillo.ArmadilloMain;

import static com.dillo.ArmadilloMain.CurrentState.*;
import static com.dillo.dilloUtils.FailSafes.AnswerPPL.answerAccusation;
import static com.dillo.dilloUtils.Teleport.SmartTP.TPToNext;
import static com.dillo.dilloUtils.Teleport.TeleportToBlock.tpStageWalk;
import static com.dillo.dilloUtils.Utils.CenterPlayer.centerStage2;

import com.dillo.Pathfinding.BlockNode;
import com.dillo.Pathfinding.PathFinderV2;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.dilloUtils.*;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.StartMacro;
import com.dillo.utils.previous.random.ids;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ArmadilloMain {

  public static int newDilloCounter = 0;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (ArmadilloStates.isOnline()) {
        if (ArmadilloStates.currentState == TPSTAGEWALK) {
          tpStageWalk();
        }

        if (ArmadilloStates.currentState == ROUTEOBSTRUCTEDCLEAR) {
          SpinDrive.onStateSpinDrive();
        }

        if (ArmadilloStates.currentState == CENTERSTAGE2) {
          centerStage2();
        }

        if (ArmadilloStates.currentState == ARMADILLO) {
          StateDillo.stateDillo();
        }

        if (ArmadilloStates.currentState == SPINDRIVE) {
          NewSpinDrive.newSpinDrive();
        }

        if (ArmadilloStates.currentState == TPSTAGE2) {
          TeleportToBlock.teleportStage2();
        }

        if (ArmadilloStates.currentState == TPSTAGE3) {
          TeleportToBlock.teleportStage3();
        }

        if (ArmadilloStates.currentState == STARTWALKINGPATH) {
          WalkOnPath.startWalkingPath();
        }

        if (ArmadilloStates.currentState == STARTCHECKDILLO) {
          GetOffArmadillo.getOffArmadillo(NEXTBLOCKSTAGE2, currentRoute.currentBlock.getY(), 500, false);
        }

        if (ArmadilloStates.currentState == RESTARTPATHFINDER) {
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

        if (ArmadilloStates.currentState == RESUMEWALKING) {
          ArmadilloStates.currentState = null;
          WalkOnPath.walkOnPath(WalkOnPath.blockRoute);
        }

        if (ArmadilloStates.currentState == NEXTBLOCKSTAGE2) {
          TeleportToNextBlock.teleportToNextBlockStage2();
        }

        if (ArmadilloStates.currentState == STARTMACRO) {
          StartMacro.startMacro();
        }

        if (ArmadilloStates.currentState == STARTAGAINDRIVE) {
          DilloDriveBlockDetection.detectBlocks();
          ArmadilloStates.currentState = SPINDRIVE;
        }

        if (ArmadilloStates.currentState == ANSWER_ACCUSATION) {
          answerAccusation();
        }

        if (ArmadilloStates.currentState == SMARTTP) {
          TPToNext();
        }
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
