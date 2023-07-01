package com.dillo.commands.RouteCommands;

import static com.dillo.commands.RouteCommands.ImportFromWeb.decodeWaypointData;
import static com.dillo.commands.RouteCommands.ImportFromWeb.writeFileList;

import com.dillo.dilloUtils.BlockUtils.fileUtils.WriteFile;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.minecraft.util.BlockPos;
import org.apache.commons.codec.binary.Base64;

public class ImportFromClipboard extends Command {

  public ImportFromClipboard() {
    super("importRoute");
  }

  @DefaultHandler
  public void handle(String fileName) {
    String clipboard = getClipboardContents();
    JsonArray json = null;

    boolean isBase64 = Base64.isArrayByteBase64(clipboard.getBytes());

    if (!isBase64) {
      try {
        Gson gson = new Gson();
        json = gson.fromJson(clipboard, JsonArray.class);
      } catch (Exception e) {
        //SendChat.chat(String.valueOf(e));
      }

      if (json != null) {
        File endFile = createFile(fileName);
        currentRoute.currentRoute.clear();
        currentRoute.strucList.clear();
        currentRoute.currentRouteFile = endFile;
        currentRoute.currentRouteSelected = fileName;
        currentRoute.currentBlock = null;

        for (int i = 0; i < json.size(); i++) {
          JsonObject jsonObject = json.get(i).getAsJsonObject();

          int x = jsonObject.get("x").getAsInt();
          int y = jsonObject.get("y").getAsInt();
          int z = jsonObject.get("z").getAsInt();

          WriteFile.writeFile(endFile, new BlockPos(x, y, z));
        }

        SendChat.chat(prefix.prefix + "Added route! (name -> " + currentRoute.currentRouteSelected + ")");
      }
    } else {
      Object[] resultDecode = decodeWaypointData(clipboard);

      //SendChat.chat(String.valueOf((boolean) resultDecode[0]));

      if (resultDecode != null) {
        if (!(boolean) resultDecode[0]) {
          File endFile = createFile(fileName);
          currentRoute.currentRoute.clear();
          currentRoute.currentRouteFile = endFile;
          currentRoute.currentRouteSelected = fileName;
          currentRoute.strucList.clear();
          currentRoute.currentBlock = null;
          writeFileList((List<BlockPos>) resultDecode[1], endFile);
          SendChat.chat(prefix.prefix + "Added route! (name -> " + currentRoute.currentRouteSelected + ")");
        } else {
          SendChat.chat(prefix.prefix + "Failed Load! (Maybe wrong format?)");
        }
      }
    }
  }

  public static File createFile(String name) {
    File newRoute = null;

    if (name.contains(".json")) newRoute =
      new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + name); else newRoute =
      new File(GetConfigFolder.getMcDir() + "/MiningInTwo/" + name + ".json");

    try {
      newRoute.createNewFile();
    } catch (IOException e) {}

    return newRoute;
  }

  public static String getClipboardContents() {
    try {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable transferable = clipboard.getContents(null);

      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        return (String) transferable.getTransferData(DataFlavor.stringFlavor);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
