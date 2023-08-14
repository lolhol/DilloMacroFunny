package com.dillo.pathfinding.mit.finder.utils;

import com.dillo.utils.BlockUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

  public static ReturnClass isAbleToInteract(BlockPos block, BlockPos parentBlock, boolean isMine) {
    List<BlockPos> removeBlocksWalk = canWalkOn(block, parentBlock);

    if (removeBlocksWalk != null && removeBlocksWalk.isEmpty()) {
      return new ReturnClass(removeBlocksWalk, ActionTypes.WALK);
    }

    List<BlockPos> removeBlocksJump = canJumpOn(block, parentBlock);
    if (removeBlocksJump != null && removeBlocksJump.isEmpty()) {
      return new ReturnClass(removeBlocksJump, ActionTypes.JUMP);
    }

    List<BlockPos> removeBlocksFall = canFall(block, parentBlock);
    if (removeBlocksFall != null && removeBlocksFall.isEmpty()) {
      return new ReturnClass(removeBlocksFall, ActionTypes.FALL);
    }

    if (isMine) {
      List<List<BlockPos>> combined = new ArrayList<>();

      if (removeBlocksWalk != null) combined.add(removeBlocksWalk);
      if (removeBlocksJump != null) combined.add(removeBlocksJump);
      if (removeBlocksFall != null) combined.add(removeBlocksFall);

      List<BlockPos> min = null;

      if (!combined.isEmpty()) {
        min = getTheMinList(combined);
      }

      if (min == null) return null;

      return new ReturnClass(removeBlocksFall, ActionTypes.BREAK);
    }

    return null;
  }

  private static List<BlockPos> getTheMinList(List<List<BlockPos>> lists) {
    return lists.stream().min(Comparator.comparingInt(List::size)).orElse(null);
  }

  @Getter
  @AllArgsConstructor
  public static class ReturnClass {

    public List<BlockPos> blocksToBreak;
    public ActionTypes actionType;
  }

  public static List<BlockPos> canWalkOn(BlockPos block, BlockPos parent) {
    double yDif = Math.abs(block.getY() - parent.getY());
    List<BlockPos> blocksToBeRemoved = new ArrayList<>();

    if (BlockUtils.isBlockSolid(block)) blocksToBeRemoved.add(block);

    BlockPos block1 = BlockUtils.makeNewBlock(0, 1, 0, block);
    if (BlockUtils.isBlockSolid(block1)) blocksToBeRemoved.add(block1);

    return (
      yDif <= 0.001 && BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) ? blocksToBeRemoved : null
    );
  }

  public static List<BlockPos> canJumpOn(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();
    List<BlockPos> blocksToBeRemoved = new ArrayList<>();

    if (BlockUtils.isBlockSolid(block)) blocksToBeRemoved.add(block);

    BlockPos block1 = BlockUtils.makeNewBlock(0, 1, 0, block);
    if (BlockUtils.isBlockSolid(block1)) blocksToBeRemoved.add(block1);

    BlockPos block2 = BlockUtils.makeNewBlock(0, 1, 0, parentBlock);
    if (BlockUtils.isBlockSolid(block2)) blocksToBeRemoved.add(block2);

    return (yDiff == 1 && BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) ? blocksToBeRemoved : null);
  }

  public static List<BlockPos> canFall(BlockPos block, BlockPos parentBlock) {
    double yDiff = block.getY() - parentBlock.getY();

    List<BlockPos> blocksToBeRemoved = new ArrayList<>();

    if (BlockUtils.isBlockSolid(block)) blocksToBeRemoved.add(block);

    BlockPos block1 = new BlockPos(block.getX(), parentBlock.getY() + 1, block.getZ());
    if (BlockUtils.isBlockSolid(block1)) blocksToBeRemoved.add(block1);

    return (
      yDiff < 0 &&
        yDiff > -4 &&
        BlockUtils.isBlockSolid(BlockUtils.makeNewBlock(0, -1, 0, block)) &&
        isAllClearToY(block.getY(), parentBlock.getY(), block)
        ? blocksToBeRemoved
        : null
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
