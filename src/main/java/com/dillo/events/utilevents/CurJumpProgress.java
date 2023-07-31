package com.dillo.events.utilevents;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CurJumpProgress extends Event {

  public double progress;
  public double max;
  public boolean isRest;

  public CurJumpProgress(double curProgress, double max, boolean isRest) {
    progress = curProgress;
    this.max = max;
    this.isRest = isRest;
    //SendChat.chat(curProgress + "!!!!");
  }
}
