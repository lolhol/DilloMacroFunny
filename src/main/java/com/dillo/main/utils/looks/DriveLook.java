package com.dillo.main.utils.looks;

import static com.dillo.utils.previous.random.ids.mc;

import com.dillo.events.MillisecondEvent;
import com.dillo.events.macro.OnStartJumpEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DriveLook {

  public static LookAt.Rotation startRot;
  public static LookAt.Rotation endRot;
  private static long endTime;
  private static long endTimeP;
  private static boolean isDoneP = true;
  private static double addAm = 0;
  private static double addPi = 0;
  private static boolean isDoneRotate = true;
  private static double add = 0;
  private static boolean registered;
  long time;

  public static void addYaw(long totalTime, float addYaw) {
    addAm = addYaw / totalTime;
    endTime = System.currentTimeMillis() + totalTime;
    add = 0;

    isDoneRotate = false;
    registered = false;
  }

  public static void addPitch(long totalTime, float addPitch) {
    addPi = addPitch / totalTime;
    endTimeP = System.currentTimeMillis() + totalTime;

    isDoneP = false;
  }

  public static void reset() {
    isDoneRotate = true;
  }

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (!isDoneRotate) {
      if (System.currentTimeMillis() <= endTime) {
        mc.thePlayer.rotationYaw += addAm;
        add += addAm;
      } else {
        isDoneRotate = true;
      }

      if (add > 150 && !registered) {
        MinecraftForge.EVENT_BUS.post(new OnStartJumpEvent(endTime - System.currentTimeMillis()));
        registered = true;
      }
    }

    if (!isDoneP) {
      if (System.currentTimeMillis() <= endTimeP) {
        mc.thePlayer.rotationPitch += addPi;
      } else {
        isDoneP = true;
      }
    }
  }
}
