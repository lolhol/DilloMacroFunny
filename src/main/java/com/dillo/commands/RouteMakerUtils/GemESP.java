package com.dillo.commands.RouteMakerUtils;

import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class GemESP extends Command {

  public static boolean isRenderGems = false;

  public GemESP() {
    super("gemESP");
  }

  @DefaultHandler
  public void handle() {
    isRenderGems = !isRenderGems;

    String str = isRenderGems ? "Started Rendering!" : "Stopped Rendering!";
    SendChat.chat(prefix.prefix + str);
    ids.mc.renderGlobal.loadRenderers();
  }
}
