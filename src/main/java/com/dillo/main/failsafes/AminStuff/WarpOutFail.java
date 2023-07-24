package com.dillo.main.failsafes.AminStuff;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WarpOutFail {

  @SubscribeEvent
  public void onWorldChange(WorldEvent.Load event) {
    if (!ArmadilloStates.isOnline()) return;
    if (ids.mc.thePlayer == null || ids.mc.theWorld == null) return;

    SendChat.chat(prefix.prefix + "Detected world change! Disabling!");
    ArmadilloStates.offlineState = KillSwitch.OFFLINE;
  }
}
