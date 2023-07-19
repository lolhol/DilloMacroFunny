package com.dillo.Events;

import java.time.LocalDateTime;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SecondEvent extends Event {

  public LocalDateTime dateTime;
  public long timestamp;

  public SecondEvent() {
    dateTime = LocalDateTime.now();
    timestamp = System.currentTimeMillis();
  }
}
