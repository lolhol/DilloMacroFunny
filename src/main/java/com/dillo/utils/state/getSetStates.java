package com.dillo.utils.state;

import java.util.Objects;

public class getSetStates {

    public static String state = null;
    public static int tickSinceStateChange = 0;

    public static void setState(String newState) {
        if (
                Objects.equals(newState, "tickCount") ||
                        Objects.equals(newState, "armadilloTicks") ||
                        Objects.equals(newState, "armadilloClickTicks")
        ) {
            state = newState;
        } else {
            state = newState;
            tickSinceStateChange = 0;
        }
    }
}
