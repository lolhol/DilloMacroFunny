package com.dillo.commands.RouteCommands;

import com.dillo.gui.GUIUtils.CheckRoute.GetFailPointsList;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class ObstructedPoints extends Command {

  public ObstructedPoints() {
    super("obstructedPoints");
  }

  @DefaultHandler
  public void handle() {
    SendChat.chat(prefix.prefix + "The points are: ");

    for (int point : GetFailPointsList.failListPoints) {
      SendChat.chat("Point -> " + ((int) point + 1));
    }
  }
}
