package com.dillo.dilloUtils.TpUtils;

import com.dillo.ArmadilloMain.ArmadilloStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WalkForward {

  private static boolean start = false;
  private static int curTicks = 0;
  private static int totalTicks = 0;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;
  private static String stateAfter = null;

  public static void walkForward(int totalTime, String state) {
    totalTicks = totalTime;
    start = true;
    stateAfter = state;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (ArmadilloStates.offlineState.equals("online") && start) {
      if (totalTicks > curTicks) {
        KeyBinding.setKeyBindState(forward.getKeyCode(), true);
        ArmadilloStates.currentState = stateAfter;
        curTicks++;
      } else {
        curTicks = 0;
        totalTicks = 0;
        start = false;
        KeyBinding.setKeyBindState(forward.getKeyCode(), false);
      }
    }
  }
}
