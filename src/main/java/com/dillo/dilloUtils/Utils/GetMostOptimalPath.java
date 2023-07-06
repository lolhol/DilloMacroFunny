package com.dillo.dilloUtils.Utils;

import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.getYawBlockAround;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;
import static com.dillo.utils.GetAngleToBlock.calcAngleFromYaw;

import com.dillo.utils.previous.chatUtils.SendChat;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;

public class GetMostOptimalPath {

  public static OptimalPath getBestPath(List<BlockPos> originBlocks, float currentLook) {
    float displacement = 0;
    float bestDisplacement = 0;
    List<BlockPos> best = new ArrayList<BlockPos>();

    while (displacement < 360) {
      List<BlockPos> prevBest = new ArrayList<>();

      for (BlockPos block : originBlocks) {
        float yaw = calcAngleFromYaw(block, currentLook + displacement);
        if (yaw < 80 || yaw > 280) {
          prevBest.add(block);
        }
      }

      if (best.size() < prevBest.size()) {
        bestDisplacement = displacement;
        best = new ArrayList<>(prevBest);
      }

      displacement += 10;
    }

    return new OptimalPath(best, displacement);
  }

  @Getter
  @AllArgsConstructor
  public static class OptimalPath {

    public List<BlockPos> path = null;
    public float displacement = 0;
  }
}
