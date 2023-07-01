package com.dillo.utils;

import com.dillo.utils.previous.random.ids;

public class DistFromXPlayer {

  public static double distFromXPlayer(double x, double y, double z) {
    double dX = ids.mc.thePlayer.posX - x;
    double dZ = ids.mc.thePlayer.posZ - z;
    double dY = ids.mc.thePlayer.posY - y;
    double dis = Math.sqrt(dX * dX + dZ * dZ);
    return Math.sqrt(dis * dis + dY * dY);
  }
}
