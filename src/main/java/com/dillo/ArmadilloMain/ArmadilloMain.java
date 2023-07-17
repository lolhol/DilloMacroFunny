package com.dillo.ArmadilloMain;

import static com.dillo.ArmadilloMain.CurrentState.NEXTBLOCKSTAGE2;
import static com.dillo.ArmadilloMain.CurrentState.SPINDRIVE;
import static com.dillo.dilloUtils.FailSafes.AnswerPPL.answerAccusation;
import static com.dillo.dilloUtils.NewSpinDrive.startAgain;
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
import com.dillo.utils.previous.SendChat;
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
      try {
        if (ArmadilloStates.isOnline()) {
          if (ArmadilloStates.currentState != null) {
            switch (ArmadilloStates.currentState) {
              case TPSTAGEWALK:
                tpStageWalk();
                return;
              case ROUTEOBSTRUCTEDCLEAR:
                SpinDrive.onStateSpinDrive();
                return;
              case CENTERSTAGE2:
                centerStage2();
                return;
              case ARMADILLO:
                StateDillo.stateDillo();
                return;
              case SPINDRIVE:
                NewSpinDrive.newSpinDrive();
                return;
              case TPSTAGE2:
                TeleportToBlock.teleportStage2();
                return;
              case TPSTAGE3:
                TeleportToBlock.teleportStage3();
                return;
              case STARTWALKINGPATH:
                WalkOnPath.startWalkingPath();
                return;
              case STARTCHECKDILLO:
                GetOffArmadillo.getOffArmadillo(NEXTBLOCKSTAGE2, currentRoute.currentBlock.getY(), 500, false);
                return;
              case RESTARTPATHFINDER:
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
                return;
              case RESUMEWALKING:
                ArmadilloStates.currentState = null;
                WalkOnPath.walkOnPath(WalkOnPath.blockRoute);
                return;
              case NEXTBLOCKSTAGE2:
                TeleportToNextBlock.teleportToNextBlockStage2();
                return;
              case STARTMACRO:
                StartMacro.startMacro();
                return;
              case STARTAGAINDRIVE:
                DilloDriveBlockDetection.detectBlocks();
                ArmadilloStates.currentState = SPINDRIVE;
                return;
              case ANSWER_ACCUSATION:
                answerAccusation();
                return;
              case SMARTTP:
                TPToNext();
                return;
              case STATEDILLONOGETTINGON:
                StateDillo.stateDilloNoGettingOn();
                return;
              case RESTARTDRIVEWAIT:
                startAgain();
                return;
            }
          }
        }
      } catch (NullPointerException e) {
        SendChat.chat(String.valueOf(ArmadilloStates.isOnline()));
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
