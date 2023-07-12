package com.dillo.dilloUtils.Utils;

import static com.dillo.dilloUtils.LookAt.getNeededChange;
import static com.dillo.dilloUtils.LookAt.getRotation;
import static com.dillo.dilloUtils.NewSpinDrive.isLeft;
import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.canBeBroken;

import com.dillo.data.config;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.Teleport.GetNextBlock;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class GetMostOptimalPath {

  public static boolean isClear = false;

  public static OptimalPath getBestPath(List<BlockPos> originBlocks, float currentLook) {
    float displacement = 0;
    BlockPos nextBlock = GetNextBlock.getNextBlock();
    float bestDisplacement = 0;
    List<BlockPos> best = new ArrayList<BlockPos>();
    float bestPoints = 0;

    boolean includeNext = false;

    boolean canBeBroken = canBeBroken(ids.mc.thePlayer.getPosition());
    if (!canBeBroken || isClear) {
      includeNext = true;
    }

    while (displacement < 360) {
      float points = 0;
      List<BlockPos> prevBest = new ArrayList<>();
      double neededRotation = 0;

      if (nextBlock != null) {
        neededRotation =
          getYawNeededVec(BlockUtils.fromBlockPosToVec3(nextBlock), displacement + config.headRotationMax);
      }

      if (includeNext) {
        if (isLeft) {
          if (neededRotation > (float) -config.headRotationMax + 40 && neededRotation < 0) {
            points += 5;
          } else {
            points -= 1;
          }
        } else {
          if (neededRotation < (float) config.headRotationMax - 40 && neededRotation > 0) {
            points += 5;
          } else {
            points -= 1;
          }
        }
      }

      for (BlockPos block : originBlocks) {
        Vec3 centeredBlock = centerBlock(block);

        float yaw = getYawNeededVec(centeredBlock, displacement);

        float headMovementLeft = -config.headRotationMax + 40;
        float headMovementRight = config.headRotationMax - 40;

        if (isClear) {
          headMovementLeft = -160;
        } else {
          headMovementRight = 160;
        }

        if (isLeft) {
          if (yaw > headMovementLeft && yaw < 0) {
            if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass) {
              points += 1.5;
            } else {
              points += 1;
            }

            prevBest.add(block);
          }
        } else {
          if (yaw < headMovementRight && yaw > 0) {
            if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass) {
              points += 1.5;
            } else {
              points += 1;
            }

            prevBest.add(block);
          }
        }
      }

      if (bestPoints < points) {
        bestDisplacement = displacement;
        best = new ArrayList<>(prevBest);
      }

      displacement += 5;
    }

    isClear = false;

    if (isLeft) {
      return new OptimalPath(best, -bestDisplacement);
    } else {
      return new OptimalPath(best, bestDisplacement);
    }
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
