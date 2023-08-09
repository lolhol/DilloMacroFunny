package com.dillo.main.esp.other;

import com.dillo.utils.previous.random.ids;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import static com.dillo.config.config.armadildo;

public class BigDildoDillo {

    /**
     * The following code was taken from "aurora client"
     */

    /// Modify path (aka add pickture)
    private static final ResourceLocation ARMADILDO = new ResourceLocation("funny-textures:dillo/bigDilloFunny.png");

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!armadildo) return;

        ids.mc.theWorld
                .getLoadedEntityList()
                .stream()
                .filter(EntityArmorStand.class::isInstance)
                .forEach(entity -> {
                    if (entity.getName().toLowerCase().contains("armadillo")) {
                        double x =
                                entity.lastTickPosX +
                                        (entity.posX - entity.lastTickPosX) *
                                                event.partialTicks -
                                        ids.mc.getRenderManager().viewerPosX;
                        double y =
                                entity.lastTickPosY +
                                        (entity.posY - entity.lastTickPosY) *
                                                event.partialTicks -
                                        ids.mc.getRenderManager().viewerPosY;
                        double z =
                                entity.lastTickPosZ +
                                        (entity.posZ - entity.lastTickPosZ) *
                                                event.partialTicks -
                                        ids.mc.getRenderManager().viewerPosZ;
                        GL11.glPushMatrix();
                        GL11.glTranslated(x, y - 0.2, z);
                        GL11.glScalef(0.03f, 0.03f, 0.03f);
                        GL11.glRotated(-ids.mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
                        GlStateManager.disableDepth();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

                        // Add big ***ldo here
                        ids.mc.getTextureManager().bindTexture(ARMADILDO);

                        Gui.drawModalRectWithCustomSizedTexture(50, 90, 0.0f, 0.0f, -100, -100, -100.0f, -100.0f);
                        GlStateManager.enableDepth();
                        GL11.glPopMatrix();
                    }
                });
    }
}
