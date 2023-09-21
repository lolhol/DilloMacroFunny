package com.dillo.pathfinding.mit.finder.walker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class WalkerConfig {

  public boolean isShift;
  public float distanceToShift;

  BlockPos endBlock;
}
