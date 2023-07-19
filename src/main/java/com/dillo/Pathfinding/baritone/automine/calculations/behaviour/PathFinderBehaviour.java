package com.dillo.Pathfinding.baritone.automine.calculations.behaviour;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;

@AllArgsConstructor
public class PathFinderBehaviour {

  @Getter
  List<Block> forbiddenMiningBlocks;

  @Getter
  List<Block> allowedMiningBlocks;

  @Getter
  int maxY;

  @Getter
  int minY;

  @Getter
  int searchRadius;

  @Getter
  boolean staticMode;
}
