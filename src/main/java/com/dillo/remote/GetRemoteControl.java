package com.dillo.remote;

import com.dillo.config.config;
import com.dillo.utils.previous.random.ids;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GetRemoteControl {

    public static JsonArray remoteControlActions = new JsonArray();
    private static int currentTicks = 0;

    public static String getCurrentUsername() {
        EntityPlayer player = ids.mc.thePlayer;
        return player.getName();
    }

    public static JsonArray requestData(String link) {
        try {
            URL url = new URL(link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonReader jsonReader = new JsonReader(reader);

            JsonElement element = new JsonParser().parse(jsonReader);

            return element.getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (config.remoteControl) {
                if (currentTicks >= config.timeBetweenFetches * 20) {
                    Gson gson = new Gson();
                    currentTicks = 0;

                    String currChat = gson.toJson(RemoteControlChat.chatArray);
                    String encodedChat = null;
                    try {
                        encodedChat = URLEncoder.encode(currChat, "UTF-8");
                    } catch (java.io.UnsupportedEncodingException e) {
                        //SendChat.chat(e.toString());
                    }

                    JsonArray output = requestData(
                            "http://localhost:3000/api/getUserData?name=" + getCurrentUsername().toLowerCase() + "&chat=" + encodedChat
                    );
                    //SendChat.chat(String.valueOf(output));
                    RemoteControlChat.chatArray = new ArrayList<>();
                    if (output != null) {
                        remoteControlActions = output;
                    }
                } else {
                    currentTicks++;
                }
            }
        }
    }
}
