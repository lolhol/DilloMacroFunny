package com.dillo.dilloUtils.BlockUtils.fileUtils;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.util.BlockPos;

public class ReWriteFile {

  public static Gson gson = new Gson();

  public static void reWriteFile(File file) {
    JsonArray arr = new JsonArray();

    for (BlockPos routeBlock : currentRoute.currentRoute) {
      arr.add(gson.toJsonTree(routeBlock));
    }

    JsonArray strucArr = new JsonArray();
    if (currentRoute.strucList.size() > 0) {
      for (BlockPos block : currentRoute.strucList) {
        strucArr.add(gson.toJsonTree(block));
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
  }
}
