package com.dillo.calls;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.macro.main.GetOffArmadillo;
import com.dillo.main.macro.main.NewSpinDrive;
import com.dillo.main.macro.main.StateDillo;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.main.teleport.utils.TeleportToBlock;
import com.dillo.pathfinding.Brigeros.BlockNode;
import com.dillo.pathfinding.Brigeros.PathFinderV2;
import com.dillo.pathfinding.Brigeros.WalkOnPath;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.StartMacro;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.calls.CurrentState.NEXTBLOCKSTAGE2;
import static com.dillo.calls.CurrentState.SPINDRIVE;
import static com.dillo.main.failsafes.AnswerPPL.answerAccusation;
import static com.dillo.main.macro.main.NewSpinDrive.startAgain;
import static com.dillo.main.teleport.macro.SmartTP.TPToNext;
import static com.dillo.main.teleport.utils.TeleportToBlock.tpStageWalk;
import static com.dillo.main.utils.CenterPlayer.centerStage2;

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
}
