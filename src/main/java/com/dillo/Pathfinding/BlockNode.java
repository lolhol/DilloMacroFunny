package com.dillo.Pathfinding;

import net.minecraft.util.BlockPos;

public class BlockNode {

  public BlockPos blockPos = null;
  public double gCost = 0.0;
  public double hCost = 0.0;
  public double totalCost = 0.0;
  public BlockNode parentOfBlock = null;

  public BlockNode(BlockPos pos, double g, double h, BlockNode parent) {
    blockPos = pos;
    parentOfBlock = parent;
    gCost = g;
    hCost = h;
    totalCost = g + h;
  }

  public BlockNode getParent() {
    return parentOfBlock;
  }

  public double gCost() {
    return gCost;
  }

  public double hCost() {
    return hCost;
  }

  public double totalCost() {
    return totalCost;
  }

  public BlockPos blockPos() {
    return blockPos;
  }

  public boolean equals(BlockNode other) {
    return blockPos.equals(other.blockPos);
  }

  public int hashCode() {
    return blockPos.hashCode();
  }
}
