package com.dillo.pathfinding.mit.finder.utils;

import java.util.Comparator;

public class BlockNodeCompare implements Comparator<BlockNodeClass> {

  @Override
  public int compare(BlockNodeClass one, BlockNodeClass two) {
    int totalCostComparison = Double.compare(one.totalCost, two.totalCost);

    if (totalCostComparison != 0) {
      return totalCostComparison;
    } else {
      return Double.compare(one.hCost, two.hCost);
    }
  }
}
