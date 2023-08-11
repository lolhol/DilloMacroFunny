package com.dillo.main.failsafes;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.failsafes.RestartMacroUtils.BestBlocks;
import com.dillo.main.failsafes.RestartMacroUtils.CheckIfCanSeeAny;
import com.dillo.main.failsafes.RestartMacroUtils.GetClosestBlocks;
import com.dillo.main.failsafes.RestartMacroUtils.IsAuthenticated;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.main.teleport.utils.TeleportToBlock;
import com.dillo.pathfinding.Brigeros.FindPathToBlock;
import com.dillo.pathfinding.Brigeros.WalkOnPath;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static com.dillo.calls.CurrentState.ARMADILLO;

public class RestartMacroFailsafe {

    public static boolean isRestart = false;
    private static int ticks = 0;

    public static void restartMacro() {
        isRestart = true;
        List<BlockPos> closestBlocks = GetClosestBlocks.getClosestBlocks(currentRoute.currentRoute, 60);

        if (closestBlocks.size() > 0) {
            List<BlockPos> visibleBlocks = CheckIfCanSeeAny.checkIfCanSeeAny(closestBlocks);

            if (visibleBlocks.size() > 0) {
                List<BlockPos> bestVisibleBlocks = BestBlocks.bestBlocks(visibleBlocks);
                BlockPos bestBlock = bestVisibleBlocks.get(0);

                TeleportToBlock.teleportToBlock(bestBlock, config.tpHeadMoveSpeed, config.tpWait, ARMADILLO);
            } else {
                if (IsAuthenticated.isAuthenticated()) {
                    List<BlockPos> foundRoute = FindPathToBlock.pathfinderTest(TeleportToNextBlock.nextBlockInList);

                    if (foundRoute != null) {
            /*RenderMultipleLines.renderMultipleLines(null, null, false);
            for (int i = 0; i < foundRoute.size(); i++) {
              if (i != foundRoute.size() - 1) {
                RenderMultipleLines.renderMultipleLines(foundRoute.get(i), foundRoute.get(i + 1), true);
              }
            }*/

                        foundRoute.remove(0);

                        ArmadilloStates.offlineState = KillSwitch.ONLINE;
                        WalkOnPath.walkOnPath(foundRoute);
                    } else {
                        SendChat.chat(prefix.prefix + "Could not find path...");
                    }
                } else {
                    SendChat.chat(prefix.prefix + "Hmmmmmm... Thats a bit suspect... ur not verified...");
                }
            }
        } else {
            isRestart = false;
            SendChat.chat(prefix.prefix + "There are no blocks close by!");
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (ArmadilloStates.isOnline() && config.restartMacro) {
            if (ArmadilloStates.currentState == null) {
                ticks++;
            } else {
                ticks = 0;
            }

            if (ticks >= config.restartTrigerTime * 20) {
                ArmadilloStates.offlineState = KillSwitch.OFFLINE;
                ArmadilloStates.currentState = null;
                restartMacro();
            }
        }
    }
}
