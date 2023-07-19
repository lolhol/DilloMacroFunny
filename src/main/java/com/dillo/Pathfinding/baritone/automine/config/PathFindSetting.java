package com.dillo.Pathfinding.baritone.automine.config;

import com.dillo.Pathfinding.baritone.automine.calculations.behaviour.PathMode;
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
