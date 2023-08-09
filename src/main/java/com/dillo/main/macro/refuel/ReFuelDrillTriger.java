package com.dillo.main.macro.refuel;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.dillo.config.config.refuel;
import static com.dillo.main.macro.refuel.ReFuelDrill.reFuelDrill;

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
