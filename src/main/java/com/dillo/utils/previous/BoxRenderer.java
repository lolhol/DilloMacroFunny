package com.dillo.utils.previous;

//import com.sun.prism.impl.VertexBuffer;

import com.dillo.utils.degreeToRad;
import com.dillo.utils.previous.random.ids;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class BoxRenderer {

  private final Minecraft mc = Minecraft.getMinecraft();

  public static void drawOutlinedBoundingBox(BlockPos block, Color color, float width, float partialTicks) {
    Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
    double x = block.getX() - (viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks);
    double y = block.getY() - (viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks);
    double z = block.getZ() - (viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks);
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
      new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1),
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

  public static void drawLine(float angle) {
    double radians = degreeToRad.degreeToRad(angle);
    double dx = Math.cos(radians);
    double dz = Math.sin(radians);
    double length = 5;

    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    GlStateManager.pushMatrix();
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.disableDepth();
    GlStateManager.disableCull();
    GlStateManager.disableLighting();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GL11.glLineWidth(1);

    worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);

    worldrenderer
      .pos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ)
      .color(100, 100, 50, 0)
      .endVertex();
    worldrenderer
      .pos(ids.mc.thePlayer.posX + dx * length, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ + dz * length)
      .color(100, 100, 50, 0)
      .endVertex();

    tessellator.draw();

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
