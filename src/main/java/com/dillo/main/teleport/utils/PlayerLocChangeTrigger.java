package com.dillo.main.teleport.utils;

import com.dillo.events.MillisecondEvent;
import com.dillo.events.PlayerLocChangeEvent;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerLocChangeTrigger {

  Vec3 prevPos;
  boolean isPrev = true;
  int ticks = 0;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (ticks >= 5) {
        if (!prevPos.equals(ids.mc.thePlayer.getPositionVector())) {
          MinecraftForge.EVENT_BUS.post(new PlayerLocChangeEvent());
        }
        prevPos = ids.mc.thePlayer.getPositionVector();
        ticks = 0;
      } else {
        ticks++;
      }
    }
  }
}
