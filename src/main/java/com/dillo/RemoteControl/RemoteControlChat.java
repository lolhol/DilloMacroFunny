package com.dillo.RemoteControl;

import com.dillo.data.config;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
