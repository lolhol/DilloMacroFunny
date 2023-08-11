package com.dillo.main.teleport.utils;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.main.utils.keybinds.AllKeybinds.FORWARD;

public class WalkForward {

    private static boolean start = false;
    private static int curTicks = 0;
    private static int totalTicks = 0;
    private static CurrentState stateAfter = null;

    public static void walkForward(int totalTime, CurrentState state) {
        totalTicks = totalTime;
        start = true;
        stateAfter = state;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (ArmadilloStates.isOnline() && start) {
            if (totalTicks > curTicks) {
                KeyBinding.setKeyBindState(FORWARD.getKeyCode(), true);
                curTicks++;
            } else {
                curTicks = 0;
                totalTicks = 0;
                start = false;
                ArmadilloStates.currentState = stateAfter;
                KeyBinding.setKeyBindState(FORWARD.getKeyCode(), false);
            }
        }
    }
}
