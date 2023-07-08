package com.dillo.dilloUtils.Utils;

import com.dillo.dilloUtils.LookAt;
import com.dillo.utils.previous.random.ids;

public class LookYaw {

  public static void lookToYaw(long time, float addYaw) {
    float rotation = curRotation() + addYaw;

    LookAt.smoothLook(new LookAt.Rotation(0, rotation), time);
  }

  public static float curRotation() {
    float rotationYaw = ids.mc.thePlayer.rotationYaw;

    rotationYaw %= 360;
    if (rotationYaw < 0) {
      rotationYaw += 360;
    }

    return rotationYaw;
  }

  public static void lookToPitch(long time, float addPitch) {
    float rotation = curRotation();

    LookAt.smoothLook(new LookAt.Rotation(ids.mc.thePlayer.rotationPitch + addPitch, rotation), time);
  }

  public static void lookToYawPitch(long time, float addPitch, float addYaw) {
    float rotationYaw = curRotation() + addYaw;

    LookAt.smoothLook(new LookAt.Rotation(ids.mc.thePlayer.rotationPitch + addPitch, rotationYaw), time);
  }
}
