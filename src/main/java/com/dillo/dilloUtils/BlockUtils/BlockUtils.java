package com.dillo.dilloUtils.BlockUtils;

import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class BlockUtils {

  public static BlockPos blockCheck;
  public static List<BlockPos> alreadyBroken = new ArrayList<BlockPos>();

  public static ArrayList<BlockPos> getNearestBlocks() {
    int radius = 1;
    ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
    for (int i = -radius; i <= radius; i++) {
      for (int y = -3; y <= 5; y++) {
        for (int z = -radius; z <= radius; z++) {
          IBlockState block = ids.mc.theWorld.getBlockState(
            new BlockPos(ids.mc.thePlayer.posX + i, ids.mc.thePlayer.posY + y, ids.mc.thePlayer.posZ + z)
          );

          if (block.getBlock() == Blocks.stained_glass_pane || block.getBlock() == Blocks.stained_glass) {
            blockPosList.add(
              new BlockPos(ids.mc.thePlayer.posX + i, ids.mc.thePlayer.posY + y, ids.mc.thePlayer.posZ + z)
            );
          }
        }
      }
    }

    return blockPosList;
  }

  public static ArrayList<BlockPos> getCobbleBlockUnder() {
    int radius = 1;
    ArrayList<BlockPos> blockUnderList = new ArrayList<BlockPos>();

    for (int i = -radius; i <= radius; i++) {
      for (int y = -7; y <= 0; y++) {
        for (int z = -radius; z <= radius; z++) {
          IBlockState block = ids.mc.theWorld.getBlockState(new BlockPos(i, y, z));

          if (block.getBlock() == Blocks.cobblestone) {
            blockUnderList.add(new BlockPos(i, y, z));
          }
        }
      }
    }

    return blockUnderList;
  }

  public static ArrayList<BlockPos> mainBlockChecks() {
    ArrayList<BlockPos> blockReturnList = getNearestBlocks();
    ArrayList<BlockPos> outList = new ArrayList<>(blockReturnList);

    for (BlockPos block : blockReturnList) {
      //Checks if the Y pos of block is not under player.
      if (block.getY() <= currentRoute.currentBlock.getY() + 0.2) {
        outList.remove(block);
      }

      //Ignores blocks that are not full blocks.
      if (config.ignorePanes) {
        Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

        if (blockType.toString().contains("pane")) {
          outList.remove(block);
        }
      }

      if (config.blockBreakSkip) {
        if (alreadyBroken.contains(block)) {
          outList.remove(block);
        }
      }
    }

    if (blockReturnList.size() <= config.cutOff) {
      return null;
    }

    return outList;
  }
}
