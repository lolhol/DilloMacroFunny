package com.dillo.MITGUI.GUIUtils.CurRatesUtils;

import com.dillo.MITGUI.GUIUtils.StringUtils.StringParser;
import com.dillo.MITGUI.GUIUtils.StringUtils.StringParserClass;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemsPickedUp {

  public static List<List<Integer>> pickedUpGemstones = new ArrayList<>();
  public static long timePoint = System.currentTimeMillis();
  public static long time = 0;
  public static boolean started = false;
  public static JsonObject jsonObject = new JsonObject();
  public static long currTime = System.currentTimeMillis();
  public static boolean first = true;
  public static long firstTime = System.currentTimeMillis();

  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    if (event.message.getUnformattedText().contains("Gemstone")) {
      if (first) {
        SendChat.chat(":?");
        firstTime = System.currentTimeMillis();
      }

      currTime = System.currentTimeMillis();
      started = true;
      List<String> gems = Arrays.asList("RUBY", "AMBER", "TOPAZ", "SAPPHIRE", "AMETHYST", "JASPER", "JADE");
      String unformattedText = event.message.getUnformattedText();
      String lowerText = unformattedText.toLowerCase();

      //System.out.println(event.message.getFormattedText());

      time += System.currentTimeMillis() - timePoint;
      timePoint = System.currentTimeMillis();

      //SendChat.chat(unformattedText + "!!!!");

      for (String gem : gems) {
        if (event.message.getUnformattedText().toLowerCase().contains(gem.toLowerCase())) {
          //SendChat.chat(gem.toLowerCase());
          String jsonGem = "FLAWED_" + gem.toUpperCase();
          if (!jsonObject.has(jsonGem)) {
            StringParserClass result = StringParser.parseStringFlawed(unformattedText);
            jsonObject.addProperty(jsonGem, result.ammount);

            SendChat.chat(jsonObject.get(jsonGem).getAsString());
          } else {
            int currNumber = jsonObject.get(jsonGem).getAsInt();
            StringParserClass result = StringParser.parseStringFlawed(unformattedText);
            //SendChat.chat(result.toString());
            jsonObject.addProperty(jsonGem, result.ammount + currNumber);
            SendChat.chat(jsonObject.get(jsonGem).getAsString());
          }
        }
      }

      first = false;
    }
  }

  public static JsonObject getJsonObject() {
    return jsonObject;
  }

  public static long getTime() {
    return time;
  }

  public static void clearEverything() {
    time = System.currentTimeMillis();
    jsonObject = new JsonObject();
  }
}
