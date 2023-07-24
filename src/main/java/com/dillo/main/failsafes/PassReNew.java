package com.dillo.main.failsafes;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PassReNew {

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (
      event.message.getUnformattedText().toLowerCase().contains("you have") &&
      event.message.getUnformattedText().toLowerCase().contains("remaining on your pass.")
    ) {
      ids.mc.thePlayer.sendChatMessage("/purchasecrystallhollowspass");

      new Thread(() -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        SendChat.chat(prefix.prefix + "Purchased pass!");
      })
        .start();
    }
  }
}
