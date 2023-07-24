package com.dillo.main.sacks.sackclicker;

import static com.dillo.main.macro.refuel.ReFuelDrill.getInventoryName;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SackClickerMain {

  boolean state = false;
  int ticks = 0;
  int clickSlot = 0;
  List<String> names = new ArrayList<>();

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (!state) return;

      if (getInventoryName(ids.mc.currentScreen).toLowerCase().contains("sack")) {
        if (ticks >= 2) {} else {
          ticks++;
        }
      }
    }
  }

  public void sackClicker(boolean state, GemType type) {
    this.state = state;
  }
}
