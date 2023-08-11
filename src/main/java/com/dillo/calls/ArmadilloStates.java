package com.dillo.calls;

public class ArmadilloStates {

    public static boolean spinDrive = false;
    public static boolean teleporting = false;
    public static CurrentState currentState = null;
    public static KillSwitch offlineState = KillSwitch.OFFLINE;

    public static boolean isOnline() {
        return offlineState != KillSwitch.OFFLINE;
    }
}
