package com.dillo.commands.ConfigCommands;

import static com.dillo.main.files.readwrite.WriteFile.gson;

import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.io.*;
import java.lang.reflect.Field;

public class SelectConfig extends Command {

  public SelectConfig() {
    super("selectConfig");
  }

  @DefaultHandler
  public void handle(String name) {
    File configFile;

    if (name.contains(".json")) {
      configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name);
    } else {
      configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name + ".json");
    }

    if (!configFile.exists()) {
      SendChat.chat(prefix.prefix + "There is no config with that name!");
      return;
    }

    currentRoute.curConfig = configFile;

    StringBuilder content = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    JsonArray obj;
    if (!content.toString().equals("null") && !content.toString().equals("") && !content.toString().equals(" ")) {
      obj = gson.fromJson(content.toString(), JsonArray.class);

      for (Field field : config.class.getFields()) {
        for (JsonElement object : obj) {
          JsonObject object1 = object.getAsJsonObject();
          if (object1.has(field.getName())) {
            String contents = object1.get(field.getName()).getAsString();

            try {
              boolean cont = Boolean.parseBoolean(contents);
              field.set(null, cont);
              continue;
            } catch (NumberFormatException e) {
              SendChat.chat("<<<");
            } catch (IllegalAccessException e) {
              SendChat.chat("???");
              throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
              try {
                field.set(null, Integer.parseInt(contents));
              } catch (Exception ev2) {
                SendChat.chat("!!!");
              }
            }
          }
        }
      }

      SendChat.chat(prefix.prefix + "Selected " + name + "!");
    }

    File mainConfig = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/configsMain.json");

    try {
      FileWriter writer = new FileWriter(mainConfig);
      writer.write(name);
      writer.close();
    } catch (Exception e) {
      SendChat.chat(prefix.prefix + "Failed backing up into file!");
    }
  }
}
