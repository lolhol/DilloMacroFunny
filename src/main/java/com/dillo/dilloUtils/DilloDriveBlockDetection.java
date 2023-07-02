package com.dillo.dilloUtils;

import static com.dillo.data.config.isIncludeMithril;

import com.dillo.dilloUtils.BlockUtils.FromBlockToHP;
import com.dillo.utils.GetAngleToBlock;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class DilloDriveBlockDetection {

  @Getter
  @AllArgsConstructor
  public static class BlockAngle {

    public float angle = 0;
    public BlockPos blockPos;
  }

  public static List<BlockAngle> detectBlocks() {
    BlockPos refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ);
    List<BlockPos> returnList = getBlocksLayer(refrenceBlock2);
    if (returnList.size() < 1) {
      refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ);
      returnList = getBlocksLayer(refrenceBlock2);

      if (returnList.size() < 1) {
        refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 3, ids.mc.thePlayer.posZ);
        returnList = getBlocksLayer(refrenceBlock2);
      }
    }

    if (returnList.size() > 0) {
      List<BlockAngle> returnAngles = new ArrayList<BlockAngle>();

      //LookAt.smoothLook(LookAt.getRotation(returnList.get(0)), 49);

      for (BlockPos block : returnList) {
        float angle = GetAngleToBlock.calcAngle(block);

        BlockAngle blockAngle = new BlockAngle(angle, block);
        returnAngles.add(blockAngle);
      }

      returnAngles.sort((a, b) -> {
        return a.angle < b.angle ? -1 : 1;
      });

      return returnAngles;
    }

    return null;
  }

  private static boolean isSimilar(List<BlockAngle> blocks) {
    float midAngle = blocks.get(0).angle;
    int count = 0;

    for (BlockAngle block : blocks) {
      if (block.angle - 1 < midAngle && block.angle + 1 > midAngle) {
        count++;
      }
    }

    if (count >= blocks.size() - 1) {
      return true;
    }

    return false;
  }

  public static List<BlockPos> getBlocksLayer(BlockPos refrenceBlock) {
    List<BlockPos> blocks = new ArrayList<BlockPos>();

    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        BlockPos block = MoreLegitSpinDrive.makeNewBlock(x, 0, z, refrenceBlock);

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

    SendChat.chat(String.valueOf(blocks.size()));
    return blocks;
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

  private static BlockPos getConnectingXZ(List<BlockPos> blocks, BlockPos block) {
    for (int i = 0; i < blocks.size(); i++) {
      if (areConnectedSideXZ(blocks.get(i), block)) {
        return blocks.get(i);
      }
    }

    return null;
  }

  private static BlockPos getConnecting(List<BlockPos> blocks, BlockPos block) {
    for (int i = 0; i < blocks.size(); i++) {
      if (areConnectedSide(blocks.get(i), block)) {
        return blocks.get(i);
      }
    }

    return null;
  }

  private static float getClosestToPlayer(List<BlockPos> blocks) {
    float starter = FromBlockToHP.getYaw(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());

    for (BlockPos block : blocks) {
      if (FromBlockToHP.getYaw(block.getX(), block.getY(), block.getZ()) < starter) {
        starter = FromBlockToHP.getYaw(block.getX(), block.getY(), block.getZ());
      }
    }

    return starter;
  }

  private static BlockPos getClosestBlock(List<BlockPos> blocks) {
    float starter = FromBlockToHP.getYaw(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());
    BlockPos starterBlock = blocks.get(0);

    for (BlockPos block : blocks) {
      if (FromBlockToHP.getYaw(block.getX(), block.getY(), block.getZ()) < starter) {
        starter = FromBlockToHP.getYaw(block.getX(), block.getY(), block.getZ());
        starterBlock = block;
      }
    }

    return starterBlock;
  }
}
