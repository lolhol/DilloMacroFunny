package com.dillo.main.failsafes.AminStuff;

import com.dillo.calls.ArmadilloStates;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AdminRenamingDrillFail {

  int count = 0;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (ArmadilloStates.isOnline()) return;
    /*if (count < 500) {
      count++;
      return;
    }

    count = 0;

    if (DilloItemUtil.isGotAllItems()) return;

    ArmadilloStates.currentState = null;
    ArmadilloStates.offlineState = KillSwitch.OFFLINE;

    SendChat.chat(prefix.prefix + "Something fishy is going on with ur items... Admin check?");*/
  }
}
