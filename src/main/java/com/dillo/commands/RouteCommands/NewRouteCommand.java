package com.dillo.commands.RouteCommands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewRouteCommand extends Command {

  public NewRouteCommand() {
    super("createRoute");
  }

  @DefaultHandler
  public void handle(String name) {
    if (name != null && !name.equals(" ")) {
      if (!SelectRouteCommand.isValidRoute(name)) {
        try {
          File newRoute = null;

          if (name.contains(".json")) {
            newRoute = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + name);
          } else {
            newRoute = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + name + ".json");
          }

          newRoute.createNewFile();
          currentRoute.currentRouteSelected = name;
          currentRoute.currentRouteFile = newRoute;
          currentRoute.strucList = new ArrayList<>();
          currentRoute.currentRoute = new ArrayList<>();
          SendChat.chat(prefix.prefix + "Route with name " + name + " created! (and selected)");
        } catch (IOException e) {
          SendChat.chat(prefix.prefix + "Failed route creation! Run again or dm me!");
        }
      } else {
        SendChat.chat(prefix.prefix + "Route with name " + name + " already exists!");
      }
    } else {
      SendChat.chat(prefix.prefix + "Please enter a valid route name!");
    }
  }
}
