package com.dillo.pathfinding.baritone.automine.config;

import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PathFindSetting {

  @Getter
  boolean mineWithPreference;

  @Getter
  PathMode pathMode;

  @Getter
  boolean findWithBlockPos;
}
