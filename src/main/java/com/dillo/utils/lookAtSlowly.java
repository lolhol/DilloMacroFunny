package com.dillo.utils;

import net.minecraft.client.Minecraft;

public class lookAtSlowly {

    private static final double lookSpeed = 0.5;
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void lookAtSlowly(double x, int y, double z) {
        double hoekPitch;
        double hoekYaw;

        double playerAngleYaw = mc.thePlayer.rotationYaw;
        double angleYaw = 0;
        playerAngleYaw = playerAngleYaw % 360;
        double dx = mc.thePlayer.posX - x + 0.0001;
        double dz = mc.thePlayer.posY - z + 0.0001;
        double dy = mc.thePlayer.posZ - y + 0.0001;
        double dist = Math.sqrt(dx * dx + dz * dz);

        if (dx < 0.0 && dz < 0.0) {
            angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 180;
        } else if (dz < 0.0 && dx > 0.0) {
            angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 360;
        } else if (dz > 0.0 && dx < 0.0) {
            angleYaw = radToDegree.radToDegree(Math.atan(dz / dx)) + 180;
        } else if (dz > 0.0 && dx > 0.0) {
            angleYaw = radToDegree.radToDegree(Math.atan(dz / dx));
        }

        hoekYaw = angleYaw - playerAngleYaw + 90;

        if (hoekYaw > 180) {
            hoekYaw -= 360;
        }

        if (hoekYaw < -180) {
            hoekYaw += 360;
        }
    }
}
