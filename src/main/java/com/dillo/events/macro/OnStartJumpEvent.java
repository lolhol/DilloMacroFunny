package com.dillo.events.macro;

import net.minecraftforge.fml.common.eventhandler.Event;

public class OnStartJumpEvent extends Event {

  public long time;

  public OnStartJumpEvent(long time) {
    this.time = time;
  }
}
