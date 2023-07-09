package com.dillo.dilloUtils.ReFuelDrill;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReFuelDrillTriger {

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (
      event.message.getUnformattedText().toLowerCase().contains("drill") &&
      event.message.getUnformattedText().toLowerCase().contains("empty")
    ) {}
  }
}
