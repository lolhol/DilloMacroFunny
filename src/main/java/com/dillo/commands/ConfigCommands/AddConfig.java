package com.dillo.commands.ConfigCommands;

import com.dillo.config.config;
import com.dillo.utils.GetConfigFolder;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class AddConfig extends Command {

    public AddConfig() {
        super("addConfig");
    }

    @DefaultHandler
    public void handle(String name) {
        File newConfig;

        if (name.contains(".json")) {
            newConfig = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name);
        } else {
            newConfig = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/" + name + ".json");
        }

        if (newConfig.exists()) {
            SendChat.chat(prefix.prefix + "A config with that name already exists!");
            return;
        }

        try {
            newConfig.createNewFile();
        } catch (IOException e) {
        }

        JsonArray main = new JsonArray();
        Gson gson = new Gson();

        for (Field field : config.class.getFields()) {
            try {
                if (!field.getName().equals("INSTANCE")) {
                    JsonObject jsonSub = new JsonObject();
                    jsonSub.add(field.getName(), new JsonPrimitive(field.get(null).toString()));
                    main.add(jsonSub);
                }
            } catch (IllegalAccessException e) {
            }
        }

        String mainJsonString = gson.toJson(main);

        try {
            FileWriter writer = new FileWriter(newConfig);
            writer.write(mainJsonString);
            writer.close();
        } catch (Exception e) {
            SendChat.chat(prefix.prefix + "Failed backing up into file!");
        }

        File mainConfig = new File(GetConfigFolder.getMcDir() + "/MiningInTwo/configs/configsMain.json");

        try {
            FileWriter writer = new FileWriter(mainConfig);
            writer.write(name);
            writer.close();
        } catch (Exception e) {
            SendChat.chat(prefix.prefix + "Failed backing up into file!");
        }

        SendChat.chat(prefix.prefix + "Created successfully!");
    }
}
