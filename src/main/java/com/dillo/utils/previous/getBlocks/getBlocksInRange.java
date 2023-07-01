package com.dillo.utils.previous.getBlocks;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class getBlocksInRange {

  public static BlockPos getBlocksMain(int range, Set<BlockPos> destroyedBlocks, Block[] blockTypeArr) {
    BlockPos playerPos = ids.mc.thePlayer.getPosition();
    playerPos = playerPos.add(0, 1, 0);
    Vec3 playerVec = ids.mc.thePlayer.getPositionVector();
    Vec3i vec3i = new Vec3i(range, range, range);

    ArrayList<Vec3> blocks = new ArrayList<>();

    if (playerPos != null) {
      for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
        IBlockState blockState = ids.mc.theWorld.getBlockState(blockPos);

        Arrays
          .asList(blockTypeArr)
          .forEach(blockType -> {
            if (blockState.getBlock() == blockType && !destroyedBlocks.contains(blockPos)) {
              blocks.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
            }
          });
      }
    }

    double smallest = 9999;
    Vec3 closest = null;
    for (Vec3 block : blocks) {
      double dist = block.distanceTo(playerVec);
      if (dist < smallest) {
        smallest = dist;
        closest = block;
      }
    }

    if (closest != null && smallest < 5) {
      return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
    }

    return null;
  }
}
