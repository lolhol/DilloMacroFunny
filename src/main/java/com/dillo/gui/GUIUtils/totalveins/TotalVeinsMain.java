package com.dillo.gui.GUIUtils.totalveins;

public class TotalVeinsMain {

    public static int totalVeinsCur = 0;
    public static long startTime = System.currentTimeMillis();

    public int totalGemsMined() {
        if (System.currentTimeMillis() - startTime >= 30000) {
            totalVeinsCur = 0;
        }

        return totalVeinsCur == 0 ? -1 : totalVeinsCur;
    }
}
