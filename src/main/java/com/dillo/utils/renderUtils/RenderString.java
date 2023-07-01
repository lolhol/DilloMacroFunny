package com.dillo.utils.renderUtils;

import com.dillo.mixin.RenderManagerAccessor;
import com.dillo.utils.previous.random.ids;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderString {

  // Not my code, this is from GumTuneClient.

  public static void renderStr(String str, double X, double Y, double Z, float partialTicks, boolean showDist) {
    GlStateManager.alphaFunc(516, 0.1F);

    GlStateManager.pushMatrix();

    Entity viewer = ids.mc.getRenderViewEntity();
    RenderManagerAccessor rm = (RenderManagerAccessor) ids.mc.getRenderManager();

    double x = X - rm.getRenderPosX();
    double y = Y - rm.getRenderPosY();
    double z = Z - rm.getRenderPosZ();

    double distSq = x * x + y * y + z * z;
    double dist = Math.sqrt(distSq);

    if (distSq > 144) {
      x *= 12 / dist;
      y *= 12 / dist;
      z *= 12 / dist;
    }

    GlStateManager.translate(x, y, z);
    GlStateManager.translate(0, viewer.getEyeHeight(), 0);

    drawNametag(str);

    GlStateManager.rotate(-ids.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(ids.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0, -0.25f, 0);
    GlStateManager.rotate(-ids.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.rotate(ids.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

    if (showDist) {
      drawNametag("§e" + Math.round(dist * 10) / 10 + " blocks");
    }

    GlStateManager.popMatrix();

    GlStateManager.disableLighting();
  }

  public static void drawNametag(String str) {
    FontRenderer fontrenderer = ids.mc.fontRendererObj;
    float f1 = 0.0266666688f;
    GlStateManager.pushMatrix();
    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-ids.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(ids.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(-f1, -f1, f1);
    GlStateManager.disableLighting();
    GlStateManager.depthMask(false);
    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
    int i = 0;

    int j = fontrenderer.getStringWidth(str) / 2;
    GlStateManager.disableTexture2D();
    bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
    bufferBuilder.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
    bufferBuilder.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
    bufferBuilder.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
    bufferBuilder.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
    GlStateManager.depthMask(true);

    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

    GlStateManager.enableDepth();
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
  }
}
