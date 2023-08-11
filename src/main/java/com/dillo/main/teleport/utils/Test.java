package com.dillo.main.teleport.utils;

import com.dillo.events.PlayerLocChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Test {

    @SubscribeEvent
    public void onChange(PlayerLocChangeEvent event) {
        //SendChat.chat("CHNAGE!");
    }
}
