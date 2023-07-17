package com.dillo.commands.RouteMakerUtils;

import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;

public class GemESP extends Command {

  public static boolean isRenderGems = false;

  public GemESP() {
    super("gemESP");
  }

  @DefaultHandler
  public void handle() {
    SendChat.chat("Rendering");
    isRenderGems = !isRenderGems;

    new Thread(() -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      Minecraft.getMinecraft().refreshResources();
    })
      .start();
  }
}
