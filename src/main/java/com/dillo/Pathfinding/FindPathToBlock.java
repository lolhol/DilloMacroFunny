package com.dillo.Pathfinding;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleLines;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class FindPathToBlock {

  public static BlockPos finalBlock = null;

  public static List<BlockPos> findPathToBlock(
    BlockPos block,
    int depth,
    List<BlockPos> routeBlocks,
    HashSet<BlockPos> alrVisited
  ) {
    depth++;

    if (!preinitChecks(depth, block)) {
      return null;
    }

    if (routeBlocks.size() > 0 && routeBlocks.get(routeBlocks.size() - 1).equals(block)) {
      return routeBlocks;
    }

    // Get list with optimized positions
    List<BlockPos> bestBlockPositions = getBestList(routeBlocks, block);

    // Performs the recursions via for loop because the "getClosestBlocks()" returns a list.
    routeBlocks = new ArrayList<>(routeBlocks);
    for (int i = 0; i < bestBlockPositions.size(); i++) {
      BlockPos blockInQuestion = bestBlockPositions.get(i);
      BlockPos blockToAdd = null;

      if (checkIfCanFall(blockInQuestion)) {
        int holeDepth = getDepthOfHole(blockInQuestion);
        if (holeDepth < 20) {
          if (!alrVisited.contains(blockInQuestion)) {
            blockToAdd =
              new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() - holeDepth + 1, blockInQuestion.getZ());
          }
        }
      } else if (checkIfCanWalk(blockInQuestion)) {
        if (!alrVisited.contains(blockInQuestion)) {
          blockToAdd = blockInQuestion;
        }
      } else if (checkIfCanJump(blockInQuestion)) {
        if (!alrVisited.contains(blockInQuestion)) {
          blockToAdd = new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 1, blockInQuestion.getZ());
        }
      } else if (checkIfCanWalkUp(blockInQuestion)) {
        if (!alrVisited.contains(blockInQuestion)) {
          blockToAdd = new BlockPos(blockInQuestion.getX(), blockInQuestion.getY(), blockInQuestion.getZ());
          alrVisited.add(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 1, blockInQuestion.getZ()));
        }
      }

      alrVisited.add(blockInQuestion);

      if (blockToAdd != null) {
        routeBlocks.add(blockToAdd);
        alrVisited.add(blockToAdd);

        List<BlockPos> foundPath = findPathToBlock(block, depth, routeBlocks, alrVisited);

        if (foundPath != null) {
          return foundPath;
        }
      }
    }

    return null;
  }

  public static List<BlockPos> pathfinderTest(BlockPos block) {
    finalBlock = block;
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    List<BlockPos> foundWalkableRoute = new ArrayList<>();
    foundWalkableRoute.add(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ));

    HashSet<BlockPos> hashSet = new HashSet<>();

    List<BlockPos> routeFound = findPathToBlock(block, 0, foundWalkableRoute, hashSet);

    return routeFound;
  }

  public static int getDepthOfHole(BlockPos blockPos) {
    int depth = 0;
    double y = blockPos.getY();

    while (
      depth < 50 &&
      y > 0 &&
      ids.mc.theWorld.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ())).getBlock() == Blocks.air
    ) {
      depth++;
      y--;
    }

    return depth;
  }

  public static List<BlockPos> getLowestBlocks(List<BlockPos> blocks) {
    blocks.sort((a, b) -> {
      if (ids.mc.theWorld.getBlockState(new BlockPos(a.getX(), a.getY() - 1, a.getZ())).getBlock() == Blocks.air) {
        return -1;
      } else if (
        ids.mc.theWorld.getBlockState(new BlockPos(b.getX(), b.getY() - 1, b.getZ())).getBlock() == Blocks.air
      ) {
        return 1;
      } else {
        return 0;
      }
    });

    return blocks;
  }

  public static boolean checkIfCanWalkUp(BlockPos block) {
    Block originBlock = ids.mc.theWorld.getBlockState(block).getBlock();

    if (
      originBlock.toString().contains("stairs") ||
      (originBlock.toString().contains("slab") && !originBlock.toString().contains("double_"))
    ) {
      return true;
    }

    return false;
  }

  public static List<BlockPos> getClosestBlocks(List<BlockPos> blocks, BlockPos block) {
    blocks.sort((a, b) -> {
      return (DistanceFromTo.distanceFromTo(a, block) < DistanceFromTo.distanceFromTo(b, block)) ? -1 : 1;
    });

    return blocks;
  }

  private static boolean checkIfCanWalk(BlockPos blockInQuestion) {
    if (
      (
        ids.mc.theWorld.getBlockState(blockInQuestion).getBlock() == Blocks.air ||
        ids.mc.theWorld.getBlockState(blockInQuestion).getBlock() == Blocks.tallgrass ||
        ids.mc.theWorld.getBlockState(blockInQuestion).getBlock() == Blocks.red_flower ||
        ids.mc.theWorld.getBlockState(blockInQuestion).getBlock() == Blocks.yellow_flower
      ) &&
      ids.mc.theWorld
        .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 1, blockInQuestion.getZ()))
        .getBlock() ==
      Blocks.air &&
      ids.mc.theWorld
        .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() - 1, blockInQuestion.getZ()))
        .getBlock() !=
      Blocks.air
    ) {
      return true;
    }

    return false;
  }

  private static boolean checkIfCanFall(BlockPos blockInQuestion) {
    Block currBlockState = ids.mc.theWorld.getBlockState(blockInQuestion).getBlock();
    Block currBlockStateUnder1 = ids.mc.theWorld
      .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() - 1, blockInQuestion.getZ()))
      .getBlock();
    Block currBlockStateAbove1 = ids.mc.theWorld
      .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 1, blockInQuestion.getZ()))
      .getBlock();
    //Block currBlockStateAbove2 = ids.mc.theWorld.getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 2, blockInQuestion.getZ())).getBlock();

    if (currBlockState == Blocks.air && currBlockStateUnder1 == Blocks.air && currBlockStateAbove1 == Blocks.air) {
      return true;
    }

    return false;
  }

  private static boolean checkIfCanJump(BlockPos blockInQuestion) {
    Block currBlockStateAbove1 = ids.mc.theWorld
      .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 1, blockInQuestion.getZ()))
      .getBlock();
    Block currBlockStateAbove2 = ids.mc.theWorld
      .getBlockState(new BlockPos(blockInQuestion.getX(), blockInQuestion.getY() + 2, blockInQuestion.getZ()))
      .getBlock();

    if (currBlockStateAbove1 == Blocks.air && currBlockStateAbove2 == Blocks.air) {
      return true;
    }

    return false;
  }

  public static List<BlockPos> prioratizeJump(List<BlockPos> blocks) {
    blocks.sort((a, b) -> {
      if (ids.mc.theWorld.getBlockState(a).getBlock() != Blocks.air && !removeNonBlocks(a)) {
        return -1;
      } else if (ids.mc.theWorld.getBlockState(b).getBlock() != Blocks.air && !removeNonBlocks(a)) {
        return 1;
      } else {
        return 0;
      }
    });

    return blocks;
  }

  public static boolean removeNonBlocks(BlockPos block) {
    return (
      ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.tallgrass ||
      ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.red_flower ||
      ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.yellow_flower ||
      ids.mc.theWorld.getBlockState(new BlockPos(block.getX(), block.getY() - 1, block.getZ())).getBlock() ==
      Blocks.tallgrass
    );
  }

  private static List<BlockPos> getBestList(List<BlockPos> routeBlocks, BlockPos block) {
    List<BlockPos> newBlockPositions = new ArrayList<>();
    newBlockPositions.add(
      new BlockPos(
        routeBlocks.get(routeBlocks.size() - 1).getX() + 1,
        routeBlocks.get(routeBlocks.size() - 1).getY(),
        routeBlocks.get(routeBlocks.size() - 1).getZ()
      )
    );
    newBlockPositions.add(
      new BlockPos(
        routeBlocks.get(routeBlocks.size() - 1).getX(),
        routeBlocks.get(routeBlocks.size() - 1).getY(),
        routeBlocks.get(routeBlocks.size() - 1).getZ() + 1
      )
    );
    newBlockPositions.add(
      new BlockPos(
        routeBlocks.get(routeBlocks.size() - 1).getX() - 1,
        routeBlocks.get(routeBlocks.size() - 1).getY(),
        routeBlocks.get(routeBlocks.size() - 1).getZ()
      )
    );
    newBlockPositions.add(
      new BlockPos(
        routeBlocks.get(routeBlocks.size() - 1).getX(),
        routeBlocks.get(routeBlocks.size() - 1).getY(),
        routeBlocks.get(routeBlocks.size() - 1).getZ() - 1
      )
    );

    if (block.getY() < ids.mc.thePlayer.posY) {
      return getLowestBlocks(getClosestBlocks(newBlockPositions, block));
    } else if (block.getY() > ids.mc.thePlayer.posY) {
      return prioratizeJump(getClosestBlocks(newBlockPositions, block));
    } else {
      return getClosestBlocks(newBlockPositions, block);
    }
  }

  private static boolean preinitChecks(int depth, BlockPos block) {
    if (depth >= 200) {
      return false;
    }

    if (block == null) {
      return false;
    }

    return true;
  }

  public static void startPathFinder(BlockPos block) {
    ArmadilloStates.offlineState = "offline";
    RenderMultipleLines.renderMultipleLines(null, null, false);

    List<BlockPos> initList = new ArrayList<>();
    initList.add(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ));
    List<BlockPos> currentNewRoute = findPathToBlock(block, 0, initList, new HashSet<>());

    if (currentNewRoute != null) {
      for (int i = 0; i < currentNewRoute.size(); i++) {
        if (i != currentNewRoute.size() - 1) {
          RenderMultipleLines.renderMultipleLines(currentNewRoute.get(i), currentNewRoute.get(i + 1), true);
        }
      }

      currentNewRoute.remove(0);

      ArmadilloStates.offlineState = "online";
      WalkOnPath.walkOnPath(currentNewRoute);
    }
  }
}
