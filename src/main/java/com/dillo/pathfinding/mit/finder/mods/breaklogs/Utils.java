package com.dillo.pathfinding.mit.finder.mods.breaklogs;

import com.dillo.pathfinding.mit.finder.main.AStarPathFinder;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Utils {

  public List<BlockPos> foundPath = new ArrayList<>();
  public PathFinderConfig config = null;
  AStarPathFinder finder = new AStarPathFinder();

  public BlockPos getClosest(List<BlockPos> blocks) {
    BlockPos closestBlock = null;
    double closestDist = 999999999;

    for (BlockPos block : blocks) {
      double ds = DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition());

      if (ds < closestDist) {
        closestDist = ds;
        closestBlock = block;
      }
    }

    return closestBlock;
  }

  public void startPathFinder(BlockPos destBlock) {
    foundPath = new ArrayList<>();

    new Thread(() -> {
      long start = System.currentTimeMillis();

      config =
        new PathFinderConfig(
          false,
          false,
          false,
          false,
          false,
          10,
          100000,
          1000,
          BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector().addVector(-0.5, 0, -0.5)),
          destBlock.add(0, 1, 0),
          new Block[] { Blocks.air },
          new Block[] { Blocks.air },
          300,
          0
        );

      this.foundPath = com.dillo.pathfinding.mit.finder.walker.Utils.getShortList(finder.run(config));
    })
      .start();
  }

  public boolean isPathFinderDone() {
    return !foundPath.isEmpty();
  }

  public List<BlockPos> retrieveFoundPath() {
    foundPath.add(config.destinationBlock);
    return this.foundPath;
  }

  public List<BlockPos> getSurroundingLogs(int xMax, int yMax, int zMax, BlockPos reference) {
    List<BlockPos> logList = new ArrayList<>();

    for (int x = -xMax; x <= xMax; x++) {
      for (int y = -yMax; y <= yMax; y++) {
        for (int z = -zMax; z <= zMax; z++) {
          BlockPos newBlock = BlockUtils.makeNewBlock(x, y, z, reference);

          if (BlockUtils.getBlock(newBlock) == Blocks.log) {
            logList.add(newBlock);
          }
        }
      }
    }

    return logList;
  }
}
