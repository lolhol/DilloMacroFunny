package com.dillo.pathfinding.mit.finder.utils;

import com.dillo.utils.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Utils {

  public static BlockNodeClass getClassOfStarting(BlockPos startingBlock, BlockPos endBlock) {
    return new BlockNodeClass(
      null,
      startingBlock,
      endBlock,
      startingBlock,
      Costs.calculateGCostBlockPos(startingBlock, startingBlock),
      Costs.calculateHCostBlockPos(startingBlock, endBlock),
      Costs.calculateSurroundingsDoubleCost(startingBlock),
      Costs.getBreakCost(startingBlock),
      Costs.walkCost(),
      Costs.getFullCost(startingBlock, startingBlock, endBlock),
      BlockUtils.getBlockType(startingBlock)
    );
  }

  public static BlockNodeClass getClassOfEnding(BlockPos startingBlock, BlockPos endBlock) {
    return new BlockNodeClass(
      null,
      endBlock,
      endBlock,
      startingBlock,
      Costs.calculateGCostBlockPos(startingBlock, endBlock),
      Costs.calculateHCostBlockPos(endBlock, endBlock),
      Costs.calculateSurroundingsDoubleCost(endBlock),
      Costs.getBreakCost(endBlock),
      Costs.walkCost(),
      Costs.getFullCost(endBlock, startingBlock, endBlock),
      BlockUtils.getBlockType(endBlock)
    );
  }

  public static BlockNodeClass getClassOfBlock(
    BlockPos block,
    BlockNodeClass parent,
    BlockPos starting,
    BlockPos ending
  ) {
    return new BlockNodeClass(
      parent,
      block,
      ending,
      starting,
      Costs.calculateGCostBlockPos(block, starting),
      Costs.calculateHCostBlockPos(block, ending),
      Costs.calculateSurroundingsDoubleCost(block),
      Costs.getBreakCost(block),
      Costs.walkCost(),
      Costs.getFullCost(block, starting, ending),
      BlockUtils.getBlockType(block)
    );
  }

  public static List<BlockNodeClass> getBlocksAround(BlockNodeClass reference) {
    List<BlockNodeClass> returnBlocks = new ArrayList<>();

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos curBlock = BlockUtils.makeNewBlock(x, y, z, reference.blockPos);
          returnBlocks.add(getClassOfBlock(curBlock, reference, reference.startBlock, reference.finalBlock));
          /*curBlock = BlockUtils.makeNewBlock(x, 0, z, reference.blockPos);
          returnBlocks.add(getClassOfBlock(curBlock, reference, reference.startBlock, reference.finalBlock));*/

          /*int i = 1;

          while (BlockUtils.getBlockType(curBlock) == Blocks.air && i <= 4) {
            curBlock = BlockUtils.makeNewBlock(x, -i, z, reference.blockPos);
            returnBlocks.add(getClassOfBlock(curBlock, reference, reference.startBlock, reference.finalBlock));
            i++;
          }*/
        }
      }
    }

    return returnBlocks;
  }

  public static List<BlockPos> retracePath(BlockNodeClass startNode, BlockNodeClass endNode) {
    List<BlockPos> blockPath = new ArrayList<BlockPos>();
    BlockNodeClass currentNode = endNode;

    while (currentNode.parentOfBlock != null && !currentNode.equals(startNode)) {
      //SendChat.chat("!");
      blockPath.add(currentNode.blockPos());
      currentNode = currentNode.parentOfBlock;
    }

    return reverseList(blockPath);
  }

  public static List<BlockPos> reverseList(List<BlockPos> initList) {
    int len = initList.size();
    if (len == 0) return null;

    int len2 = len >> 1;
    BlockPos temp;

    for (int i = 0; i < len2; ++i) {
      temp = initList.get(i);
      initList.set(i, initList.get(initList.size() - i - 1));
      initList.set(initList.size() - i - 1, temp);
    }

    return initList;
  }

  public static boolean isAbleToInteract(BlockPos block, BlockPos parentBlock, boolean isMine) {
    if (isMine) {
      return BlockUtils.getBlock(block) != Blocks.bedrock && BlockUtils.isBlockSolid(block);
    }

    return canWalkOn(block) || canJumpOn(block, parentBlock) || canFall(block, parentBlock);
  }

  public static boolean canWalkOn(BlockPos block) {
    if (
      BlockUtils.getBlock(block) == Blocks.air &&
      BlockUtils.getBlock(BlockUtils.makeNewBlock(0, -1, 0, block)) != Blocks.air &&
      BlockUtils.getBlock(BlockUtils.makeNewBlock(0, 1, 0, block)) == Blocks.air
    ) {
      return BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block));
    }

    return false;
  }

  public static boolean canJumpOn(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();

    // TODO: Modify here to adjust sb player jump height
    return yDiff > 0 && yDiff < 1 && BlockUtils.isBlockSolid(block);
  }

  public static boolean canFall(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();

    // TODO: Modify here to adjust for the player takin no dmg from fall in SB.
    return yDiff < 0 && yDiff > -4 && BlockUtils.isBlockSolid(block);
  }
}
