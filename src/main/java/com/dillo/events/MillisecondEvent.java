package com.dillo.events;

import java.time.LocalDateTime;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MillisecondEvent extends Event {

  public LocalDateTime dateTime;
  public long timestamp;

  public MillisecondEvent() {
    timestamp = System.currentTimeMillis();
    dateTime = LocalDateTime.now();
  }
}
