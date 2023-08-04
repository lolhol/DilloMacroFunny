package com.dillo.config;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.FileWriter;
import java.lang.reflect.Field;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoSaveConfig {

  @SubscribeEvent
  public void onTick(TickEvent event) {
    if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      backUpFile();
      //ids.mc.displayGuiScreen(new ModuleEditor());
    }
  }

  public void backUpFile() {
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
      FileWriter writer = new FileWriter(currentRoute.curConfig);
      writer.write(mainJsonString);
      writer.close();
    } catch (Exception e) {
      SendChat.chat(String.valueOf(currentRoute.curConfig));
      SendChat.chat(prefix.prefix + "Failed backing up into file!");
    }
  }
}
