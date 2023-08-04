package com.dillo.events.utilevents;

import com.dillo.utils.previous.SendChat;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CurJumpProgress extends Event {

  public double progress;
  public double max;
  public boolean isRest1;

  public CurJumpProgress(double curProgress, double max, boolean isRest) {
    if (!isRest) {
      progress = curProgress;
      this.max = max;
    } else {
      this.progress = 0;
      this.max = 0;
      this.isRest1 = true;
    }
  }
}
