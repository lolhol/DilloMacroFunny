package com.dillo.RemoteControl;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Movements {

  private static KeyBinding movement = null;
  private static int walkTime = 0;
  private static boolean start = false;
  private static int currTime = 0;

  public static void movements(KeyBinding keybind, int time) {
    if (keybind != null && time > 0) {
      movement = keybind;
      walkTime = time;
      start = true;
      KeyBinding.setKeyBindState(keybind.getKeyCode(), true);
    }
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (start) {
        if (currTime >= walkTime) {
          KeyBinding.setKeyBindState(movement.getKeyCode(), false);
          movement = null;
          walkTime = 0;
          start = false;
          currTime = 0;
          System.out.println("!!!!!1");
        } else {
          currTime++;
        }
      }
    }
  }
}
