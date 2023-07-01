package com.dillo.ArmadilloMain;

public class TPSCalculator {
    private static long lastTime;
    private static int tickCount;

    public static void calculateTPS() {
        long currentTime = System.currentTimeMillis();
        tickCount++;

        if (currentTime - lastTime >= 1000) {
            double tps = tickCount / ((currentTime - lastTime) / 1000.0);
            System.out.println("Ticks per Second: " + tps);

            tickCount = 0;
            lastTime = currentTime;
        }
    }
}
