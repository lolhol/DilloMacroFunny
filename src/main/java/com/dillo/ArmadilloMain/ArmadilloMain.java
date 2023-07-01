package com.dillo.ArmadilloMain;

import static com.dillo.dilloUtils.FailSafes.AnswerPPL.answerAccusation;
import static com.dillo.dilloUtils.Utils.CenterPlayer.centerStage2;

import com.dillo.Pathfinding.BlockNode;
import com.dillo.Pathfinding.PathFinderV2;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.data.config;
import com.dillo.dilloUtils.*;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.FailSafes.ServerTPSFailsafe;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.StartMacro;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ArmadilloMain {

  int dilloCounter = 0;
  public static int newDilloCounter = 0;
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (Objects.equals(ArmadilloStates.offlineState, "online")) {
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
          PathFinderV2.restartFinder(new BlockNode(playerPos, DistanceFromTo.distanceFromTo(PathFinderV2.lastFinalDestination.blockPos(), playerPos), 0.0, null));
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
  }
}
