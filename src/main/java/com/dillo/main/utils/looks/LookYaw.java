package com.dillo.main.utils.looks;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;

import static com.dillo.main.teleport.utils.IsOnBlock.yaw;
import static com.dillo.main.utils.looks.YawLook.smoothLook2;

public class LookYaw {

    public static void lookToYaw(long time, float addYaw, boolean excludePitch) {
        LookAt.excludePitch = excludePitch;
        LookAt.smoothLook(new LookAt.Rotation(yaw, curRotation() + addYaw), time);
    }

    public static float curRotation() {
        float rotationYaw = ids.mc.thePlayer.rotationYaw;

        rotationYaw %= 360;
        if (rotationYaw < 0) {
            rotationYaw += 360;
        }

        return rotationYaw;
    }

    public static double curRotationDispl() {
        float rotationYaw = ids.mc.thePlayer.rotationYaw;
        SendChat.chat(String.valueOf(rotationYaw / 360));
        double returnVal = Math.abs(rotationYaw / 360);

        if (returnVal < 1) {
            returnVal = 1;
        }

        return returnVal;
    }

    public static void lookToPitch(long time, float addPitch) {
        float rotation = curRotation();

        LookAt.smoothLook(new LookAt.Rotation(ids.mc.thePlayer.rotationPitch + addPitch, rotation), time);
    }

    public static void lookToYawPitch(long time, float addPitch, float addYaw) {
        float rotationYaw = curRotation() + addYaw;

        LookAt.smoothLook(new LookAt.Rotation(ids.mc.thePlayer.rotationPitch + addPitch, rotationYaw), time);
    }

    public static void addPitch(long time, float addPitch) {
        float rotation = curRotation();
        LookAt.smoothLook(new LookAt.Rotation(0, rotation), time);
    }

    public static void addPitchTwo(long time, float addPitch) {
        smoothLook2(new YawLook.RotationYaw(ids.mc.thePlayer.rotationPitch + addPitch, 0), time);
    }
}
