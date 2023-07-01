package com.dillo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class getAnglePlayerToBlock {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static double getAnglePlayerToBlock(BlockPos block) {
    double sideX = block.getX() + 0.5 - mc.thePlayer.posX;
    double sideZ = block.getZ() + 0.5 - mc.thePlayer.posZ;

    if (sideX == 0) {
      return 0.124465;
    }

    double tan = sideX / sideX;
    double anglePlayerToBlock = radToDegree.radToDegree(Math.atan(tan));

    if (sideX < 0) {
      anglePlayerToBlock += 180;
    }

    return anglePlayerToBlock;
  }
}
