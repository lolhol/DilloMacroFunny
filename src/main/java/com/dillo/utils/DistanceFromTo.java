package com.dillo.utils;

import net.minecraft.util.BlockPos;

public class DistanceFromTo {

  public static double distanceFromTo(BlockPos pos1, BlockPos pos2) {
    double d1 = pos1.getX() - pos2.getX();
    double d2 = pos1.getY() - pos2.getY();
    double d3 = pos1.getZ() - pos2.getZ();

    return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
  }
}
