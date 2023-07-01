package com.dillo.commands.UtilCommands;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.Pathfinding.BlockNode;
import com.dillo.Pathfinding.PathFinderV2;
import com.dillo.Pathfinding.WalkOnPath;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleLines;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.BlockPos;

public class DetectEntityUnderCommand extends Command {

  public boolean startRender = false;

  public DetectEntityUnderCommand() {
    super("testE");
  }

  @DefaultHandler
  public void handle(String args) {
    if (Objects.equals(args, "true")) {
      ArmadilloStates.offlineState = "online";
      ArmadilloStates.currentState = "spinDrive";
    } else {
      ArmadilloStates.offlineState = "offline";
      ArmadilloStates.currentState = null;
    }

    /*if (Objects.equals(arg, "true")) {
      RenderMultipleBlocksMod.renderMultipleBlocks(null, false);
      RenderMultipleLines.renderMultipleLines(null, null, false);

      BlockPos playerPos = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
      BlockPos destBlockPos = new BlockPos(205, 81, 218);

      List<BlockPos> foundPath = PathFinderV2.pathFinder(
        new BlockNode(destBlockPos, DistanceFromTo.distanceFromTo(playerPos, destBlockPos), 0.0, null),
        new BlockNode(playerPos, 0.0, DistanceFromTo.distanceFromTo(playerPos, destBlockPos), null)
      );

      if (foundPath != null) {
        for (int i = 0; i < foundPath.size(); i++) {
          if (i != foundPath.size() - 1) {
            RenderMultipleLines.renderMultipleLines(foundPath.get(i), foundPath.get(i + 1), true);
          }
        }

        foundPath.remove(0);

        ArmadilloStates.offlineState = "online";
        SendChat.chat(String.valueOf(foundPath.size()));
        WalkOnPath.walkOnPath(foundPath);
      } else {
        SendChat.chat("Path has not been found.");
      }
      /*
            HashSet<BlockPos> testSet = new HashSet<BlockPos>();
            testSet.add(new BlockPos(1, 2, 54));

            SendChat.chat(String.valueOf(testSet.contains(new BlockPos(1, 2, 54))));
    } else {
      WalkOnPath.stopWalking();
    }*/
  }
}
