package com.dillo.Pathfinding.baritone.automine.utils.HypixelUtils;

import com.dillo.Pathfinding.baritone.automine.utils.LogUtils;
import com.dillo.Pathfinding.baritone.automine.utils.Utils.MathUtils;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;

public class WarpCoordinateUtils {

  public static ArrayList<BlockPos> getRandomEmissaryWarpCoordinates() {
    int routeNumber = MathUtils.randomNum(0, 5);
    LogUtils.debugLog("Route number: " + routeNumber);
    ArrayList<BlockPos> route = new ArrayList<>();
    if (routeNumber == 0) {
      route.add(new BlockPos(3, 165, -12));
      route.add(new BlockPos(35, 145, 1));
      route.add(new BlockPos(39, 134, 22));
    } else if (routeNumber == 1) {
      route.add(new BlockPos(4, 155, -12));
      route.add(new BlockPos(35, 145, 1));
      route.add(new BlockPos(39, 134, 22));
    } else if (routeNumber == 2) {
      route.add(new BlockPos(3, 165, -12));
      route.add(new BlockPos(38, 142, 6));
      route.add(new BlockPos(39, 134, 22));
    } else if (routeNumber == 3) {
      route.add(new BlockPos(4, 155, -12));
      route.add(new BlockPos(38, 142, 6));
      route.add(new BlockPos(39, 134, 22));
    } else if (routeNumber == 4) {
      route.add(new BlockPos(3, 165, -12));
      route.add(new BlockPos(33, 144, -2));
      route.add(new BlockPos(39, 134, 22));
    } else if (routeNumber == 5) {
      route.add(new BlockPos(4, 155, -12));
      route.add(new BlockPos(33, 144, -2));
      route.add(new BlockPos(39, 134, 22));
    }
    return route;
  }
}
