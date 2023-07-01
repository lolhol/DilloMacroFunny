package com.dillo.utils.previous.chatUtils;

import com.dillo.utils.previous.random.prefix;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class sendCommandInChat {

  private static final Minecraft mc = Minecraft.getMinecraft();

  public static void chat(String msg) {
    mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(prefix.prefix + msg));
  }
}
