package com.dillo.pathfinding.mit.finder.utils;

import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;

public class Costs {

  public static double calculateGCost(BlockNodeClass nodeClass) {
    return DistanceFromTo.distanceFromTo(nodeClass.blockPos, nodeClass.startBlock);
  }

  public static double calculateHCost(BlockNodeClass nodeClass) {
    return DistanceFromTo.distanceFromTo(nodeClass.blockPos, nodeClass.finalBlock);
  }

  public static double calculateFullCostDistance(BlockNodeClass nodeClass) {
    return calculateGCost(nodeClass) + calculateHCost(nodeClass);
  }

  public static double calculateGCostBlockPos(BlockPos pos1, BlockPos startBlock) {
    return DistanceFromTo.distanceFromTo(pos1, startBlock);
  }

  public static double calculateHCostBlockPos(BlockPos pos1, BlockPos finalBlock) {
    return DistanceFromTo.distanceFromTo(pos1, finalBlock);
  }

  public static double calculateFullCostDistance(BlockPos pos1, BlockPos startBlock, BlockPos finalBlock) {
    return calculateGCostBlockPos(pos1, startBlock) + calculateHCostBlockPos(pos1, finalBlock);
  }

  public static double getFullCost(BlockPos pos1, BlockPos startBlock, BlockPos finalBlock) {
    return (
      calculateGCostBlockPos(pos1, startBlock) + calculateHCostBlockPos(pos1, finalBlock)
      /*calculateSurroundingsDoubleCost(pos1) +
      getBreakCost(pos1) +
      walkCost()*/
    );
  }

  public static double getActionCost(ActionTypes action) {
    switch (action) {
      case WALK:
        return 1;
      case JUMP:
        return 2;
      case FALL:
        return 5;
      case BREAK:
        return 4;
    }

    return 0;
  }

  public static double calculateSurroundingsDoubleCost(BlockPos block) {
    Iterable<BlockPos> blocks = BlockPos.getAllInBox(
      BlockUtils.makeNewBlock(block.getX() - 4, block.getY(), block.getZ() - 4, block),
      BlockUtils.makeNewBlock(block.getX() + 4, block.getY() + 1, block.getZ() + 4, block)
    );
    double percent = BlockUtils.getPercentOfNonAir(blocks);
    return percent / 50;
  }

  public static double getBreakCost(BlockPos block) {
    return BlockUtils.getBlockType(block).getBlockHardness(ids.mc.theWorld, block) * 5;
  }

  public static double walkCost() {
    return 1;
  }
}
