package com.dillo.utils.previous.checks;

import com.dillo.utils.previous.random.PlayerUtil;
import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class canReach {

  public static boolean canReachBlock(BlockPos pos, float range) {
    AxisAlignedBB aabb = getBlockAABB(pos);
    return isInterceptable(PlayerUtil.getPositionEyes(), getMiddleOfAABB(aabb), aabb, range);
  }

  public static AxisAlignedBB getBlockAABB(BlockPos pos) {
    Block block = ids.mc.theWorld.getBlockState(pos).getBlock();
    block.setBlockBoundsBasedOnState((IBlockAccess) ids.mc.theWorld, pos);
    return block.getSelectedBoundingBox((World) ids.mc.theWorld, pos);
  }

  public static boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb, float range) {
    if (start.squareDistanceTo(goal) > (range * range)) return false;
    return isInterceptable(start, goal, aabb);
  }

  public static boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb) {
    return (
      isVecInYZ(start.getIntermediateWithXValue(goal, aabb.minX), aabb) ||
      isVecInYZ(start.getIntermediateWithXValue(goal, aabb.maxX), aabb) ||
      isVecInXZ(start.getIntermediateWithYValue(goal, aabb.minY), aabb) ||
      isVecInXZ(start.getIntermediateWithYValue(goal, aabb.maxY), aabb) ||
      isVecInXY(start.getIntermediateWithZValue(goal, aabb.minZ), aabb) ||
      isVecInXY(start.getIntermediateWithZValue(goal, aabb.maxZ), aabb)
    );
  }

  public static boolean isVecInYZ(Vec3 vec, AxisAlignedBB aabb) {
    return (
      vec != null &&
      vec.yCoord >= aabb.minY &&
      vec.yCoord <= aabb.maxY &&
      vec.zCoord >= aabb.minZ &&
      vec.zCoord <= aabb.maxZ
    );
  }

  public static boolean isVecInXY(Vec3 vec, AxisAlignedBB aabb) {
    return (
      vec != null &&
      vec.xCoord >= aabb.minX &&
      vec.xCoord <= aabb.maxX &&
      vec.yCoord >= aabb.minY &&
      vec.yCoord <= aabb.maxY
    );
  }

  public static boolean isVecInXZ(Vec3 vec, AxisAlignedBB aabb) {
    return (
      vec != null &&
      vec.xCoord >= aabb.minX &&
      vec.xCoord <= aabb.maxX &&
      vec.zCoord >= aabb.minZ &&
      vec.zCoord <= aabb.maxZ
    );
  }

  public static Vec3 getMiddleOfAABB(AxisAlignedBB aabb) {
    return new Vec3((aabb.maxX + aabb.minX) / 2.0D, (aabb.maxY + aabb.minY) / 2.0D, (aabb.maxZ + aabb.minZ) / 2.0D);
  }
}
