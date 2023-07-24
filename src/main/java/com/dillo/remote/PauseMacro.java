package com.dillo.remote;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.calls.KillSwitch;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PauseMacro {

  private static int pauseTicks = 0;
  private static boolean pause = false;
  private static int currTicks = 0;
  private static CurrentState dilloMainState = null;
  private static KillSwitch dilloOfflineState = null;

  public static void pauseMacro(int time, boolean unpause) {
    if (!unpause) {
      pauseTicks = time;
      pause = true;
      dilloMainState = ArmadilloStates.currentState;
      dilloOfflineState = ArmadilloStates.offlineState;
    } else {
      pause = false;
      currTicks = 0;
      pauseTicks = 0;

      if (dilloOfflineState != null) {
        ArmadilloStates.currentState = dilloMainState;
        ArmadilloStates.offlineState = dilloOfflineState;
      }
    }
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (pause) {
        if (currTicks <= pauseTicks) {
          pause = false;
          currTicks = 0;
          pauseTicks = 0;
          ArmadilloStates.currentState = dilloMainState;
          ArmadilloStates.offlineState = dilloOfflineState;
        } else {
          currTicks++;
        }
      }
    }
  }
}
