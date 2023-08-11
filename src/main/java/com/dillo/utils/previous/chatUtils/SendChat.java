package com.dillo.utils.previous.chatUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import static com.dillo.config.config.antiSpam;

public class SendChat {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void chat(String msg) {
        if (!antiSpam) {
            mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(msg));
        }
    }
}
