package com.dillo.utils.renderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderLine {

    public static void drawLine(Vec3 vec1, Vec3 vec2, float width, Color color, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();

        Vec3 playerPos = mc.thePlayer.getPositionVector();

        GL11.glPushMatrix();
        GL11.glTranslated(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);

        GL11.glLineWidth(width); // Set line width (2.0F in this example)
        GL11.glDisable(GL11.GL_TEXTURE_2D); // Disable texture rendering
        GL11.glEnable(GL11.GL_BLEND); // Enable alpha blending
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 1); // Set line color (red in this example)

        // Render line
        GL11.glVertex3d(vec1.xCoord, vec1.yCoord, vec1.zCoord);
        GL11.glVertex3d(vec2.xCoord, vec2.yCoord, vec2.zCoord);

        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }
}
