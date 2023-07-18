package com.dillo.dilloUtils;

import static com.dillo.utils.previous.random.ids.mc;

import com.dillo.Events.MillisecondEvent;
import com.dillo.utils.previous.SendChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DriveLook {

  public static LookAt.Rotation startRot;
  public static LookAt.Rotation endRot;
  private static long endTime;
  private static double addAm = 0;
  private static boolean isDoneRotate = true;
  private static double add = 0;

  public static void addYaw(long totalTime, float addYaw) {
    addAm = addYaw / totalTime;
    endTime = System.currentTimeMillis() + totalTime;

    SendChat.chat(String.valueOf(addAm));

    isDoneRotate = false;
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
    }
  }
}
