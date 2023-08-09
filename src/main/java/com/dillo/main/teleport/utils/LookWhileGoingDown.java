package com.dillo.main.teleport.utils;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.config.config;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.utils.looks.LookAt;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.calls.CurrentState.ARMADILLO;
import static com.dillo.main.utils.looks.LookAt.updateServerLook;

public class LookWhileGoingDown {

    private static CurrentState state = null;
    private static boolean isStartServerLook = false;
    private static BlockPos blockPos = null;
    private static long maxTimeTake = 0;
    private static boolean onOff = false;

    public static void lookUntilState(CurrentState endState, BlockPos blockPosition, long time) {
        state = endState;
        blockPos = blockPosition;
        maxTimeTake = time;
        onOff = true;
    }

    public static void stopLook() {
        onOff = false;
        state = null;
        blockPos = null;
        maxTimeTake = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (onOff && ArmadilloStates.isOnline()) {
                if (ArmadilloStates.currentState != ARMADILLO) {
                    if (!config.serverRotations) {
                        LookAt.smoothLook(LookAt.getRotation(blockPos), maxTimeTake);
                    }
                } else {
                    isStartServerLook = false;
                    maxTimeTake = 0;
                    blockPos = null;
                    state = null;
                    onOff = false;
                }
            } else {
                onOff = false;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if (!isStartServerLook) return;
        updateServerLook();
    }
}
