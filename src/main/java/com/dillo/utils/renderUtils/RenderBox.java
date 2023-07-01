package com.dillo.utils.renderUtils;

import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderBox {

  public static void drawBox(double x, double y, double z, Color color, float width, float partialTicks) {
    Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
    double x1 = x - (viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks);
    double y1 = y - (viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks);
    double z1 = z - (viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks);
    GlStateManager.pushMatrix();
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.disableDepth();
    GlStateManager.disableCull();
    GlStateManager.disableLighting();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GL11.glLineWidth(width);
    RenderGlobal.drawOutlinedBoundingBox(
      new AxisAlignedBB(x1, y1, z1, x1 + 1, y1 + 1, z1 + 1),
      color.getRed(),
      color.getGreen(),
      color.getBlue(),
      color.getAlpha()
    );
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.enableCull();
  }
}
