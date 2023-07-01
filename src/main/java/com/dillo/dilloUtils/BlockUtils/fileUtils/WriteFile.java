package com.dillo.dilloUtils.BlockUtils.fileUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.BlockPos;

public class WriteFile {

  public static Gson gson = new Gson();

  public static void writeFile(File file, BlockPos block) {
    if (!Objects.equals(currentRoute.currentRouteSelected, "none")) {
      currentRoute.currentRoute.add(block);
      JsonArray arr = new JsonArray();

      for (BlockPos routeBlock : currentRoute.currentRoute) {
        arr.add(gson.toJsonTree(routeBlock));
      }

      JsonArray strucArr = new JsonArray();
      if (currentRoute.strucList.size() > 0) {
        for (BlockPos blockPos : currentRoute.strucList) {
          strucArr.add(gson.toJsonTree(blockPos));
        }
      } else {
        strucArr.add(null);
      }

      JsonObject newJson = new JsonObject();
      newJson.add("route", arr);
      newJson.add("structures", strucArr);

      String json = gson.toJson(newJson);

      try {
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
      } catch (Exception e) {
        System.out.println("HHHHHHHHt");
      }
    } else {
      SendChat.chat(prefix.prefix + "Please select a route!");
    }
  }
}
