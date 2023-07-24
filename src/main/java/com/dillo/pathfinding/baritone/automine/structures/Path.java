package com.dillo.pathfinding.baritone.automine.structures;

import com.dillo.pathfinding.baritone.automine.calculations.behaviour.PathMode;
import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Path {

  @Getter
  LinkedList<BlockNode> blocksInPath;

  @Getter
  PathMode mode;
}
