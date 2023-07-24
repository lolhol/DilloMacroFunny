package com.dillo.main.scan.Utils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.util.ChatComponentText;

public class ModUtils {

  public static void sendMessage(Object object) {
    String message = "null";
    if (object != null) {
      message = object.toString().replace("&", "§");
    }
    if (ids.mc.thePlayer != null) {
      ids.mc.thePlayer.addChatMessage(new ChatComponentText("§7[§d FFFF §7] §f" + message));
    }
  }
}
