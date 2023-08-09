package com.dillo.events.utilevents;

import net.minecraftforge.fml.common.eventhandler.Event;

public class OnChangeYawEvent extends Event {

    public float yaw;
    public float yaw360;

    public OnChangeYawEvent(float curYaw) {
        yaw = curYaw;
        yaw360 = curYaw % 360;
    }
}
