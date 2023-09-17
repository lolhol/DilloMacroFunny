package com.dillo.pathfinding.mit.finder.main;

import com.dillo.pathfinding.mit.finder.utils.*;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AStarPathFinder {

  public static HashSet<BlockNodeClass> closedSet = new HashSet<>();

  public BlockPos startBlock = null;
  public BlockPos endBlock = null;

  boolean isStart;
  int ticks;

  int opened = 0;
  int closed = 0;

  public List<BlockNodeClass> run(PathFinderConfig pathFinderConfig) {
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    int depth = 0;
    isStart = true;

    PriorityQueue<BlockNodeClass> openSet = new PriorityQueue<>(new BlockNodeCompare());
    closedSet = new HashSet<>();

    BlockNodeClass previousNode = null;
    BlockNodeClass startPoint = Utils.getClassOfStarting(
      pathFinderConfig.startingBlock,
      pathFinderConfig.destinationBlock
    );

    startBlock = pathFinderConfig.startingBlock;
    endBlock = pathFinderConfig.destinationBlock;

    if (pathFinderConfig.startingBlock.equals(pathFinderConfig.destinationBlock)) {
      List<BlockNodeClass> ls = new ArrayList<>();
      ls.add(Utils.getClassOfStarting(pathFinderConfig.startingBlock, pathFinderConfig.destinationBlock));

      return ls;
    }

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

      BlockNodeClass node = openSet.poll();
      closedSet.add(node);

      if (node.blockPos.equals(endBlock) && node.parentOfBlock != null) {
        endPoint.parentOfBlock = previousNode;
        isStart = false;

        /*SendChat.chat(
          "Found! Opened " + this.opened + ". And closed " + this.closed + ". Total -> " + (this.opened + this.closed)
        );*/

        return Utils.retracePath(startPoint, endPoint);
      }

      List<BlockNodeClass> children = Utils.getBlocksAround(node, startBlock, endBlock);
      for (BlockNodeClass child : children) {
        if (closedSet.contains(child)) {
          openSet.remove(child);
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
            totalAddBreak += Costs.getBreakCost(block);
          }
        }

        child.hCost += Costs.getActionCost(child.actionType);
        child.totalCost = Costs.getFullCost(child.blockPos, startBlock, endBlock) + totalAddBreak;
        openSet.add(child);
      }

      previousNode = node;
      depth++;
    }

    isStart = false;
    SendChat.chat("!!!" + openSet.size());
    return null;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!this.isStart) return;
    if (this.ticks >= 50) {
      this.ticks = 0;
      SendChat.chat("Update: Opened " + this.opened + ". And closed " + this.closed);
    }

    this.ticks++;
  }
}
