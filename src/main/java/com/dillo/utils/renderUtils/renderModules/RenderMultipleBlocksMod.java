package com.dillo.utils.renderUtils.renderModules;

import com.dillo.utils.renderUtils.RenderBox;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RenderMultipleBlocksMod {

    private static List<Vec3> blocks1 = new ArrayList<>();
    private static boolean startRender = false;

    public static void renderMultipleBlocks(Vec3 block, boolean renderState) {
        startRender = renderState;
        if (startRender) {
            blocks1.add(block);
        } else {
            startRender = false;
            blocks1 = new ArrayList<>();
        }
    }

    public static void stopRenderBlock(Vec3 block) {
        if (blocks1 != null) {
            blocks1.remove(block);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (startRender && blocks1 != null) {
            for (int i = 0; i < blocks1.size(); i++) {
                try {
                    RenderBox.drawBox(
                            blocks1.get(i).xCoord,
                            blocks1.get(i).yCoord,
                            blocks1.get(i).zCoord,
                            Color.GREEN,
                            0.5F,
                            event.partialTicks,
                            false
                    );
                } catch (Exception e) {
                }
            }
        }
    }
}
