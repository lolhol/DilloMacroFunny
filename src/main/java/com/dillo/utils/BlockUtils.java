package com.dillo.utils;

import static com.dillo.config.config.isIncludeMithril;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.IsSameBlock;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class BlockUtils {

  public static Vec3 fromBlockPosToVec3(BlockPos block) {
    return new Vec3(block.getX(), block.getY(), block.getZ());
  }

  public static BlockPos fromVec3ToBlockPos(Vec3 block) {
    return new BlockPos(block.xCoord, block.yCoord, block.zCoord);
  }

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

  public static int getPrevBlockPosition(BlockPos curBlock) {
    for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
      BlockPos block = currentRoute.currentRoute.get(i);

      if (curBlock.equals(block)) {
        int prev = 0;

        if (i == 0) {
          prev = currentRoute.currentRoute.size() - 1;
        } else {
          prev = i - 1;
        }

        return prev;
      }
    }

    return -1;
  }

  public static BlockPos getPrevBlockPos(BlockPos curBlock) {
    int result = getPrevBlockPosition(curBlock);
    return result == -1 ? null : currentRoute.currentRoute.get(result);
  }

  public static boolean isOnBlockInRoute(BlockPos curBlock) {
    for (BlockPos block : currentRoute.currentRoute) {
      if (
        Math.abs(block.getZ() - curBlock.getZ()) < 0.001 && Math.abs(block.getX() - curBlock.getX()) < 0.001
      ) return true;
    }

    return false;
  }

  public static List<BlockPos> getBlocksLayer(BlockPos refrenceBlock) {
    List<BlockPos> blocks = new ArrayList<BlockPos>();

    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        BlockPos block = makeNewBlock(x, 0, z, refrenceBlock);

        if (!isIncludeMithril) {
          if (
            (
              ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane ||
              ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass
            ) &&
            !isSameBlock(block, refrenceBlock)
          ) {
            blocks.add(block);
          }
        } else {
          if (
            (
              ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.prismarine ||
              ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.wool
            ) &&
            !isSameBlock(block, refrenceBlock)
          ) {
            blocks.add(block);
          }
        }
      }
    }

    //SendChat.chat(String.valueOf(blocks.size()));
    return blocks;
  }

  public static BlockPos checkIfOnBlock() {
    List<BlockPos> blocks = currentRoute.currentRoute;

    for (int i = 0; i < blocks.size(); i++) {
      //SendChat.chat(String.valueOf(Math.abs(blocks.get(i).getX() - ids.mc.thePlayer.posX + 0.5)));

      if (
        Math.abs(blocks.get(i).getX() - ids.mc.thePlayer.posX + 0.5) <= 0.0001 &&
        Math.abs(blocks.get(i).getZ() - ids.mc.thePlayer.posZ + 0.5) <= 0.0001 &&
        Math.abs(blocks.get(i).getY() - ids.mc.thePlayer.posY + 1) <= 0.0001
      ) {
        return blocks.get(i);
      }
    }

    return null;
  }

  private static boolean isSameBlock(BlockPos refrenceBlock, BlockPos block) {
    if (block.getX() == refrenceBlock.getX() && block.getZ() == refrenceBlock.getZ()) {
      return true;
    }

    return false;
  }

  private static BlockPos getConnectedBlock(BlockPos refrenceBlock, List<BlockPos> blocks) {
    for (BlockPos block : blocks) {
      if (block != refrenceBlock && areConnectedSide(block, refrenceBlock)) {
        return block;
      }
    }

    return null;
  }

  private static BlockPos getConnectedBlockY(BlockPos refrenceBlock, List<BlockPos> blocks) {
    for (BlockPos block : blocks) {
      if (block != refrenceBlock && areConnectedY(block, refrenceBlock)) {
        return block;
      }
    }

    return null;
  }

  private static boolean areConnectedSideXZ(BlockPos block1, BlockPos block2) {
    int dx = Math.abs(block1.getX() - block2.getX());
    int dz = Math.abs(block1.getZ() - block2.getZ());

    return dx <= 1 && dz <= 1;
  }

  private static boolean areConnectedSide(BlockPos block1, BlockPos block2) {
    int dx = Math.abs(block1.getX() - block2.getX());
    int dz = Math.abs(block1.getZ() - block2.getZ());

    return (dx == 0 && dz <= 1) || (dx <= 1 && dz == 0);
  }

  private static boolean areConnectedY(BlockPos block1, BlockPos block2) {
    int dx = Math.abs(block1.getX() - block2.getX());
    int dy = Math.abs(block1.getY() - block2.getY());
    int dz = Math.abs(block1.getZ() - block2.getZ());

    return ((dx == 0 && dz <= 1) || (dx <= 1 && dz == 0)) && dy <= 1;
  }

  private static BlockPos getConnectingY(List<BlockPos> blocks, BlockPos block) {
    for (int i = 0; i < blocks.size(); i++) {
      if (areConnectedY(blocks.get(i), block)) {
        return blocks.get(i);
      }
    }

    return null;
  }

  public static BlockPos makeNewBlock(double addx, double addy, double addz, BlockPos prevBlock) {
    return new BlockPos(prevBlock.getX() + addx, prevBlock.getY() + addy, prevBlock.getZ() + addz);
  }

  public static Block getBlock(BlockPos blockPos) {
    return ids.mc.theWorld.getBlockState(blockPos).getBlock();
  }

  public static boolean areAllLoaded(List<BlockPos> blocksInRoute) {
    for (BlockPos block : blocksInRoute) {
      BlockPos pos = block;

      if (!ids.mc.theWorld.getChunkFromBlockCoords(block).isLoaded()) {
        return false;
      }
    }

    return true;
  }
}
