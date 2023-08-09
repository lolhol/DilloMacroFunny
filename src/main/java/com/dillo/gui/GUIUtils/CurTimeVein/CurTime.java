package com.dillo.gui.GUIUtils.CurTimeVein;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.dillo.main.teleport.macro.TeleportToNextBlock.isTeleporting;

public class CurTime {

    public static long start = System.currentTimeMillis();
    public static long now = System.currentTimeMillis();
    public static long returnTime = 0;
    public static boolean first = false;
    public static List<Long> timeList = new ArrayList<>();

    public static long curTime() {
        if (System.currentTimeMillis() - start < 15000) {
            long currTotalTime = 0;

            for (long time : timeList) {
                currTotalTime += time;
            }

            return timeList.size() != 0 ? currTotalTime / timeList.size() : -1;
        } else {
            timeList.clear();
            return -1;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ArmadilloStates.isOnline()) {
                if (timeList.size() >= 10) {
                    timeList.remove(0);
                }

                if (ArmadilloStates.currentState == CurrentState.ARMADILLO) {
                    start = System.currentTimeMillis();
                }

                if (isTeleporting) {
                    returnTime = System.currentTimeMillis() - start;
                    timeList.add(returnTime);
                    first = true;
                    isTeleporting = false;
                }
            }
        }
    }
}
