package com.dillo.commands.RouteCommands;

import static com.dillo.MITGUI.Overlay.curTime;

import com.dillo.dilloUtils.BlockUtils.fileUtils.ReadFileContents;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.File;
import java.util.Objects;
import net.minecraft.util.BlockPos;

public class SelectRouteCommand extends Command {

  public SelectRouteCommand() {
    super("selectRoute");
  }

  @DefaultHandler
  public void handle(String args) {
    if (!Objects.equals(args, "")) {
      if (isValidRoute(args)) {
        args = args.replace(" ", "");
        currentRoute.currentRouteSelected = args;

        if (currentRoute.currentRouteSelected.contains(".json")) {
          currentRoute.currentRouteFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + args);
        } else {
          currentRoute.currentRouteFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + args + ".json");
        }

        JsonObject object = ReadFileContents.readFileContents(currentRoute.currentRouteFile);

        if (object != null) {
          JsonArray currentRouteArr = object.get("route").getAsJsonArray();
          currentRoute.currentRoute.clear();
          if (currentRouteArr.size() > 0) {
            for (JsonElement element : currentRouteArr) {
              JsonObject curObj = element.getAsJsonObject();
              currentRoute.currentRoute.add(
                new BlockPos(curObj.get("x").getAsInt(), curObj.get("y").getAsInt(), curObj.get("z").getAsInt())
              );
            }
          }

          try {
            JsonArray currentStrucArr = object.get("structures").getAsJsonArray();
            currentRoute.strucList.clear();
            if (currentStrucArr.get(0) != null) {
              if (currentStrucArr.size() > 0) {
                for (JsonElement element : currentStrucArr) {
                  JsonObject curObj = element.getAsJsonObject();
                  currentRoute.strucList.add(
                    new BlockPos(curObj.get("x").getAsInt(), curObj.get("y").getAsInt(), curObj.get("z").getAsInt())
                  );
                }
              }
            }
          } catch (Exception e) {}
        } else {
          currentRoute.currentRoute.clear();
          currentRoute.strucList.clear();
        }

        SendChat.chat(prefix.prefix + "Route selected! (Name -> " + currentRoute.currentRouteSelected + ")");
        curTime = System.currentTimeMillis();
      } else {
        SendChat.chat(prefix.prefix + "No route with such name was found! Run /createRoute <name> to create a route!");
      }
    } else {
      SendChat.chat(prefix.prefix + "Please provide a valid route name!");
    }
  }

  public static boolean isValidRoute(String name) {
    File folder = new File(GetConfigFolder.getMcDir() + "/MiningInTwo");

    if (folder.isDirectory()) {
      File[] files = folder.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            if (!name.contains(".json")) {
              if (Objects.equals(name + ".json", file.getName())) return true;
            } else {
              if (Objects.equals(name, file.getName())) return true;
            }
          }
        }
      }
    }

    return false;
  }
}
