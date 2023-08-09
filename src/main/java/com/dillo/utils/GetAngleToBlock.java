package com.dillo.utils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class GetAngleToBlock {

    public static float calcAngle(BlockPos pos) {
        float yaw = Math.abs(ids.mc.thePlayer.rotationYaw);
        float angle = Math.abs(yaw - getBlockYaw(pos.getX(), pos.getZ(), ids.mc.thePlayer));

        if (isBlockToLeft(pos.getX(), pos.getZ(), ids.mc.thePlayer)) {
            angle = 360.0f - angle;
        }

        angle %= 360.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }

        return angle;
    }

    private static float getBlockYaw(int blockX, int blockZ, EntityPlayer player) {
        double dx = blockX - player.posX;
        double dz = blockZ - player.posZ;
        return (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
    }

    private static boolean isBlockToLeft(int blockX, int blockZ, EntityPlayer player) {
        double dx = blockX - player.posX;
        double dz = blockZ - player.posZ;
        double angle = Math.atan2(dz, dx);
        return (Math.toDegrees(angle) - player.rotationYaw) > 0;
    }

    public static float calcAngleFromYaw(Vec3 pos, float curYaw) {
        float yaw = Math.abs(curYaw);
        float angle = Math.abs(yaw - getBlockYawDouble(pos.xCoord, pos.yCoord, ids.mc.thePlayer));

        if (isBlockToLeftDouble(pos.xCoord, pos.yCoord, ids.mc.thePlayer)) {
            angle = 360.0f - angle;
        }

        angle %= 360.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }

        return angle;
    }

    private static float getBlockYawDouble(double blockX, double blockZ, EntityPlayer player) {
        double dx = blockX - player.posX;
        double dz = blockZ - player.posZ;
        return (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
    }

    private static boolean isBlockToLeftDouble(double blockX, double blockZ, EntityPlayer player) {
        double dx = blockX - player.posX;
        double dz = blockZ - player.posZ;
        double angle = Math.atan2(dz, dx);
        return (Math.toDegrees(angle) - player.rotationYaw) > 0;
    }
}
