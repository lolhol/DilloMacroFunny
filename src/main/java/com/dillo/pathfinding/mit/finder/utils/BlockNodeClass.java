package com.dillo.pathfinding.mit.finder.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class BlockNodeClass {

  public BlockNodeClass parentOfBlock;
  public BlockPos blockPos;

  // Info

  public BlockPos finalBlock;
  public BlockPos startBlock;

  // Costs

  public double gCost;
  public double hCost;
  public double surroundings;
  public double breakCost;
  public double walkCost;

  public double totalCost;

  // Other

  public Block blockType;

  public BlockPos blockPos() {
    return this.blockPos;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof BlockNodeClass) {
      return this.blockPos.equals(((BlockNodeClass) other).blockPos);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.blockPos.hashCode();
  }

  public Boolean isSame(BlockPos block) {
    return this.blockPos.equals(block);
  }
}
