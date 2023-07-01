package com.dillo.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class getCobbleBlock {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static boolean getCobbleBlock() {
    List<BlockPos> blocks = new ArrayList<>();
    boolean checks = false;
    int radius = 0;
    int x = (int) mc.thePlayer.posX;
    int y = (int) mc.thePlayer.posY;
    int z = (int) mc.thePlayer.posZ;
    for (int i = x - radius; i <= x + radius; i++) {
      for (int j = y - 2; j <= y; j++) {
        for (int k = z - radius; k <= z + radius; k++) {
          BlockPos pos = new BlockPos(i, j, k);
          IBlockState block = mc.theWorld.getBlockState(pos);

          if (block.getBlock() == Blocks.cobblestone) {
            checks = true;
          }
        }
      }
    }
    return checks;
  }
}
