package com.dillo.pathfinding.mit.finder.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class PathFinderConfig {

  public boolean isMine;
  public boolean isSwim;
  public boolean isWidePath;
  public boolean walkableBlocks;
  public boolean isInNonRenderedChunks;

  public int widePathWidth;
  public int maxIterations;
  public int maxPathSize;

  public BlockPos startingBlock;
  public BlockPos destinationBlock;

  public Block[] availableBlockTypes;
  public Block[] mineableBlocks;

  public int maxHeight;
  public int maxDepth;
}
