package com.dillo.commands.RouteCommands;

import static com.dillo.main.files.readwrite.ReWriteFile.reWriteFile;
import static com.dillo.remote.GetRemoteControl.requestData;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;

public class ImportRouteFromWebsite extends Command {

  public ImportRouteFromWebsite() {
    super("importRouteWebsite");
  }

  @DefaultHandler
  public void handle(String name, String newRouteName) {
    JsonObject element = getRouteWeb(name);

    if (!element.get("stat").getAsBoolean()) {
      SendChat.chat(
        prefix.prefix +
        "It seems as if the route you tried to get does not exist BUT the closest thing i found is -> " +
        element.get("closest").getAsString() +
        ". Did you mean that?"
      );

      return;
    }

    JsonObject obj = element.get("route").getAsJsonObject();
    JsonArray blockArr = obj.get("route").getAsJsonArray();
    JsonArray structureArr = obj.get("structures").getAsJsonArray();

    try {
      File newRoute;

      if (newRouteName.contains(".json")) {
        newRoute = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + newRouteName);
      } else {
        newRoute = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + newRouteName + ".json");
      }

      if (!newRoute.exists()) {
        newRoute.createNewFile();
        currentRoute.currentRouteSelected = newRouteName;
        currentRoute.currentRouteFile = newRoute;
        currentRoute.strucList = new ArrayList<>();
        currentRoute.currentRoute = new ArrayList<>();

        if (blockArr.size() > 0) {
          for (JsonElement element1 : blockArr) {
            JsonObject curObj = element1.getAsJsonObject();
            currentRoute.currentRoute.add(
              new BlockPos(curObj.get("x").getAsInt(), curObj.get("y").getAsInt(), curObj.get("z").getAsInt())
            );
          }
        }

        if (structureArr.size() > 0) {
          for (JsonElement element2 : structureArr) {
            if (!element2.isJsonNull()) {
              JsonObject curObj = element2.getAsJsonObject();
              currentRoute.currentRoute.add(
                new BlockPos(curObj.get("x").getAsInt(), curObj.get("y").getAsInt(), curObj.get("z").getAsInt())
              );
            }
          }
        }

        reWriteFile(newRoute);

        com.dillo.utils.previous.chatUtils.SendChat.chat(
          prefix.prefix + "Route with name " + newRouteName + " created! (and selected)"
        );
      } else {
        SendChat.chat(prefix.prefix + "That route alr exists!");
      }
    } catch (IOException e) {
      com.dillo.utils.previous.chatUtils.SendChat.chat(prefix.prefix + "Failed route creation! Run again or dm me!");
    }
  }

  public JsonObject getRouteWeb(String name) {
    try {
      URL url = new URL("http://localhost:3000/api/getRoute?name=" + name);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      JsonReader jsonReader = new JsonReader(reader);

      JsonObject element = new JsonParser().parse(jsonReader).getAsJsonObject();

      return element;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
