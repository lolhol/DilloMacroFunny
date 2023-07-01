package com.dillo.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class getBlockFromAngle {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static BlockPos getBlockFromAngle(double angle, double dist) {
    int playerX = mc.thePlayer.getPosition().getX();
    int playerZ = mc.thePlayer.getPosition().getZ();

    double dx = dist * Math.cos(degreeToRad.degreeToRad(angle));
    double dz = dist * Math.sin(degreeToRad.degreeToRad(angle));

    BlockPos pos = new BlockPos(playerX + dx, mc.thePlayer.posY, playerZ + dz);
    IBlockState blockState = mc.theWorld.getBlockState(pos);

    if (blockState.getBlock() == Blocks.stained_glass || blockState.getBlock() == Blocks.stained_glass_pane) {
      return pos;
    }

    return null;
  }
}
