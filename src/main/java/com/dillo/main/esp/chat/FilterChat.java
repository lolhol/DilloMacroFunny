package com.dillo.main.esp.chat;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.dillo.config.config.removeProcs;
import static com.dillo.gui.GUIUtils.StringUtils.StringParser.parseStringFlawed;

public class FilterChat {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (
                event.message.getUnformattedText().contains("Gemstone") &&
                        parseStringFlawed(event.message.getUnformattedText()) != null &&
                        removeProcs
        ) {
            event.message = null;
        }
    }
}
