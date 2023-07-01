package com.dillo.dilloUtils;

import com.dillo.RemoteControl.GetRemoteControl;
import com.dillo.utils.GetConfigFolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckFile {
  private static List<List<String>> defaultAcusations = new ArrayList<List<String>>();
  public static File file = new File(GetConfigFolder.getMcDir() + "/MiningInTwo");
  public static File configFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/default-route.json");
  public static File answersFile = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/chatAnswers.json");

  public static void checkFiles() {
    if (!file.exists()) {
      file.mkdirs();
    }

    if (!configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (IOException e) {
      }
    }

    if (!answersFile.exists()) {
      Gson gson = new Gson();

      try {
        answersFile.createNewFile();
        makeAcusations();
        writeStringFile(answersFile, gson.toJson(defaultAcusations));
      } catch (IOException e) {
        System.out.println("Error creating ________________________________________________________________________________________________________________________________");
      }
    }
  }

  public static void writeStringFile(File file, String text) {
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(text);
      writer.close();
    } catch (Exception e) {

    }
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
}
