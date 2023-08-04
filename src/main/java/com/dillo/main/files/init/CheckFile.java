package com.dillo.main.files.init;

import static com.dillo.main.files.readwrite.WriteFile.gson;

import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.remote.GetRemoteControl;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckFile {

  private static List<List<String>> defaultAcusations = new ArrayList<List<String>>();
  public static File file = new File(GetConfigFolder.getMcDir() + "/MiningInTwo");
  public static File configs = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs");
  public static File configMain = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/configsMain.json");
  public static File configDefault = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/defaultConfig.json");
  public static File configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/default-route.json");
  public static File answersFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json");
  private static boolean startCheck = false;
  private static String name = "";

  public static void checkFiles() {
    if (!file.exists()) {
      file.mkdirs();
    }

    if (!configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (IOException e) {}
    }

    if (!configs.exists()) {
      configs.mkdirs();
    }

    StringBuilder content = new StringBuilder();
    if (!configMain.exists()) {
      try {
        configMain.createNewFile();
      } catch (IOException e) {}
    } else {
      try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
          content.append(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    StringBuilder contentDefault = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(configDefault))) {
      String line;
      while ((line = reader.readLine()) != null) {
        contentDefault.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (
      configDefault.exists() &&
      !Objects.equals(contentDefault.toString(), "") &&
      !Objects.equals(contentDefault.toString(), " ")
    ) {
      if (!content.toString().equals("") && !content.toString().equals(" ")) {
        String string;
        string = content.toString();
        selectConfigFromName(string);
      } else {
        selectConfigFromName("defaultConfig");
      }
    } else {
      try {
        configDefault.createNewFile();
      } catch (IOException e) {}

      presetConfig(new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/defaultConfig.json"));
      selectConfigFromName("defaultConfig");
    }

    if (!answersFile.exists()) {
      Gson gson = new Gson();

      try {
        answersFile.createNewFile();
        makeAcusations();
        writeStringFile(answersFile, gson.toJson(defaultAcusations));
      } catch (IOException e) {}
    }
  }

  public static void writeStringFile(File file, String text) {
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(text);
      writer.close();
    } catch (Exception e) {}
  }

  private static void makeAcusations() {
    JsonArray arr = GetRemoteControl.requestData("http://localhost:3000/api/default-acusations");

    if (arr != null) {
      for (JsonElement accusation : arr) {
        JsonArray inside = new JsonParser().parse(accusation.toString()).getAsJsonArray();
        //System.out.println(inside.get(0) + "/dfmdkfkmdkmf");

        List<String> currAccusation = new ArrayList<>();
        currAccusation.add(inside.get(0).getAsString());
        currAccusation.add(inside.get(1).getAsString());

        defaultAcusations.add(currAccusation);
      }
    }
  }

  private static void selectConfigFromName(String name) {
    if (Objects.equals(name, "") || Objects.equals(name, " ") || name == null) return;

    File configFile;

    if (name.contains(".json")) {
      configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name);
    } else {
      configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name + ".json");
    }

    try {
      if (!configFile.exists()) {
        SendChat.chat(prefix.prefix + "There is no config with that name!");
        return;
      }
    } catch (NullPointerException e) {
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
            } catch (NumberFormatException e) {} catch (IllegalAccessException e) {
              throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
              try {
                field.set(null, Integer.parseInt(contents));
              } catch (Exception ev2) {}
            }
          }
        }
      }
    }
  }

  public static void presetConfig(File file) {
    JsonArray main = new JsonArray();
    Gson gson = new Gson();

    for (Field field : config.class.getFields()) {
      try {
        if (!field.getName().equals("INSTANCE")) {
          JsonObject jsonSub = new JsonObject();
          jsonSub.add(field.getName(), new JsonPrimitive(field.get(null).toString()));
          main.add(jsonSub);
        }
      } catch (IllegalAccessException e) {}
    }

    String mainJsonString = gson.toJson(main);

    try {
      FileWriter writer = new FileWriter(file);
      writer.write(mainJsonString);
      writer.close();
    } catch (Exception e) {
      SendChat.chat(prefix.prefix + "Failed backing up into file!");
    }
  }
}
