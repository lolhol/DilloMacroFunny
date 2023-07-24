package com.dillo.commands.RouteCommands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.files.readwrite.ReWriteFile;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class ClearStructures extends Command {

  public ClearStructures() {
    super("clearStructures");
  }

  @DefaultHandler
  public void handle() {
    if (currentRoute.strucList.size() > 0) {
      currentRoute.strucList.clear();
      ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
    } else {
      SendChat.chat(prefix.prefix + "The route that you selected does not have any structures!");
    }
  }
}
