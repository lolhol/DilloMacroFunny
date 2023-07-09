package com.dillo.dilloUtils.Utils;

import static com.dillo.dilloUtils.LookAt.getNeededChange;
import static com.dillo.dilloUtils.LookAt.getRotation;
import static com.dillo.dilloUtils.NewSpinDrive.isLeft;

import com.dillo.data.config;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.Teleport.GetNextBlock;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class GetMostOptimalPath {

  public static OptimalPath getBestPath(List<BlockPos> originBlocks, float currentLook) {
    float displacement = 0;
    BlockPos nextBlock = GetNextBlock.getNextBlock();
    float bestDisplacement = 0;
    List<BlockPos> best = new ArrayList<BlockPos>();
    float bestPoints = 0;

    while (displacement < 360) {
      float points = 0;
      List<BlockPos> prevBest = new ArrayList<>();

      for (BlockPos block : originBlocks) {
        Vec3 centeredBlock = centerBlock(block);

        float yaw = getYawNeededVec(centeredBlock, displacement);
        if (isLeft) {
          if (yaw > (float) -config.headRotationMax + 40 && yaw < 0) {
            prevBest.add(block);
          }
        } else {
          if (yaw < (float) config.headRotationMax - 40 && yaw > 0) {
            prevBest.add(block);
          }
        }
      }

      points += prevBest.size();
      if (nextBlock != null) {
        points +=
          10 /
          Math.abs(getYawNeededVec(BlockUtils.fromBlockPosToVec3(nextBlock), displacement + config.headRotationMax));
      }

      if (bestPoints < points) {
        bestDisplacement = displacement;
        best = new ArrayList<>(prevBest);
      }

      displacement += 5;
    }

    return new OptimalPath(best, bestDisplacement);
  }

  @Getter
  @AllArgsConstructor
  public static class OptimalPath {

    public List<BlockPos> path = null;
    public float displacement = 0;
  }

  public static Vec3 centerBlock(BlockPos block) {
    return new Vec3(block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
  }

  public static float getYawNeededVec(Vec3 block, float addCurYaw) {
    LookAt.Rotation rotation = getRotation(block);
    LookAt.Rotation startRot = new LookAt.Rotation(
      ids.mc.thePlayer.rotationPitch,
      ids.mc.thePlayer.rotationYaw + addCurYaw
    );
    LookAt.Rotation neededChange = getNeededChange(startRot, rotation);
    LookAt.Rotation endRot = new LookAt.Rotation(startRot.pitch + neededChange.pitch, startRot.yaw + neededChange.yaw);

    return neededChange.yaw;
  }
}
