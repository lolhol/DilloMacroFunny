package com.dillo.dilloUtils.FailSafes.AminStuff;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.utils.previous.random.ids;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WarpOutFail {

  @SubscribeEvent
  public void onWorldChange(WorldEvent.Load event) {
    if (!ArmadilloStates.isOnline()) return;
    if (ids.mc.thePlayer == null || ids.mc.theWorld == null) return;

    ArmadilloStates.offlineState = KillSwitch.OFFLINE;
  }
}
