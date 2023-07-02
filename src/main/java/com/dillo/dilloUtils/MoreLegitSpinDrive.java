package com.dillo.dilloUtils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class MoreLegitSpinDrive {

  public static BlockPos makeNewBlock(double addx, double addy, double addz, BlockPos prevBlock) {
    return new BlockPos(prevBlock.getX() + addx, prevBlock.getY() + addy, prevBlock.getZ() + addz);
  }

  public static Block getBlock(BlockPos blockPos) {
    return ids.mc.theWorld.getBlockState(blockPos).getBlock();
  }
}
