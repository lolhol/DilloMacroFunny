package com.dillo.main.esp.player;

import com.dillo.events.MillisecondEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class FreeLook {

  private boolean flying = false;
  public static boolean isAdd;

  @SubscribeEvent
  public void onMouseEvent(MillisecondEvent event) {
    isAdd = Keyboard.isKeyDown(Keyboard.KEY_I);
  }
  /*@SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START && flying) {}
  }*/
}
