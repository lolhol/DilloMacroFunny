package com.dillo.main.utils;

import com.dillo.events.utilevents.CurJumpProgress;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class JumpProgressRegister {

  boolean isOnDillo = false;
  double prevY = 0;
  double maxY = 0;
  boolean isFirst = true;

  public void startStop(boolean state) {
    isOnDillo = state;
  }

  public void reset() {
    maxY = 0;
    isOnDillo = false;
    MinecraftForge.EVENT_BUS.post(new CurJumpProgress(0, maxY, true));
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isOnDillo) return;

    try {
      if (ids.mc.thePlayer.posY > prevY && !isFirst) {
        maxY += ids.mc.thePlayer.posY - prevY;
        MinecraftForge.EVENT_BUS.post(new CurJumpProgress(ids.mc.thePlayer.posY - prevY, maxY, false));
      }

      SendChat.chat(String.valueOf(ids.mc.thePlayer.posY - prevY) + "!!!");

      prevY = ids.mc.thePlayer.posY;
      isFirst = false;
    } catch (NullPointerException e) {
      SendChat.chat("ERR");
    }
  }
}
