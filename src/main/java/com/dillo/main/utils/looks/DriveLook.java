package com.dillo.main.utils.looks;

import com.dillo.events.MillisecondEvent;
import com.dillo.events.macro.OnStartJumpEvent;
import com.dillo.events.utilevents.OnChangeYawEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.dillo.utils.previous.random.ids.mc;

public class DriveLook {

    public static long startJTime;
    public static boolean projectJump = false;
    public static boolean doneLook180 = false;
    private static long startTime;
    private static long totalTime1;
    private static long endTime;
    private static long endTimeP;
    private static boolean isDoneP = true;
    private static double addAm = 0;
    private static double addPi = 0;
    private static boolean isDoneRotate = true;
    private static double add = 0;
    private static boolean registered;
    private static double max;
    private static float addYawLook;

    public static void addYaw(long totalTime, float addYaw) {
        addAm = addYaw / totalTime;
        addYawLook = addYaw;
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + totalTime;
        totalTime1 = totalTime;
        add = 0;
        max = 0;

        isDoneRotate = false;
        registered = false;
    }

    public static void addPitch(long totalTime, float addPitch) {
        addPi = addPitch / totalTime;
        endTimeP = System.currentTimeMillis() + totalTime;

        isDoneP = false;
    }

    public static void reset() {
        isDoneRotate = true;
    }

    public static void pause() {
        isDoneRotate = true;
    }

    public static void unpause() {
        isDoneRotate = false;
    }

    public static void resetJump() {
        max = 0;
    }

    @SubscribeEvent
    public void onMillisecond(MillisecondEvent event) {
        if (!isDoneRotate) {
            if (System.currentTimeMillis() <= endTime) {
                mc.thePlayer.rotationYaw += (float) addAm;
                add += addAm;
                MinecraftForge.EVENT_BUS.post(new OnChangeYawEvent(mc.thePlayer.rotationYaw));
            } else {
                doneLook180 = true;
                projectJump = false;
                isDoneRotate = true;
                resetJump();
            }

            if (add > 60 && !registered) {
                MinecraftForge.EVENT_BUS.post(new OnStartJumpEvent(endTime - System.currentTimeMillis()));
                registered = true;
            }
        }

        if (!isDoneP) {
            if (System.currentTimeMillis() <= endTimeP) {
                mc.thePlayer.rotationPitch += addPi;
            } else {
                isDoneP = true;
            }
        }
    }
}
