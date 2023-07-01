package com.dillo.utils;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class BlockUtils {

  public static Vec3 fromBlockPosToVec3(BlockPos block) {
    return new Vec3(block.getX(), block.getY(), block.getZ());
  }

  public static BlockPos fromVec3ToBlockPos(Vec3 block) {
    return new BlockPos(block.xCoord, block.yCoord, block.zCoord);
  }
}
