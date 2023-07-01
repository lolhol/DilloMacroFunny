package com.dillo.dilloUtils.BlockUtils;

import com.dillo.utils.previous.random.ids;
import com.dillo.utils.radToDegree;

public class FromBlockToHP {

  public static float getPitch(double x, double y, double z) {
    double dx = ids.mc.thePlayer.posX - x + 0.0001;
    double dz = ids.mc.thePlayer.posY - z + 0.0001;
    double dy = ids.mc.thePlayer.posZ - y + 0.0001;

    double dist = Math.sqrt(dx * dx + dz * dz);
    double hoekPitch;

    hoekPitch = radToDegree.radToDegree(Math.atan(dy / dist));
    return (float) hoekPitch;
  }

  public static float getYaw(double x, double y, double z) {
    double hoekYaw;

    double playerAngleYaw = ids.mc.thePlayer.rotationYaw;
    double angleYaw = 0;

    playerAngleYaw = playerAngleYaw % 360;
    double dx = ids.mc.thePlayer.posX - x + 0.0001;
    double dz = ids.mc.thePlayer.posY - z + 0.0001;

    if (dx < 0.0 && dz < 0.0) {
      angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 180;
    } else if (dz < 0.0 && dx > 0.0) {
      angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 360;
    } else if (dz > 0.0 && dx < 0.0) {
      angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 180;
    } else if (dz > 0.0 && dx > 0.0) {
      angleYaw = radToDegree.radToDegree(Math.atan(dz / dx));
    }

    hoekYaw = angleYaw - Math.abs(playerAngleYaw) + 90;

    if (hoekYaw > 180) {
      hoekYaw -= 360;
    }

    if (hoekYaw < -180) {
      hoekYaw += 360;
    }

    return (float) hoekYaw;
  }
}
