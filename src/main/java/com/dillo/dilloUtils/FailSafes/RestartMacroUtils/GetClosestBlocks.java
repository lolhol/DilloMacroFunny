package com.dillo.dilloUtils.FailSafes.RestartMacroUtils;

import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;

public class GetClosestBlocks {

  public static List<BlockPos> getClosestBlocks(List<BlockPos> initialList, double maxDist) {
    BlockPos playerPosition = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
    List<BlockPos> closestBlocks = new ArrayList<BlockPos>();

    for (BlockPos blockPos : initialList) {
      if (DistanceFromTo.distanceFromTo(blockPos, playerPosition) <= maxDist) {
        closestBlocks.add(blockPos);
      }
    }

    return closestBlocks;
  }
}
