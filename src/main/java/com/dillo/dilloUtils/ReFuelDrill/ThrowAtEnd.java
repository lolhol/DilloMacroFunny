package com.dillo.dilloUtils.ReFuelDrill;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ThrowAtEnd {

  public static boolean isThrow = false;

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (ReFuelDrill.isStart) {
      if (event.message.getUnformattedText().toLowerCase().contains("equipped")) {
        if (event.message.getUnformattedText().toLowerCase().contains("armadillo")) {
          isThrow = true;
        } else {
          isThrow = false;
        }
        ReFuelDrill.isStart = false;
      }
    }
  }
}
