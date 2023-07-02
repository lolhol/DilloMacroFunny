package com.dillo.MITGUI.GUIUtils.DilloRouteUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;

public class IsInBlockRange {

  public static boolean isInCheckRange() {
    double curFurthest = 0;

    for (BlockPos block : currentRoute.currentRoute) {
      double dist = DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition());

      if (dist > curFurthest) {
        curFurthest = dist;
      }
    }

    if (curFurthest > 120 || currentRoute.currentRoute.size() < 1) {
      return false;
    }

    return true;
  }
}
