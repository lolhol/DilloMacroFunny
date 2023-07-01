package com.dillo.utils;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class GetConfigFolder {

  public static File getMcDir() {
    if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isDedicatedServer()) {
      return new File("");
    }

    String string = String.valueOf(Minecraft.getMinecraft().mcDataDir);

    return new File(string);
  }
}
