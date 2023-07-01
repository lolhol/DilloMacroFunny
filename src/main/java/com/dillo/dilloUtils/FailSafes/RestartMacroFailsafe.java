package com.dillo.dilloUtils.FailSafes;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.Pathfinding.FindPathToBlock;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.FailSafes.RestartMacroUtils.BestBlocks;
import com.dillo.dilloUtils.FailSafes.RestartMacroUtils.CheckIfCanSeeAny;
import com.dillo.dilloUtils.FailSafes.RestartMacroUtils.GetClosestBlocks;
import com.dillo.dilloUtils.FailSafes.RestartMacroUtils.IsAuthenticated;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleLines;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

        TeleportToBlock.teleportToBlock(bestBlock, config.tpHeadMoveSpeed, config.tpWait, "armadillo");
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

            ArmadilloStates.offlineState = "online";
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
    if (Objects.equals(ArmadilloStates.offlineState, "online") && config.restartMacro) {
      if (ArmadilloStates.currentState == null) {
        ticks++;
      } else {
        ticks = 0;
      }

      if (ticks >= config.restartTrigerTime * 20) {
        ArmadilloStates.offlineState = "offline";
        ArmadilloStates.currentState = null;
        restartMacro();
      }
    }
  }
}
