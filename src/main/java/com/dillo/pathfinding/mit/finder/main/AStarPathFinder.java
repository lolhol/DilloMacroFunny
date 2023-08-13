package com.dillo.pathfinding.mit.finder.main;

import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.pathfinding.mit.finder.utils.Costs;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.mit.finder.utils.Utils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AStarPathFinder {

  boolean isStart;
  int ticks;

  int opened = 0;
  int closed = 0;

  public List<BlockPos> AStarPathFinder(PathFinderConfig pathFinderConfig) {
    int depth = 0;
    List<BlockNodeClass> openSet = new ArrayList<>();
    HashSet<BlockNodeClass> closedSet = new HashSet<>();
    BlockNodeClass previousNode = null;

    BlockNodeClass startPoint = Utils.getClassOfStarting(
      pathFinderConfig.startingBlock,
      pathFinderConfig.destinationBlock
    );
    BlockNodeClass endPoint = Utils.getClassOfEnding(pathFinderConfig.startingBlock, pathFinderConfig.destinationBlock);
    openSet.add(Utils.getClassOfStarting(pathFinderConfig.startingBlock, pathFinderConfig.destinationBlock));

    while (depth <= pathFinderConfig.maxIterations && openSet.size() > 0) {
      opened = openSet.size();
      closed = closedSet.size();

      //hCost ====> distance from end node.
      //gCost ====> distance from start node.
      //fCost ====> gCost + hCost.

      BlockNodeClass node = openSet.get(0);

      for (BlockNodeClass blockNode : openSet) {
        if (blockNode.totalCost <= node.totalCost && blockNode.hCost < node.hCost) {
          if (!closedSet.contains(blockNode)) {
            node = blockNode;
          }
        }
      }

      openSet.remove(node);
      closedSet.add(node);

      if (node.isSame(endPoint.blockPos)) {
        endPoint.parentOfBlock = previousNode;
        SendChat.chat("Found!");
        return Utils.retracePath(startPoint, endPoint);
      }

      //List<BlockNode> children = getChildren(node, endPoint.blockPos());
      List<BlockNodeClass> children = Utils.getBlocksAround(node);

      for (BlockNodeClass child : children) {
        if (closedSet.contains(child)) {
          continue;
        }

        if (!Utils.isAbleToInteract(child.blockPos, child.parentOfBlock.blockPos, false)) {
          continue;
        }

        double newCostToNeighbour = node.gCost + DistanceFromTo.distanceFromTo(node.blockPos(), child.blockPos());
        if (newCostToNeighbour < child.gCost || !openSet.contains(child)) {
          /*RenderMultipleBlocksMod.renderMultipleBlocks(
            new Vec3(child.blockPos().getX(), child.blockPos().getY(), child.blockPos().getZ()),
            true
          );*/

          //child.gCost = newCostToNeighbour;
          child.hCost = Costs.calculateHCostBlockPos(child.blockPos, pathFinderConfig.destinationBlock);
          child.gCost = DistanceFromTo.distanceFromTo(child.blockPos(), startPoint.blockPos());
          child.totalCost = Costs.calculateFullCostDistance(child);
          child.parentOfBlock = node;

          openSet.add(child);
        }
      }

      previousNode = node;
      depth++;
    }

    return null;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!this.isStart) return;
    if (this.ticks >= 50) {
      this.ticks = 0;
      SendChat.chat("Opened " + this.opened + ". And closed " + this.closed);
    }

    this.ticks++;
  }
}
