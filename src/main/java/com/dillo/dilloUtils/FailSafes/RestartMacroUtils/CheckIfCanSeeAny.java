package com.dillo.dilloUtils.FailSafes.RestartMacroUtils;

import com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CheckIfCanSeeAny {

  public static List<BlockPos> checkIfCanSeeAny(List<BlockPos> initialBlocks) {
    List<BlockPos> returnList = new ArrayList<BlockPos>();

    for (BlockPos block : initialBlocks) {
      Vec3 questionBlock = GetUnobstructedPos.getUnobstructedPos(block);

      if (questionBlock != null) {
        returnList.add(block);
      }
    }

    return returnList;
  }
}
