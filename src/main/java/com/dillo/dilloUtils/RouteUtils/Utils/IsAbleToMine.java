package com.dillo.dilloUtils.RouteUtils.Utils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class IsAbleToMine {

  public static boolean isAbleToMine(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    if (
      blockType != Blocks.chest &&
      blockType != Blocks.air &&
      blockType != Blocks.obsidian &&
      blockType != Blocks.bedrock &&
      blockType != Blocks.stained_glass &&
      blockType != Blocks.stained_glass_pane &&
      blockType != Blocks.cobblestone &&
      blockType != Blocks.prismarine
    ) {
      return true;
    }

    return false;
  }
}
