package com.dillo.dilloUtils.Utils;

import static com.dillo.dilloUtils.LookAt.getNeededChange;
import static com.dillo.dilloUtils.LookAt.getRotation;
import static com.dillo.dilloUtils.NewSpinDrive.isLeft;

import com.dillo.data.config;
import com.dillo.dilloUtils.LookAt;
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
    float bestDisplacement = 0;
    List<BlockPos> best = new ArrayList<BlockPos>();

    while (displacement < 360) {
      List<BlockPos> prevBest = new ArrayList<>();

      for (BlockPos block : originBlocks) {
        Vec3 centeredBlock = centerBlock(block);

        float yaw = getYawNeededVec(centeredBlock, displacement);
        if (isLeft) {
          if (yaw > (float) -config.headRotationMax && yaw < 0) {
            prevBest.add(block);
          }
        } else {
          if (yaw < (float) config.headRotationMax && yaw > 0) {
            prevBest.add(block);
          }
        }
      }

      if (best.size() < prevBest.size()) {
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
