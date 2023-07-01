package com.dillo.commands.RouteCommands;

import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.File;
import java.util.Objects;

public class RoutesInFolder extends Command {

  public RoutesInFolder() {
    super("currentRoutes");
  }

  @DefaultHandler
  public void handle() {
    SendChat.chat("The routes in your file are...");
    getAllRoutes();
  }

  public static void getAllRoutes() {
    File folder = new File(GetConfigFolder.getMcDir() + "/MiningInTwo");

    if (folder.isDirectory()) {
      File[] files = folder.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            SendChat.chat(file.getName().replace("..json", ""));
          }
        }
      }
    }
  }
}
