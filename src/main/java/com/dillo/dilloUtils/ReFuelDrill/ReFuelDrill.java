package com.dillo.dilloUtils.ReFuelDrill;

import static com.dillo.ArmadilloMain.CurrentState.REFUELING;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;

public class ReFuelDrill {

  public static void reFuelDrill() {
    ArmadilloStates.currentState = REFUELING;
    ArmadilloStates.offlineState = KillSwitch.OFFLINE;

    Thread refuel = new Thread(() -> {});
    refuel.start();
  }
}
