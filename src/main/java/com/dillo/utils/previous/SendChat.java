package com.dillo.utils.previous;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SendChat {

  private static final Minecraft mc = Minecraft.getMinecraft();

  public static void chat(String msg) {
    mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(msg));
  }
}
