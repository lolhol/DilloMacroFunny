package com.dillo.pathfinding.mit.finder.utils;

import com.dillo.utils.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
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
      BlockUtils.getBlockType(startingBlock),
      null
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
      BlockUtils.getBlockType(endBlock),
      null
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
      BlockUtils.getBlockType(block),
      null
    );
  }

  public static List<BlockNodeClass> getBlocksAround(BlockNodeClass reference) {
    List<BlockNodeClass> returnBlocks = new ArrayList<>();

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos curBlock = BlockUtils.makeNewBlock(x, y, z, reference.blockPos);
          returnBlocks.add(getClassOfBlock(curBlock, reference, reference.startBlock, reference.finalBlock));

          while (y == -1 && !BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, curBlock))) {
            curBlock = BlockUtils.makeNewBlock(0, -1, 0, curBlock);
            returnBlocks.add(getClassOfBlock(curBlock, reference, reference.startBlock, reference.finalBlock));
          }
        }
      }
    }

    return returnBlocks;
  }

  public static List<BlockNodeClass> retracePath(BlockNodeClass startNode, BlockNodeClass endNode) {
    List<BlockPos> blockPath = new ArrayList<BlockPos>();
    List<BlockNodeClass> nodeClass = new ArrayList<>();
    BlockNodeClass currentNode = endNode;

    while (currentNode.parentOfBlock != null && !currentNode.equals(startNode)) {
      //SendChat.chat("!");

      /*RenderMultipleBlocksMod.renderMultipleBlocks(
        new Vec3(currentNode.blockPos().getX(), currentNode.blockPos().getY(), currentNode.blockPos().getZ()),
        true
      );*/

      blockPath.add(currentNode.blockPos());
      nodeClass.add(currentNode);
      currentNode = currentNode.parentOfBlock;
    }

    return reverseList(nodeClass);
  }

  public static List<BlockNodeClass> reverseList(List<BlockNodeClass> initList) {
    int len = initList.size();
    if (len == 0) return null;

    int len2 = len >> 1;
    BlockNodeClass temp;

    for (int i = 0; i < len2; ++i) {
      temp = initList.get(i);
      initList.set(i, initList.get(initList.size() - i - 1));
      initList.set(initList.size() - i - 1, temp);
    }

    return initList;
  }

  public static ActionTypes isAbleToInteract(BlockPos block, BlockPos parentBlock, boolean isMine) {
    if (canWalkOn(block, parentBlock)) {
      return ActionTypes.WALK;
    }

    if (canJumpOn(block, parentBlock)) {
      return ActionTypes.JUMP;
    }

    if (canFall(block, parentBlock)) {
      return ActionTypes.FALL;
    }

    /*if (isMine && BlockUtils.getBlock(block) != Blocks.bedrock && BlockUtils.isBlockSolid(block)) {
      return ActionTypes.BREAK;
    }*/

    return null;
  }

  public static boolean canWalkOn(BlockPos block, BlockPos parent) {
    double yDif = Math.abs(block.getY() - parent.getY());

    /*if (
      yDif < 0.001
    ) */
    return ( //RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(block), true);
      !BlockUtils.isBlockSolid(block) &&
      BlockUtils.getBlock(BlockUtils.makeNewBlock(0, 1, 0, block)) == Blocks.air &&
      BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) &&
      yDif <= 0.0001
    );
  }

  public static boolean canJumpOn(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();

    // TODO: Modify here to adjust sb player jump height

    /*if (
      yDiff == 1 &&
      !BlockUtils.isBlockSolid(block) &&
      BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) &&
      !BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, 1, 0, block))
    ) {
      RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(block), true);
    }*/

    return (
      yDiff == 1 &&
      !BlockUtils.isBlockSolid(block) &&
      BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) &&
      !BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, 1, 0, block))
    );
  }

  public static boolean canFall(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();

    // TODO: Modify here to adjust for the player takin no dmg from fall in SB.
    return (
      yDiff < 0 &&
      yDiff > -4 &&
      BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) &&
      !BlockUtils.isBlockSolid(block) &&
      isAllClearToY(block.getY(), parentBlock.getY(), block)
    );
  }

  public static boolean isAllClearToY(int y1, int y2, BlockPos block) {
    boolean isGreater = y1 < y2;

    while (y1 != y2) {
      if (isGreater) {
        y1++;
      } else {
        y1--;
      }

      Block curBlock = BlockUtils.getBlock(BlockUtils.makeNewBlock(0, y1, 0, block));

      if (curBlock != Blocks.air) return false;
      curBlock = BlockUtils.getBlock(BlockUtils.makeNewBlock(0, y1, 0, block));
    }

    return true;
  }
}
