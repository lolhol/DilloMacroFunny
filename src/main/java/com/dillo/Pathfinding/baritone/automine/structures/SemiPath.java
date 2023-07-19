package com.dillo.Pathfinding.baritone.automine.structures;

import com.dillo.Pathfinding.baritone.automine.calculations.behaviour.PathMode;
import java.util.LinkedList;
import lombok.AllArgsConstructor;

public class SemiPath extends Path {

  public SemiPath(LinkedList<BlockNode> blocksInPath, PathMode mode) {
    super(blocksInPath, mode);
  }
}
