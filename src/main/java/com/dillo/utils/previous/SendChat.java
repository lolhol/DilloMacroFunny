package com.dillo.utils.previous;

import static com.dillo.config.config.antiSpam;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SendChat {

  private static final Minecraft mc = Minecraft.getMinecraft();

  public static void chat(String msg) {
    if (!antiSpam) {
      mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(msg));
    }
  }
}
