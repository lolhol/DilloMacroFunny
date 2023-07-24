package com.dillo.commands.RouteCommands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class CurrentSelected extends Command {

  public CurrentSelected() {
    super("currentRoute");
  }

  @DefaultHandler
  public void handle() {
    if (
      currentRoute.currentRouteSelected == null ||
      currentRoute.currentRouteSelected.equals("") ||
      currentRoute.currentRouteSelected.equals("none")
    ) {
      SendChat.chat(prefix.prefix + "No route is currently selected!");
    } else {
      SendChat.chat(prefix.prefix + currentRoute.currentRouteSelected + " is currently selected!");
    }
  }
}
