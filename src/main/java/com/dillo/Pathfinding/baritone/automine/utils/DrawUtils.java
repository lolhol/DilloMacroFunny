package com.dillo.Pathfinding.baritone.automine.utils;

import static java.lang.Math.sqrt;

import java.awt.*;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

public class DrawUtils {

  private static final Minecraft mc = Minecraft.getMinecraft();

  private static void drawLine(WorldRenderer bufferBuilder, Vec3 pos, Vec3 nextPos, Color color) {
    bufferBuilder
      .pos(pos.xCoord, pos.yCoord, pos.zCoord)
      .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
      .endVertex();
    bufferBuilder
      .pos(nextPos.xCoord, nextPos.yCoord, nextPos.zCoord)
      .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
      .endVertex();
  }

  public static void drawLine(RenderWorldLastEvent event, Vec3 from, Vec3 to, float lineWidth, Color color) {
    final Entity render = mc.getRenderViewEntity();
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
    final double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * event.partialTicks;
    final double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * event.partialTicks;
    final double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * event.partialTicks;
    GlStateManager.pushMatrix();
    GlStateManager.translate(-realX, -realY, -realZ);
    GlStateManager.disableTexture2D();
    GlStateManager.disableLighting();
    GL11.glDisable(3553);
    GL11.glLineWidth(lineWidth);
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.disableDepth();
    GlStateManager.depthMask(false);
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color(1f, 1f, 1f, 1f);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

    DrawUtils.drawLine(bufferBuilder, from, to, color);

    tessellator.draw();
    GlStateManager.translate(realX, realY, realZ);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.enableDepth();
    GlStateManager.depthMask(true);
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.popMatrix();
  }

  public static void drawFilledBoundingBox(AxisAlignedBB aabb, Color color, float opacity, float lineWidth) {
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.disableLighting();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.disableTexture2D();
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    float a = color.getAlpha() / 255.0F;
    float r = color.getRed() / 255.0F;
    float g = color.getGreen() / 255.0F;
    float b = color.getBlue() / 255.0F;

    GlStateManager.color(r, g, b, a * opacity);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    tessellator.draw();
    GlStateManager.color(r, g, b, a * opacity);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    tessellator.draw();
    GlStateManager.color(r, g, b, a * opacity);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
    worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
    tessellator.draw();
    GlStateManager.color(r, g, b, a);
    GL11.glLineWidth(lineWidth);
    RenderGlobal.drawSelectionBoundingBox(aabb);
    GL11.glLineWidth(1.0f);
    GlStateManager.enableTexture2D();
    GlStateManager.enableDepth();
    GlStateManager.disableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
  }

  // from skytils (kt -> java)
  public static void drawWaypointText(String string, double X, double Y, double Z, float partialTicks) {
    GlStateManager.alphaFunc(516, 0.1f);
    GlStateManager.pushMatrix();
    Entity viewer = mc.getRenderViewEntity();
    double x = X - mc.getRenderManager().viewerPosX;
    double y = Y - mc.getRenderManager().viewerPosY - viewer.getEyeHeight();
    double z = Z - mc.getRenderManager().viewerPosZ;
    double distSq = x * x + y * y + z * z;
    double dist = sqrt(distSq);
    if (distSq > 144) {
      x *= 12 / dist;
      y *= 12 / dist;
      z *= 12 / dist;
    }
    GlStateManager.translate(x, y, z);
    GlStateManager.translate(0f, viewer.getEyeHeight(), 0f);
    drawTag(string);
    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
    GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
    GlStateManager.translate(0f, -0.25f, 0f);
    GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
    GlStateManager.rotate(mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
    drawTag(EnumChatFormatting.RED.toString() + ((int) dist) + " blocks");
    GlStateManager.popMatrix();
    GlStateManager.disableLighting();
  }

  // from skytils (kt -> java)
  public static void drawTag(String string) {
    FontRenderer fontRenderer = mc.fontRendererObj;
    float FLOAT_1 = 0.02666667f;

    GlStateManager.pushMatrix();
    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
    GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
    GlStateManager.scale(-FLOAT_1, -FLOAT_1, FLOAT_1);
    GlStateManager.disableLighting();
    GlStateManager.depthMask(false);
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    int i = 0;
    int j = fontRenderer.getStringWidth(string) / 2;
    GlStateManager.disableTexture2D();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos(-j - 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
    worldrenderer.pos(-j - 1, 8 + 1, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
    worldrenderer.pos(j + 1, 8 + 1, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
    worldrenderer.pos(j + 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    fontRenderer.drawString(string, -j, i, 553648127);
    GlStateManager.depthMask(true);
    fontRenderer.drawString(string, -j, i, -1);
    GlStateManager.enableBlend();
    GlStateManager.enableDepth();
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.popMatrix();
  }

  public static Triple<Double, Double, Double> viewerPosition(float partialTicks) {
    final Entity viewer = mc.getRenderViewEntity();
    double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
    double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
    double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;
    return Triple.of(viewerX, viewerY, viewerZ);
  }

  public static void drawText(String str, double X, double Y, double Z) {
    drawText(str, X, Y, Z, false);
  }

  public static void drawText(String str, double X, double Y, double Z, boolean showDistance) {
    drawText(str, X, Y, Z, false, 1.0f);
  }

  public static void drawText(String str, double X, double Y, double Z, boolean showDistance, float lScale) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    double renderPosX = X - Minecraft.getMinecraft().getRenderManager().viewerPosX;
    double renderPosY = Y - Minecraft.getMinecraft().getRenderManager().viewerPosY;
    double renderPosZ = Z - Minecraft.getMinecraft().getRenderManager().viewerPosZ;

    double distance = Math.sqrt(renderPosX * renderPosX + renderPosY * renderPosY + renderPosZ * renderPosZ);
    double multiplier = Math.max(distance / 150f, 0.1f);
    lScale *= 0.45f * multiplier;

    float xMultiplier = Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1 : 1;

    GlStateManager.pushMatrix();
    GlStateManager.translate(renderPosX, renderPosY, renderPosZ);
    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
    GlStateManager.rotate(renderManager.playerViewX * xMultiplier, 1, 0, 0);
    GlStateManager.scale(-lScale, -lScale, lScale);
    GlStateManager.disableLighting();
    GlStateManager.depthMask(false);
    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

    int textWidth = fontRenderer.getStringWidth(StringUtils.stripControlCodes((str)));

    float j = textWidth / 2f;
    GlStateManager.disableTexture2D();
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.color(0, 0, 0, 0.5f);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
    worldrenderer.pos(-j - 1, -1, 0).endVertex();
    worldrenderer.pos(-j - 1, 8, 0).endVertex();
    worldrenderer.pos(j + 1, 8, 0).endVertex();
    worldrenderer.pos(j + 1, -1, 0).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();

    fontRenderer.drawString(str, -textWidth / 2, 0, -1);

    if (showDistance) {
      textWidth = fontRenderer.getStringWidth(StringUtils.stripControlCodes((int) distance + " blocks"));
      fontRenderer.drawString((int) distance + " blocks", -textWidth / 2, 10, -1);
    }

    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }
}
