package com.dillo.dilloUtils.ReFuelDrill;

import static com.dillo.data.config.refuel;
import static com.dillo.dilloUtils.ReFuelDrill.ReFuelDrill.reFuelDrill;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReFuelDrillTriger {

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (
      event.message.getUnformattedText().toLowerCase().contains("drill") &&
      event.message.getUnformattedText().toLowerCase().contains("empty") &&
      refuel
    ) {
      reFuelDrill();
      SendChat.chat(prefix.prefix + "Re-fueling Drill!");
    }
  }
}
