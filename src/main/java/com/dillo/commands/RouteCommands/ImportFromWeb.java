package com.dillo.commands.RouteCommands;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.*;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.minecraft.util.BlockPos;
import org.apache.commons.codec.binary.Base64;

public class ImportFromWeb extends Command {

  public ImportFromWeb() {
    super("importFromWeb");
  }

  @DefaultHandler
  public void handle(String link, String name) {
    new Thread(() -> {
      if (link.contains("pastebin.com")) {
        String finalString = "";
        File fileName = ImportFromClipboard.createFile(name);

        if (!link.startsWith("https://")) {
          finalString = "https://" + link;
        } else {
          finalString = link;
        }

        finalString = convertToRawUrl(finalString);

        SendChat.chat(prefix.prefix + "Downloading data...");

        String data = getData(finalString);

        boolean isBase64 = Base64.isArrayByteBase64(data.getBytes());

        if (!isBase64) {
          completeChanges(data, fileName);
        } else {
          Object[] resultDecode = decodeWaypointData(data);

          //SendChat.chat(String.valueOf((boolean) resultDecode[0]));

          if (resultDecode != null) {
            if (!(boolean) resultDecode[0]) {
              writeFileList((List<BlockPos>) resultDecode[1], fileName);
            } else {
              SendChat.chat(prefix.prefix + "Failed Load! (Maybe wrong format?)");
            }
          }
        }
      } else {
        SendChat.chat(prefix.prefix + "Wrong site! This only supports pastebin.com!");
      }
    })
      .start();
  }

  public static void writeFileList(List<BlockPos> list, File fileName) {
    switchFiles(fileName);
    currentRoute.currentRoute = list;
    Gson gson = new Gson();
    JsonArray arr = new JsonArray();

    for (BlockPos routeBlock : currentRoute.currentRoute) {
      arr.add(gson.toJsonTree(routeBlock));
    }

    JsonArray strucArr = new JsonArray();
    if (currentRoute.strucList.size() > 0) {
      for (BlockPos routeBlock : currentRoute.strucList) {
        strucArr.add(gson.toJsonTree(routeBlock));
      }
    } else {
      strucArr.add(null);
    }

    JsonObject newJson = new JsonObject();
    newJson.add("route", arr);
    newJson.add("structures", strucArr);

    String json = gson.toJson(newJson);

    try {
      FileWriter writer = new FileWriter(currentRoute.currentRouteFile);
      writer.write(json);
      writer.close();
      SendChat.chat(prefix.prefix + "SUCCESS! Route name -> " + fileName.getName().replace("..json", ""));
    } catch (Exception e) {
      System.out.println("HHHHHHHHt");
    }
  }

  private static void switchFiles(File fileName) {
    currentRoute.currentRoute.clear();
    currentRoute.currentRouteFile = fileName;
    currentRoute.currentRouteSelected = fileName.getName();
    currentRoute.currentBlock = null;
  }

  private static void completeChanges(String data, File file) {
    JsonArray json = null;

    try {
      Gson gson = new Gson();
      json = gson.fromJson(data, JsonArray.class);

      switchFiles(file);

      if (json != null) {
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
          JsonObject jsonObject = json.get(i).getAsJsonObject();

          int x = jsonObject.get("x").getAsInt();
          int y = jsonObject.get("y").getAsInt();
          int z = jsonObject.get("z").getAsInt();

          list.add(new BlockPos(x, y, z));
        }

        writeFileList(list, file);
      }

      SendChat.chat(prefix.prefix + "Downloaded and selected -> " + file.getName() + ".");
    } catch (Exception e) {
      SendChat.chat(e.toString());
      SendChat.chat(prefix.prefix + "Failed to download!");
    }
  }

  private static String convertToRawUrl(String originalUrl) {
    try {
      URI uri = new URI(originalUrl);
      String path = uri.getPath();

      if (path.startsWith("/")) {
        path = path.substring(1);
      }

      String rawPath = "/raw/" + path;

      URI rawUri = new URI(uri.getScheme(), uri.getAuthority(), rawPath, uri.getQuery(), uri.getFragment());

      return rawUri.toString();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static String getData(String link) {
    try {
      URL url = new URL(link);
      Scanner sc = new Scanner(url.openStream());
      StringBuffer sb = new StringBuffer();

      while (sc.hasNext()) {
        sb.append(sc.next());
      }

      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Object[] decodeWaypointData(String str) {
    byte[] byteArray = null;
    try {
      byteArray = java.util.Base64.getDecoder().decode(str);
    } catch (Exception e) {
      SendChat.chat(prefix.prefix + "Could not Import route! (Could not decode -> copied wrong thing?)");
      return null;
    }

    DataInputStream dataIS = new DataInputStream(new ByteArrayInputStream(byteArray));

    try {
      List<BlockPos> finalList = new ArrayList<>();
      dataIS.readByte();
      int numbWaypoints = dataIS.readInt();

      for (int i = 0; i < numbWaypoints; i++) {
        dataIS.readUTF();

        BlockPos block = new BlockPos(dataIS.readFloat(), dataIS.readFloat(), dataIS.readFloat());
        finalList.add(block);

        dataIS.readByte();
        dataIS.readByte();
        dataIS.readByte();
        dataIS.readUTF();
        dataIS.readUTF();
      }

      return new Object[] { false, finalList };
    } catch (IOException e) {
      return new Object[] { true, "Error decoding waypoint data!" };
    }
  }
}
