package com.dillo.dilloUtils.FailSafes.RestartMacroUtils;

import com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos;
import com.dillo.dilloUtils.BlockUtils.GetUnobstructedPosFromCustom;
import net.minecraft.util.BlockPos;

public class IsObstructed {

  public static boolean isObstructed(BlockPos block, BlockPos block1) {
    return GetUnobstructedPosFromCustom.getUnobstructedPos(block1, block) == null;
  }
}
