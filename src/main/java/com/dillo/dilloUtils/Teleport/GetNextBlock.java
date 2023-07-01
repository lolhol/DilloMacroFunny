package com.dillo.dilloUtils.Teleport;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.random.IsSameBlock;
import net.minecraft.util.BlockPos;

public class GetNextBlock {

  public static BlockPos getNextBlock() {
    for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
      if (IsSameBlock.isSameBlock(currentRoute.currentBlock, currentRoute.currentRoute.get(i))) {
        if (i == currentRoute.currentRoute.size() - 1) {
          return currentRoute.currentRoute.get(0);
        } else {
          return currentRoute.currentRoute.get(i + 1);
        }
      }
    }

    return null;
  }
}
