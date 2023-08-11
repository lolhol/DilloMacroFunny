package com.dillo.main.files.readwrite;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.dillo.main.files.readwrite.WriteFile.gson;

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

        if (!content.toString().equals("null") && !content.toString().equals("") && !content.toString().equals(" ")) {
            return gson.fromJson(content.toString(), JsonObject.class);
        } else {
            return null;
        }
    }
}
