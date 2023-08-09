package com.dillo.remote;

import com.dillo.config.config;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class RemoteControlChat {

    public static List<String> chatArray = new ArrayList<>();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (config.remoteControl) {
            chatArray.add(event.message.getUnformattedText());
        }
    }
}
