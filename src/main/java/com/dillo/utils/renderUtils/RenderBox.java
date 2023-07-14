package com.dillo.utils.renderUtils;

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

public class RenderBox {

  public static void drawBox(
    double x,
    double y,
    double z,
    Color color,
    float width,
    float partialTicks,
    boolean isFill
  ) {
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

    AxisAlignedBB bb = new AxisAlignedBB(x1, y1, z1, x1 + 1, y1 + 1, z1 + 1);

    RenderGlobal.drawOutlinedBoundingBox(bb, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

    if (isFill) {
      drawFilledBoundingBox(bb, color.getRGB(), 1);
    }

    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.enableCull();
  }

  public static void drawFilledInBlock(BlockPos block, Color color, float opas, float partialTicks) {
    Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
    double x1 = block.getX() - (viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks);
    double y1 = block.getY() - (viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks);
    double z1 = block.getZ() - (viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks);

    AxisAlignedBB bb = new AxisAlignedBB(x1, y1, z1, x1 + 1, y1 + 1, z1 + 1);

    drawFilledBoundingBox(bb, color.getRGB(), opas);
  }

  public static void drawFilledBoundingBox(AxisAlignedBB aabb, int color, float opacity) {
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.disableLighting();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.disableTexture2D();
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    float a = (color >> 24 & 0xFF) / 255.0F;
    float r = (color >> 16 & 0xFF) / 255.0F;
    float g = (color >> 8 & 0xFF) / 255.0F;
    float b = (color & 0xFF) / 255.0F;

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
    RenderGlobal.drawSelectionBoundingBox(aabb);
    GlStateManager.enableTexture2D();
    GlStateManager.enableDepth();
    GlStateManager.disableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
