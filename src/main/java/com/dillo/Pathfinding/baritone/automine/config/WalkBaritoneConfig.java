package com.dillo.Pathfinding.baritone.automine.config;

import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockUtils;

public class WalkBaritoneConfig extends BaritoneConfig {

  public WalkBaritoneConfig(int minY, int maxY, int restartTimeThreshold) {
    super(MiningType.NONE, false, false, false, 200, restartTimeThreshold, null, BlockUtils.walkables, maxY, minY);
  }
}
