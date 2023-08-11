package com.dillo.main.files.readwrite;

import com.dillo.main.files.localizedData.currentRoute;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.io.FileWriter;

public class ReWriteFile {

    public static Gson gson = new Gson();

    public static void reWriteFile(File file) {
        JsonArray arr = new JsonArray();

        for (BlockPos routeBlock : currentRoute.currentRoute) {
            JsonObject newJson = new JsonObject();
            newJson.add("x", new JsonPrimitive(routeBlock.getX()));
            newJson.add("y", new JsonPrimitive(routeBlock.getY()));
            newJson.add("z", new JsonPrimitive(routeBlock.getZ()));
            arr.add(newJson);
        }

        JsonArray strucArr = new JsonArray();
        if (currentRoute.strucList.size() > 0) {
            for (BlockPos blockPos : currentRoute.strucList) {
                JsonObject newJson = new JsonObject();
                newJson.add("x", new JsonPrimitive(blockPos.getX()));
                newJson.add("y", new JsonPrimitive(blockPos.getY()));
                newJson.add("z", new JsonPrimitive(blockPos.getZ()));
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
    }
}
