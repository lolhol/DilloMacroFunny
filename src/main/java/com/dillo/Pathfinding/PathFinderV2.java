package com.dillo.Pathfinding;

import static java.lang.Float.isInfinite;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleLines;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PathFinderV2 {

  public static double fallY = 0.0;
  public static BlockNode lastFinalDestination = null;

  public static List<BlockPos> pathFinder(BlockNode endPoint, BlockNode startPoint) {
    double depth = 0;
    lastFinalDestination = endPoint;

    List<BlockNode> openSet = new ArrayList<BlockNode>();
    HashSet<BlockPos> closedSet = new HashSet<BlockPos>();
    BlockNode previousNode = null;
    openSet.add(startPoint);

    while (depth < 100000 && openSet.size() > 0) {
      //hCost ====> distance from end node.
      //gCost ====> distance from start node.
      //fCost ====> gCost + hCost.

      // MAKE ORDERED SET!
      BlockNode node = openSet.get(0);
      for (BlockNode blockNode : openSet) {
        if (blockNode.totalCost() <= node.totalCost() && blockNode.hCost() < node.hCost()) {
          if (!closedSet.contains(blockNode.blockPos())) {
            node = blockNode;
          }
        }
      }

      openSet.remove(node);
      closedSet.add(node.blockPos());

      if (node.equals(endPoint)) {
        endPoint.parentOfBlock = previousNode;
        return retracePath(startPoint, endPoint);
      }

      List<BlockNode> children = getChildren(node, endPoint.blockPos());

      for (BlockNode child : children) {
        if (closedSet.contains(child.blockPos())) {
          continue;
        }

        if (!canJump(child) && !canBeWalkedOn(child) && !canFall(child) && !canBreakBlock(child.blockPos())) {
          continue;
        }

        double newCostToNeighbour = node.gCost() + DistanceFromTo.distanceFromTo(node.blockPos(), child.blockPos());
        if (newCostToNeighbour < child.gCost() || !openSet.contains(child)) {
          child.gCost = newCostToNeighbour;
          //child.gCost = DistanceFromTo.distanceFromTo(child.blockPos(), endPoint.blockPos());
          child.totalCost = child.gCost + child.hCost;
          child.parentOfBlock = node;

          if (!openSet.contains(child)) {
            openSet.add(child);
          }
        }
      }

      previousNode = node;
      depth++;
    }

    return null;
  }

  public static boolean isOnCorner(BlockPos parent, BlockPos child) {
    if (
      DistanceFromTo.distanceFromTo(new BlockPos(1, 1, 1), new BlockPos(1, 1, 2)) <
      DistanceFromTo.distanceFromTo(parent, child)
    ) {
      return true;
    }

    return false;
  }

  public static boolean canBeWalkedOn(BlockNode block) {
    BlockPos parentBlockPos = block.getParent().blockPos();
    BlockPos childBlockPos = block.blockPos();

    Block childBlockAboveType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() + 1, childBlockPos.getZ()))
      .getBlock();
    Block childBlockType = ids.mc.theWorld.getBlockState(childBlockPos).getBlock();
    Block childBlockUnderType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() - 1, childBlockPos.getZ()))
      .getBlock();

    if (
      Math.abs(parentBlockPos.getY() - childBlockPos.getY()) < 0.00001 &&
      childBlockType == Blocks.air &&
      childBlockUnderType != Blocks.air &&
      childBlockAboveType == Blocks.air
    ) {
      return true;
    }

    return false;
  }

  public static boolean canJump(BlockNode block) {
    BlockPos parentBlockPos = block.getParent().blockPos();
    BlockPos childBlockPos = block.blockPos();

    Block childBlockAboveType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() + 1, childBlockPos.getZ()))
      .getBlock();
    Block childBlockType = ids.mc.theWorld.getBlockState(childBlockPos).getBlock();
    Block childBlockUnderType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() - 1, childBlockPos.getZ()))
      .getBlock();
    Block childBlockAbove2Type = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() + 2, childBlockPos.getZ()))
      .getBlock();

    if (
      Math.abs(childBlockPos.getY() - parentBlockPos.getY()) <= 1 &&
      childBlockType == Blocks.air &&
      childBlockAboveType == Blocks.air &&
      childBlockUnderType != Blocks.air &&
      childBlockAbove2Type == Blocks.air
    ) {
      //RenderMultipleBlocksMod.renderMultipleBlocks(new Vec3(block.getX(), block.getY(), block.getZ()), true);
      return true;
    }

    return false;
  }

  public static boolean canFall(BlockNode block) {
    BlockPos parentBlockPos = block.getParent().blockPos();
    BlockPos childBlockPos = block.blockPos();

    Block childBlockType = ids.mc.theWorld.getBlockState(childBlockPos).getBlock();
    Block childAboveBlockType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() + 1, childBlockPos.getZ()))
      .getBlock();
    Block childAbove2BlocksType = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() + 2, childBlockPos.getZ()))
      .getBlock();
    int childParentDrop = Math.abs(parentBlockPos.getY() - childBlockPos.getY());
    Block childBlockBelow = ids.mc.theWorld
      .getBlockState(new BlockPos(childBlockPos.getX(), childBlockPos.getY() - childParentDrop, childBlockPos.getZ()))
      .getBlock();

    if (
      childParentDrop >= 1 &&
      childBlockType == Blocks.air &&
      childAboveBlockType == Blocks.air &&
      childParentDrop <= 4 &&
      childBlockBelow != Blocks.air &&
      childAbove2BlocksType == Blocks.air
    ) {
      RenderMultipleBlocksMod.renderMultipleBlocks(
        new Vec3(childBlockPos.getX(), childBlockPos.getY(), childBlockPos.getZ()),
        true
      );
      return true;
    }

    return false;
  }

  public static boolean canBreakBlock(BlockPos block) {
    //Block parentType = ids.mc.theWorld.getBlockState(parentBlockPos).getBlock();
    Block childType = ids.mc.theWorld.getBlockState(block).getBlock();

    return childType != Blocks.bedrock && childType != Blocks.air;
  }

  public static void restartFinder(BlockNode start) {
    if (start != null) {
      RenderMultipleLines.renderMultipleLines(null, null, false);
      List<BlockPos> foundPath = pathFinder(lastFinalDestination, start);

      if (foundPath != null) {
        for (int i = 0; i < foundPath.size(); i++) {
          if (i != foundPath.size() - 1) {
            RenderMultipleLines.renderMultipleLines(foundPath.get(i), foundPath.get(i + 1), true);
          }
        }

        foundPath.remove(0);

        ArmadilloStates.offlineState = "online";
        SendChat.chat(String.valueOf(foundPath.size()));
        WalkOnPath.walkOnPath(foundPath);
      } else {
        SendChat.chat("Path has not been found.");
      }
    }
  }

  public static List<BlockPos> retracePath(BlockNode startNode, BlockNode endNode) {
    List<BlockPos> blockPath = new ArrayList<BlockPos>();
    BlockNode currentNode = endNode;

    while (currentNode.getParent() != null && !currentNode.equals(startNode)) {
      //SendChat.chat("!");
      //RenderMultipleBlocksMod.renderMultipleBlocks(new Vec3(currentNode.blockPos().getX(), currentNode.blockPos().getY(), currentNode.blockPos().getZ()), true);
      blockPath.add(currentNode.blockPos());
      currentNode = currentNode.getParent();
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

  public static List<BlockNode> getChildren(BlockNode parent, BlockPos destination) {
    List<BlockNode> children = new ArrayList<BlockNode>();

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
          BlockPos block = new BlockPos(
            parent.blockPos().getX() + i,
            parent.blockPos().getY() + k,
            parent.blockPos().getZ() + j
          );
          BlockNode child = new BlockNode(block, getGCost(parent, block), getHCost(block, destination), parent);

          Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

          if (!parent.equals(child)) {
            if (block.getY() > parent.blockPos().getY()) {
              child.totalCost -= 5;
            }

            if (parent.blockPos().getY() > block.getY()) {
              child.totalCost += 5;
            }

            if (blockType == Blocks.water) {
              child.totalCost += 5;
            }

            if (blockType != Blocks.air && !canJump(child)) {
              float blockHardness = ids.mc.theWorld
                .getBlockState(block)
                .getBlock()
                .getBlockHardness(ids.mc.theWorld, block);

              if (!isInfinite(blockHardness)) {
                if (isOnCorner(parent.blockPos(), block)) {
                  if (isInMiddle(block, parent.blockPos())) {
                    if (
                      ids.mc.theWorld
                        .getBlockState(new BlockPos(block.getX(), block.getY() + 1, block.getZ()))
                        .getBlock() ==
                      Blocks.air
                    ) {
                      blockHardness *= 3;
                    } else {
                      blockHardness *= 4;
                    }
                  } else {
                    blockHardness *= 4;
                  }
                }

                child.totalCost += blockHardness;
                //SendChat.chat(String.valueOf(child.totalCost));
              }
            }

            if (
              blockType != Blocks.lava &&
              blockType != Blocks.gravel &&
              blockType != Blocks.sand &&
              blockType != Blocks.bedrock
            ) children.add(child);
          }
        }
      }
    }

    return children;
  }

  public static boolean isInMiddle(BlockPos child, BlockPos parent) {
    if (
      DistanceFromTo.distanceFromTo(new BlockPos(child.getX(), child.getY() - 1, child.getZ()), parent) >
      DistanceFromTo.distanceFromTo(child, parent)
    ) {
      return true;
    }

    return false;
  }

  public static double getGCost(BlockNode parent, BlockPos childPos) {
    return (parent.gCost() + DistanceFromTo.distanceFromTo(parent.blockPos(), childPos));
  }

  public static double getHCost(BlockPos childPos, BlockPos destinationPos) {
    return DistanceFromTo.distanceFromTo(childPos, destinationPos);
  }
}
