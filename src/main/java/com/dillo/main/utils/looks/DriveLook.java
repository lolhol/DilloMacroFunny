package com.dillo.main.utils.looks;

import static com.dillo.utils.previous.random.ids.mc;

import com.dillo.config.config;
import com.dillo.events.MillisecondEvent;
import com.dillo.events.macro.OnStartJumpEvent;
import com.dillo.events.utilevents.CurJumpProgress;
import com.dillo.events.utilevents.OnChangeYawEvent;
import com.dillo.utils.previous.SendChat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DriveLook {

  public static long startJTime;
  private static long startTime;
  private static long totalTime1;
  private static long endTime;
  private static long endTimeP;
  private static boolean isDoneP = true;
  private static double addAm = 0;
  private static double addPi = 0;
  private static boolean isDoneRotate = true;
  private static double add = 0;
  private static boolean registered;
  private static double max;
  private static float addYawLook;
  public static boolean projectJump = false;
  public static boolean doneLook180 = false;

  public static void addYaw(long totalTime, float addYaw) {
    addAm = addYaw / totalTime;
    addYawLook = addYaw;
    startTime = System.currentTimeMillis();
    endTime = System.currentTimeMillis() + totalTime;
    totalTime1 = totalTime;
    add = 0;
    max = 0;

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

  public static void pause() {
    isDoneRotate = true;
  }

  public static void unpause() {
    isDoneRotate = false;
  }

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (!isDoneRotate) {
      if (System.currentTimeMillis() <= endTime) {
        mc.thePlayer.rotationYaw += (float) addAm;
        add += addAm;
        MinecraftForge.EVENT_BUS.post(new OnChangeYawEvent(mc.thePlayer.rotationYaw));
      } else {
        doneLook180 = true;
        projectJump = false;
        isDoneRotate = true;
        resetJump();
      }

      if (add > 60 && !registered) {
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

  public static void resetJump() {
    max = 0;
  }
}
