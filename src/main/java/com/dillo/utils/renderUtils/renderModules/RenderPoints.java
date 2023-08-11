package com.dillo.utils.renderUtils.renderModules;

import com.dillo.utils.renderUtils.RenderLine;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RenderPoints {

    //public static final List<List<List<List<Vec3>>>> listOfLists = new ArrayList<>();
    public static List<Vec3> points = new ArrayList<>();
    private static boolean renderState = false;
    private static double scale = 1.0;

    public static void renderPoint(Vec3 pointVec, double size, boolean state) {
        if (state) {
            points.add(new Vec3(pointVec.xCoord, pointVec.yCoord, pointVec.zCoord));
            scale = size;
            renderState = true;
        } else {
            renderState = false;
            points = new ArrayList<>();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (renderState) {
            for (Vec3 point : points) {
                RenderLine.drawLine(
                        new Vec3(point.xCoord - scale, point.yCoord, point.zCoord),
                        new Vec3(point.xCoord + scale, point.yCoord, point.zCoord),
                        2,
                        Color.RED,
                        event.partialTicks
                );
                RenderLine.drawLine(
                        new Vec3(point.xCoord, point.yCoord - scale, point.zCoord),
                        new Vec3(point.xCoord, point.yCoord + scale, point.zCoord),
                        2,
                        Color.RED,
                        event.partialTicks
                );
                RenderLine.drawLine(
                        new Vec3(point.xCoord, point.yCoord, point.zCoord - scale),
                        new Vec3(point.xCoord, point.yCoord, point.zCoord + scale),
                        2,
                        Color.RED,
                        event.partialTicks
                );
            }
        }
    }
}
