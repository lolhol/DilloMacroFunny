package com.dillo.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class DonePathEvent extends Event {
    public boolean done = false;

    public DonePathEvent() {
        done = true;
    }
}
