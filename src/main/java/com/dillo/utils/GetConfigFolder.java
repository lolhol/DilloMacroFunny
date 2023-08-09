package com.dillo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import java.io.File;

public class GetConfigFolder {

    public static File getMcDir() {
        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isDedicatedServer()) {
            return new File("");
        }

        String string = String.valueOf(Minecraft.getMinecraft().mcDataDir);

        return new File(string);
    }
}
