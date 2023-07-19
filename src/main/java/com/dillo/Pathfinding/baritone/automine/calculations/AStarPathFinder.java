package com.dillo.Pathfinding.baritone.automine.calculations;

import com.dillo.Pathfinding.baritone.automine.calculations.behaviour.PathFinderBehaviour;
import com.dillo.Pathfinding.baritone.automine.calculations.behaviour.PathMode;
import com.dillo.Pathfinding.baritone.automine.calculations.exceptions.NoBlockException;
import com.dillo.Pathfinding.baritone.automine.calculations.exceptions.NoPathException;
import com.dillo.Pathfinding.baritone.automine.handlers.MacroHandler;
import com.dillo.Pathfinding.baritone.automine.logging.Logger;
import com.dillo.Pathfinding.baritone.automine.structures.BlockNode;
import com.dillo.Pathfinding.baritone.automine.structures.BlockType;
import com.dillo.Pathfinding.baritone.automine.structures.Path;
import com.dillo.Pathfinding.baritone.automine.structures.SemiPath;
import com.dillo.Pathfinding.baritone.automine.utils.AngleUtils;
import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockUtils;
import com.dillo.Pathfinding.baritone.automine.utils.HypixelUtils.MineUtils;
import java.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class AStarPathFinder {

  Minecraft mc = Minecraft.getMinecraft();

  ArrayList<BlockPos> blackListedPos = new ArrayList<>();

  AStarCalculator calculator = new AStarCalculator();

  PathFinderBehaviour pathFinderBehaviour;
  PathMode mode = PathMode.MINE;
  BlockPos lastTarget;

  public AStarPathFinder(PathFinderBehaviour options) {
    pathFinderBehaviour = options;
  }

  public void addToBlackList(BlockPos... blackListedPos) {
    this.blackListedPos.addAll(Arrays.asList(blackListedPos));
  }

  public void removeFromBlackList(BlockPos blockPos) {
    blackListedPos.remove(blockPos);
  }

  public void clearBlackList() {
    blackListedPos.clear();
  }

  public Path getPath(PathMode mode, boolean withPreference, ArrayList<ArrayList<BlockData<?>>> blockType)
    throws NoBlockException, NoPathException {
    initialize(mode);

    long pastTime = System.currentTimeMillis();

    LinkedList<LinkedList<BlockNode>> possiblePaths = new LinkedList<>();
    List<BlockPos> foundBlocks = new ArrayList<>();

    foundBlocks.add(new BlockPos(207, 75, 187));

    possiblePaths = getPossiblePaths(foundBlocks);

    if (!possiblePaths.isEmpty()) {
      Logger.playerLog(
        "Total time | Time per path : " +
        (System.currentTimeMillis() - pastTime) +
        " ms | " +
        ((System.currentTimeMillis() - pastTime) * 1.0D / possiblePaths.size()) +
        " ms"
      );
      possiblePaths.sort(Comparator.comparingDouble(this::calculatePathCost));
      setLastTarget(possiblePaths.getFirst());
      return new Path(possiblePaths.getFirst(), mode);
    }

    /*if (withPreference) { // loop for EACH block type
      for (ArrayList<BlockData<?>> block : blockType) {
        foundBlocks =
          BlockUtils.findBlockInCube(
            pathFinderBehaviour.getSearchRadius() * 2,
            blackListedPos,
            pathFinderBehaviour.getMinY(),
            pathFinderBehaviour.getMaxY(),
            block
          );
      }
    } else { // 1 loop for ALL block types
      for (ArrayList<BlockData<?>> block : blockType) {
        foundBlocks.addAll(
          BlockUtils.findBlockInCube(pathFinderBehaviour.getSearchRadius(), blackListedPos, 0, 256, block)
        );
      }
      possiblePaths.addAll(getPossiblePaths(foundBlocks));
    }*/

    if (foundBlocks.isEmpty()) throw new NoBlockException();

    if (possiblePaths.isEmpty()) throw new NoPathException();

    Logger.playerLog(
      "Total time | Time per path : " +
      (System.currentTimeMillis() - pastTime) +
      " ms | " +
      ((System.currentTimeMillis() - pastTime) * 1.0D / possiblePaths.size()) +
      " ms"
    );
    possiblePaths.sort(Comparator.comparingDouble(this::calculatePathCost));
    setLastTarget(possiblePaths.getFirst());
    return new Path(possiblePaths.getFirst(), mode);
  }

  public Path getPath(PathMode mode, BlockPos blockPos) throws NoPathException { // from blockPos
    initialize(mode);
    LinkedList<BlockNode> path = calculator.calculatePath(
      BlockUtils.getPlayerLoc(),
      blockPos,
      pathFinderBehaviour,
      mode,
      20000
    );

    if (path.isEmpty()) throw new NoPathException();

    Logger.log("Path size: " + path.size());
    setLastTarget(path);

    return Objects.requireNonNull(path.pollLast()).isFullPath() ? new Path(path, mode) : new SemiPath(path, mode);
  }

  private void setLastTarget(LinkedList<BlockNode> blockList) {
    removeFromBlackList(lastTarget); //prevent it from finding again
    this.lastTarget = blockList.getFirst().getPos();
  }

  private LinkedList<LinkedList<BlockNode>> getPossiblePaths(List<BlockPos> targetBlocks) {
    LinkedList<LinkedList<BlockNode>> possiblePaths = new LinkedList<>();
    int limit = 3000;
    for (BlockPos targetBlock : targetBlocks) {
      LinkedList<BlockNode> path = pathFinderBehaviour.isStaticMode()
        ? calculator.calculateStaticPath(targetBlock)
        : calculator.calculatePath(BlockUtils.getPlayerLoc(), targetBlock, pathFinderBehaviour, mode, limit);

      if (!path.isEmpty()) {
        if (path.getLast().getPos() == null) path.removeLast(); // remove last dummy blockNode as it is useless for find(BLock)

        possiblePaths.add(path);
        limit = calculator.getStep();
      }
    }
    return possiblePaths;
  }

  private void initialize(PathMode mode) {
    this.mode = mode;
  }

  private double calculatePathCost(List<BlockNode> nodes) {
    double cost = 0.0D;
    //if (MacroHandler.macros.get(0).isEnabled()) {
    if (nodes.size() <= 2) {
      for (BlockNode node : nodes) cost +=
        (
          Math.abs(
            AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
          ) +
          Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
        ) /
        540.0d;
    } else {
      for (BlockNode node : nodes) cost += (node.getType() == BlockType.WALK) ? 1D : 1.5D;
    }
    /*for (BlockNode node : nodes) {
      if (
        MineUtils
          .getMithrilColorBasedOnPriority(4)
          .stream()
          .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
      ) {
        cost +=
          4.0D *
          (
            Math.abs(
              AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
            ) +
            Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
          ) /
          540.0d;
      } else if (
        MineUtils
          .getMithrilColorBasedOnPriority(3)
          .stream()
          .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
      ) {
        cost +=
          3.0D *
          (
            Math.abs(
              AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
            ) +
            Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
          ) /
          540.0d;
      } else if (
        MineUtils
          .getMithrilColorBasedOnPriority(2)
          .stream()
          .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
      ) {
        cost +=
          2.0D *
          (
            Math.abs(
              AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
            ) +
            Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
          ) /
          540.0d;*/
    /*} else {
            cost +=
              (
                Math.abs(
                  AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) -
                  AngleUtils.getRequiredYawSide(node.getPos())
                ) +
                Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
              ) /
              540.0d;
          }*/
    /*} else {
        if (
          MineUtils
            .getMithrilColorBasedOnPriority(2)
            .stream()
            .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
        ) {
          cost +=
            12.5D *
            (
              Math.abs(
                AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
              ) +
              Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
            ) /
            540.0d;
        } else if (
          MineUtils
            .getMithrilColorBasedOnPriority(1)
            .stream()
            .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
        ) {
          cost +=
            8.0D *
            (
              Math.abs(
                AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
              ) +
              Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
            ) /
            540.0d;
        } else if (
          MineUtils
            .getMithrilColorBasedOnPriority(0)
            .stream()
            .anyMatch(blockData -> BlockUtils.isBlock(blockData, node.getPos()))
        ) {
          cost +=
            4.0D *
            (
              Math.abs(
                AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
              ) +
              Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
            ) /
            540.0d;
        } else {
          cost +=
            (
              Math.abs(
                AngleUtils.getActualRotationYaw(mc.thePlayer.rotationYaw) - AngleUtils.getRequiredYawSide(node.getPos())
              ) +
              Math.abs(mc.thePlayer.rotationPitch - AngleUtils.getRequiredPitchSide(node.getPos()))
            ) /
            540.0d;
        }
      }
    }*/
    //}
    return cost;
  }
}
