package com.dillo.main.utils.funny;

import com.dillo.calls.ArmadilloStates;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class VeinCombo {

    int ticks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!ArmadilloStates.isOnline()) return;
    }
}
