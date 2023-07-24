package com.dillo.main.teleport.utils;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.calls.KillSwitch;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaitThenCall {

  private static CurrentState newSetState = null;
  private static long waitTime = 0;
  private static long timePoint = 0;
  private static boolean startWait = false;

  public static void waitThenCall(long time, CurrentState newState) {
    newSetState = newState;
    waitTime = time;
    timePoint = System.currentTimeMillis();
    startWait = true;
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (startWait) {
      if (timePoint + waitTime <= System.currentTimeMillis()) {
        if (ArmadilloStates.offlineState == KillSwitch.OFFLINE) {
          startWait = false;
          ArmadilloStates.currentState = null;
        } else {
          startWait = false;
          ArmadilloStates.currentState = newSetState;
        }
      } else {
        if (ArmadilloStates.offlineState == KillSwitch.OFFLINE) {
          startWait = false;
          ArmadilloStates.currentState = null;
        }
      }
    }
  }
}
