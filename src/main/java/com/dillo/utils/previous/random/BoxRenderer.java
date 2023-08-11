package com.dillo.utils.previous.random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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
}
