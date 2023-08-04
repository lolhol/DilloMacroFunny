package com.dillo.gui.util;

import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * From Aurora :D
 */

public class FontDefiner {

  public static FontRender getFontRenderer(float size) {
    FontRender tempRenderer;
    try {
      tempRenderer =
        new FontRender(
          Font
            .createFont(
              Font.TRUETYPE_FONT,
              Minecraft.getMinecraft().getResourceManager().getResource(getFontLocation()).getInputStream()
            )
            .deriveFont(Font.PLAIN, size),
          true,
          false
        );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return tempRenderer;
  }

  public static FontRender getFontRenderer() {
    return getFontRenderer(19f);
  }

  private static ResourceLocation getFontLocation() {
    String location = "funny-textures:";
    /*switch (Config.hudFont) {
      case 0:
        location += "kanit.ttf";
        break;
      case 1:
        location += "oldkanit.ttf";
        break;
      case 2:
        location += "dys.ttf";
        break;
    }*/
    return new ResourceLocation(location);
  }
}