package com.dillo.Pathfinding.baritone.automine.config;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;

@AllArgsConstructor
public class BaritoneConfig {

  @Getter
  MiningType mineType;

  @Getter
  boolean shiftWhenMine;

  @Getter
  boolean mineFloor;

  @Getter
  boolean mineWithPreference;

  @Getter
  int rotationTime;

  @Getter
  int restartTimeThreshold;

  @Getter
  List<Block> forbiddenPathfindingBlocks;

  @Getter
  List<Block> allowedPathfindingBlocks;

  @Getter
  int maxY;

  @Getter
  int minY;
}
