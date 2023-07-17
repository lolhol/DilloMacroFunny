package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class YawLook {

  private static RotationTypeYaw rotationType;

  private enum RotationTypeYaw {
    NORMAL,
  }

  public static RotationYaw startRot;

  public static boolean done = true;
  public static RotationYaw neededChange;
  public static RotationYaw endRot;
  private static long startTime;
  private static long endTime;
  private static boolean isStart = true;

  public static class RotationYaw {

    public float pitch;
    public float yaw;

    public RotationYaw(float pitch, float yaw) {
      this.pitch = pitch;
      this.yaw = yaw;
    }
  }

  public static void smoothLook2(RotationYaw rotation, long time) {
    rotationType = RotationTypeYaw.NORMAL;

    startRot = new YawLook.RotationYaw(ids.mc.thePlayer.rotationPitch, ids.mc.thePlayer.rotationYaw);

    neededChange = getNeededChange(startRot, rotation);

    endRot = new YawLook.RotationYaw(startRot.pitch + neededChange.pitch, startRot.yaw + neededChange.yaw);

    startTime = System.currentTimeMillis();
    endTime = System.currentTimeMillis() + time;
  }

  public static YawLook.RotationYaw getNeededChange(YawLook.RotationYaw startRot, YawLook.RotationYaw endRot) {
    float yawDiff = wrapAngleTo180(endRot.yaw) - wrapAngleTo180(startRot.yaw);

    if (yawDiff <= -180) {
      yawDiff += 360;
    } else if (yawDiff > 180) {
      yawDiff -= 360;
    }

    return new YawLook.RotationYaw(endRot.pitch - startRot.pitch, yawDiff);
  }

  public static float wrapAngleTo180(float angle) {
    return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
  }

  public static void reset() {
    done = true;
    startRot = null;
    neededChange = null;
    endRot = null;
    startTime = 0;
    endTime = 0;
  }

  //if (ArmadilloStates.isOnline()) {

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (ArmadilloStates.isOnline()) {
      if (!isStart) {
        reset();
        return;
      }

      if (rotationType != RotationTypeYaw.NORMAL) return;
      if (System.currentTimeMillis() <= endTime) {
        ids.mc.thePlayer.rotationPitch = interpolate(startRot.pitch, endRot.pitch);
        //ids.mc.thePlayer.rotationYaw = interpolate(startRot.yaw, endRot.yaw);
      } else {
        if (!done) {
          //ids.mc.thePlayer.rotationYaw = endRot.yaw;
          ids.mc.thePlayer.rotationPitch = endRot.pitch;

          reset();
        }
      }
    }
  }

  private static float interpolate(float start, float end) {
    return (
      (end - start) * easeOutCubic((float) (System.currentTimeMillis() - startTime) / (endTime - startTime)) + start
    );
  }

  public static float easeOutCubic(double number) {
    return (float) Math.max(0, Math.min(1, 1 - Math.pow(1 - number, 3)));
  }

  public static YawLook.RotationYaw getRotation(Vec3 vec3) {
    return getRotation(
      new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + ids.mc.thePlayer.getEyeHeight(), ids.mc.thePlayer.posZ),
      vec3
    );
  }

  public static YawLook.RotationYaw getRotation(final Vec3 from, final Vec3 to) {
    double diffX = to.xCoord - from.xCoord;
    double diffY = to.yCoord - from.yCoord;
    double diffZ = to.zCoord - from.zCoord;
    double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

    float pitch = (float) -Math.atan2(dist, diffY);
    float yaw = (float) Math.atan2(diffZ, diffX);
    pitch = (float) wrapAngleTo180((float) ((pitch * 180F / Math.PI + 90) * -1));
    yaw = (float) wrapAngleTo180((float) ((yaw * 180 / Math.PI) - 90));

    return new YawLook.RotationYaw(pitch, yaw);
  }

  public static YawLook.RotationYaw getRotation(BlockPos block) {
    return getRotation(new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5));
  }
}
