package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.utils.previous.random.ids;
import java.util.Objects;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//THIS IS NOT MY CODE LOL (GOT IT FROM GTC)
//(I'M TOO BRAIN-DEAD TO MAKE SMH LIKE THIS...)

public class LookAt {

  private static RotationType rotationType;

  private enum RotationType {
    NORMAL,
  }

  public static Rotation startRot;

  public static boolean done = true;
  public static Rotation neededChange;
  public static Rotation endRot;
  private static long startTime;
  private static long endTime;

  public static class Rotation {

    public float pitch;
    public float yaw;

    public Rotation(float pitch, float yaw) {
      this.pitch = pitch;
      this.yaw = yaw;
    }
  }

  public static void smoothLook(Rotation rotation, long time) {
    rotationType = RotationType.NORMAL;

    //float pitch1 = FromBlockToHP.getPitch(x + 0.25, y, z + 0.25);
    //float yaw1 = FromBlockToHP.getYaw(x + 0.25, y, z + 0.25);

    startRot = new Rotation(ids.mc.thePlayer.rotationPitch, ids.mc.thePlayer.rotationYaw);

    neededChange = getNeededChange(startRot, rotation);

    endRot = new Rotation(startRot.pitch + neededChange.pitch, startRot.yaw + neededChange.yaw);

    startTime = System.currentTimeMillis();
    endTime = System.currentTimeMillis() + time;
  }

  public static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
    float yawDiff = wrapAngleTo180(endRot.yaw) - wrapAngleTo180(startRot.yaw);

    if (yawDiff <= -180) {
      yawDiff += 360;
    } else if (yawDiff > 180) {
      yawDiff -= 360;
    }

    return new Rotation(endRot.pitch - startRot.pitch, yawDiff);
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

  //if (Objects.equals(ArmadilloStates.offlineState, "online")) {

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (Objects.equals(ArmadilloStates.offlineState, "online")) {
      if (rotationType != RotationType.NORMAL) return;
      if (System.currentTimeMillis() <= endTime) {
        ids.mc.thePlayer.rotationPitch = interpolate(startRot.pitch, endRot.pitch);
        ids.mc.thePlayer.rotationYaw = interpolate(startRot.yaw, endRot.yaw);
      } else {
        if (!done) {
          ids.mc.thePlayer.rotationYaw = endRot.yaw;
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

  public static Rotation getRotation(Vec3 vec3) {
    return getRotation(
      new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + ids.mc.thePlayer.getEyeHeight(), ids.mc.thePlayer.posZ),
      vec3
    );
  }

  public static Rotation getRotation(final Vec3 from, final Vec3 to) {
    double diffX = to.xCoord - from.xCoord;
    double diffY = to.yCoord - from.yCoord;
    double diffZ = to.zCoord - from.zCoord;
    double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

    float pitch = (float) -Math.atan2(dist, diffY);
    float yaw = (float) Math.atan2(diffZ, diffX);
    pitch = (float) wrapAngleTo180((float) ((pitch * 180F / Math.PI + 90) * -1));
    yaw = (float) wrapAngleTo180((float) ((yaw * 180 / Math.PI) - 90));

    return new Rotation(pitch, yaw);
  }

  public static Rotation getRotation(BlockPos block) {
    return getRotation(new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5));
  }
}
