package com.dillo.main.teleport.utils;

import com.dillo.events.PlayerLocChangeEvent;
import com.dillo.utils.previous.SendChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Test {

  @SubscribeEvent
  public void onChange(PlayerLocChangeEvent event) {
    SendChat.chat("CHNAGE!");
  }
}
