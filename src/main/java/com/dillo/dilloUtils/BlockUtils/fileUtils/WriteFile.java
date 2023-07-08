package com.dillo.dilloUtils.BlockUtils.fileUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Objects;
import net.minecraft.util.BlockPos;

public class WriteFile {

  public static Gson gson = new Gson();

  public static void writeFile(File file, BlockPos block) {
    if (!Objects.equals(currentRoute.currentRouteSelected, "none")) {
      currentRoute.currentRoute.add(block);
      JsonArray arr = new JsonArray();

      for (BlockPos routeBlock : currentRoute.currentRoute) {
        JsonObject newJson = new JsonObject();
        newJson.add("x", new JsonPrimitive(routeBlock.getX()));
        newJson.add("y", new JsonPrimitive(routeBlock.getY()));
        newJson.add("z", new JsonPrimitive(routeBlock.getX()));
        arr.add(newJson);
      }

      JsonArray strucArr = new JsonArray();
      if (currentRoute.strucList.size() > 0) {
        for (BlockPos blockPos : currentRoute.strucList) {
          JsonObject newJson = new JsonObject();
          newJson.add("x", new JsonPrimitive(blockPos.getX()));
          newJson.add("y", new JsonPrimitive(blockPos.getY()));
          newJson.add("z", new JsonPrimitive(blockPos.getX()));
          strucArr.add(newJson);
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
