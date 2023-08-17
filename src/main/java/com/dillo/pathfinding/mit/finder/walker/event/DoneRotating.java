package com.dillo.pathfinding.mit.finder.walker.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class DoneRotating extends Event {

  public boolean isDoneRotate = false;

  public DoneRotating(boolean state) {
    this.isDoneRotate = state;
  }
}
