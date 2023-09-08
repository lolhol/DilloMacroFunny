package com.dillo.pathfinding.mit.finder.main;

import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.pathfinding.mit.finder.utils.Costs;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.mit.finder.utils.Utils;
import com.dillo.utils.previous.SendChat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AStarPathFinder {

  public static BlockNodeClass startClass = null;
  public static BlockNodeClass endClass = null;
  public static BlockPos startBlock = null;
  public static BlockPos endBlock = null;

  boolean isStart;
  int ticks;

  int opened = 0;
  int closed = 0;

  public List<BlockNodeClass> AStarPathFinder(PathFinderConfig pathFinderConfig) {
    int depth = 0;

    List<BlockNodeClass> openSet = new ArrayList<>();
    isStart = true;

    HashSet<BlockNodeClass> closedSet = new HashSet<>();
    BlockNodeClass previousNode = null;

    BlockNodeClass startPoint = Utils.getClassOfStarting(
      pathFinderConfig.startingBlock,
      pathFinderConfig.destinationBlock
    );

    BlockNodeClass endPoint = Utils.getClassOfEnding(pathFinderConfig.startingBlock, pathFinderConfig.destinationBlock);
    openSet.add(Utils.getClassOfStarting(pathFinderConfig.startingBlock, pathFinderConfig.destinationBlock));

    while (depth <= pathFinderConfig.maxIterations && !openSet.isEmpty()) {
      opened = openSet.size();
      closed = closedSet.size();

      //----------------------------------------------------------------------
      //| hCost ====> distance from end node.                                |
      //| gCost ====> distance from start node.                              |
      //| fCost ====> gCost + hCost.                                         |
      //----------------------------------------------------------------------

      BlockNodeClass node = openSet.get(0);
      for (BlockNodeClass blockNode : openSet) {
        if (blockNode.totalCost < node.totalCost && blockNode.hCost < node.hCost && !closedSet.contains(blockNode)) {
          node = blockNode;
        }
      }
      openSet.remove(node);
      closedSet.add(node);

      if (Utils.isSameBlock(node, endPoint) && node.parentOfBlock != null) {
        endPoint.parentOfBlock = previousNode;
        isStart = false;

        SendChat.chat(
          "Found! Opened " + this.opened + ". And closed " + this.closed + ". Total -> " + (this.opened + this.closed)
        );

        return Utils.retracePath(startPoint, endPoint);
      }

      List<BlockNodeClass> children = Utils.getBlocksAround(node);
      for (BlockNodeClass child : children) {
        if (closedSet.contains(child)) {
          continue;
        }

        Utils.ReturnClass typeAction = Utils.isAbleToInteract(
          child.blockPos,
          child.parentOfBlock,
          pathFinderConfig.isMine
        );

        if (typeAction == null) {
          continue;
        }

        child.actionType = typeAction.actionType;
        double totalAddBreak = 0;
        if (pathFinderConfig.isMine && typeAction.blocksToBreak != null) {
          for (BlockPos block : typeAction.blocksToBreak) {
            child.broken.add(block);
            totalAddBreak += Costs.getBreakCost(block);
          }
        }

        child.hCost += Costs.getActionCost(child.actionType);
        child.totalCost = Costs.getFullCost(child.blockPos, child.startBlock, child.finalBlock) + totalAddBreak;
        openSet.add(child);
      }

      previousNode = node;
      depth++;
    }

    isStart = false;
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
