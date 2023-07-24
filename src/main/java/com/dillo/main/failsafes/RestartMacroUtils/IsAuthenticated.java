package com.dillo.main.failsafes.RestartMacroUtils;

import com.dillo.remote.GetRemoteControl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class IsAuthenticated {

  public static boolean isAuthenticated() {
    String resultData = getDataFromWebsite(
      "http://localhost:3000/api/getUserPerms?name=" + GetRemoteControl.getCurrentUsername().toLowerCase()
    );

    if (resultData != null) {
      return resultData.contains("true");
    }

    return false;
  }

  public static String getDataFromWebsite(String website) {
    try {
      URL url = new URL(website); // Replace with the actual URL of your webpage
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      JsonReader jsonReader = new JsonReader(reader);

      JsonElement element = new JsonParser().parse(jsonReader);
      JsonArray jsonArray = element.getAsJsonArray();
      JsonElement firstElement = jsonArray.get(0);

      return firstElement.getAsString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
