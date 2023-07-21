package com.dillo.Pathfinding.stevebot.core.data.modification;

import com.dillo.Pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.Pathfinding.stevebot.core.data.blocks.BlockWrapper;
import com.dillo.Pathfinding.stevebot.core.data.items.wrapper.ItemToolWrapper;

public interface Modification {
  Modification[] EMPTY = new Modification[] {};

  static Modification placeBlock(BaseBlockPos position, BlockWrapper block) {
    return new BlockPlaceModification(position, block);
  }

  static Modification breakBlock(BaseBlockPos position, ItemToolWrapper tool) {
    return new BlockBreakModification(position, tool);
  }

  static Modification breakBlock(BaseBlockPos position) {
    return new BlockBreakModification(position);
  }

  static Modification healthChange(int healthChange) {
    return new HealthChangeModification(healthChange);
  }
}
