package com.dillo.dilloUtils.BlockUtils.fileUtils;

import static com.dillo.dilloUtils.BlockUtils.fileUtils.WriteFile.gson;

import com.google.common.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import net.minecraft.util.BlockPos;

public class ReadFileContents {

  public static JsonObject readFileContents(File file) {
    StringBuilder content = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("________________________________________________________________");
    System.out.println(content.toString());
    System.out.println("________________________________________________________________");

    if (!content.toString().equals("null") && !content.toString().equals("") && !content.toString().equals(" ")) {
      return gson.fromJson(content.toString(), JsonObject.class);
    } else {
      return null;
    }
  }
}
