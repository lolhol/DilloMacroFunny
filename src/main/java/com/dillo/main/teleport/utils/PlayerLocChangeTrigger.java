package com.dillo.main.teleport.utils;

import com.dillo.events.PlayerLocChangeEvent;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerLocChangeTrigger {

    BlockPos prevPos;
    boolean isPrev = true;
    int ticks = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ids.mc.theWorld == null) return;

            if (prevPos == null) prevPos = ids.mc.thePlayer.getPosition();
            if (DistanceFromTo.distanceFromTo(prevPos, ids.mc.thePlayer.getPosition()) >= 0.01) {
                MinecraftForge.EVENT_BUS.post(new PlayerLocChangeEvent(ids.mc.thePlayer.getPosition()));
            }

            prevPos = ids.mc.thePlayer.getPosition();
            ticks = 0;
        }
    }
}
