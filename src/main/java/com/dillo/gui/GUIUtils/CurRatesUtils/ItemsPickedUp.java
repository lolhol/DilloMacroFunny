package com.dillo.gui.GUIUtils.CurRatesUtils;

import com.dillo.gui.GUIUtils.StringUtils.StringParser;
import com.dillo.gui.GUIUtils.StringUtils.StringParserClass;
import com.google.gson.JsonObject;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dillo.gui.GUIUtils.StringUtils.StringParser.parseStringFlawed;

public class ItemsPickedUp {

    public static List<List<Integer>> pickedUpGemstones = new ArrayList<>();
    public static long timePoint = System.currentTimeMillis();
    public static long time = 0;
    public static boolean started = false;
    public static JsonObject jsonObject = new JsonObject();
    public static long currTime = System.currentTimeMillis();
    public static boolean first = true;
    public static long firstTime = System.currentTimeMillis();

    public static JsonObject getJsonObject() {
        return jsonObject;
    }

    public static long getTime() {
        return time;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (
                event.message.getUnformattedText().contains("Gemstone") &&
                        parseStringFlawed(event.message.getUnformattedText()) != null
        ) {
            if (first) {
                //SendChat.chat(":?");
                firstTime = System.currentTimeMillis();
            }

            currTime = System.currentTimeMillis();
            started = true;
            List<String> gems = Arrays.asList("RUBY", "AMBER", "TOPAZ", "SAPPHIRE", "AMETHYST", "JASPER", "JADE");
            String unformattedText = event.message.getUnformattedText();
            String lowerText = unformattedText.toLowerCase();

            time += System.currentTimeMillis() - timePoint;
            timePoint = System.currentTimeMillis();

            for (String gem : gems) {
                if (event.message.getUnformattedText().toLowerCase().contains(gem.toLowerCase())) {
                    //SendChat.chat(gem.toLowerCase());
                    String jsonGem = "FLAWED_" + gem.toUpperCase();
                    if (!jsonObject.has(jsonGem)) {
                        StringParserClass result = StringParser.parseStringFlawed(unformattedText);
                        jsonObject.addProperty(jsonGem, result.ammount);
                        //SendChat.chat(jsonObject.get(jsonGem).getAsString());
                    } else {
                        int currNumber = jsonObject.get(jsonGem).getAsInt();
                        StringParserClass result = StringParser.parseStringFlawed(unformattedText);
                        //SendChat.chat(result.toString());
                        jsonObject.addProperty(jsonGem, result.ammount + currNumber);
                        //SendChat.chat(jsonObject.get(jsonGem).getAsString());
                    }
                }
            }

            first = false;
        }
    }
}
