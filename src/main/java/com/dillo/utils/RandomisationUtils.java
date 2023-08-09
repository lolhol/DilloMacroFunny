package com.dillo.utils;

public class RandomisationUtils {

    public static int randomNumberBetweenInt(float min, float max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static double randomNumberBetweenDouble(float min, float max) {
        return Math.random() * (max - min + 1) + min;
    }

    public static float percentOf(float one, float two) {
        return (one / two) * 100;
    }

    public static double getPercent(float number, double percent) {
        return (number / 100) * percent;
    }

    public static double getRandomisationPercent() {
        return randomNumberBetweenDouble(10, 30);
    }

    public static int getRandomAdd(float initNumber) {
        double randomisation = RandomisationUtils.getRandomisationPercent();
        int add = (int) Math.floor(RandomisationUtils.getPercent(initNumber, randomisation));
        return add;
    }
}
