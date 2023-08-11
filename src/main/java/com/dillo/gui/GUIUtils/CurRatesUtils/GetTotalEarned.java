package com.dillo.gui.GUIUtils.CurRatesUtils;

import com.dillo.config.config;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import static com.dillo.gui.GUIUtils.CurRatesUtils.GetCurGemPrice.getPrice;
import static com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp.firstTime;
import static com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp.getJsonObject;

public class GetTotalEarned {

    public static TotalEarning totalEarned() {
        double total = 0;
        List<String> gems = Arrays.asList("RUBY", "AMBER", "TOPAZ", "SAPPHIRE", "AMETHYST", "JASPER", "JADE");
        JsonObject jsonObj = getJsonObject();

        for (String gem : gems) {
            double currTotal = 0;

            if (jsonObj.has("FLAWED_" + gem)) {
                double price = 0;

                if (config.npcPrice) {
                    price = 240;
                } else {
                    price = getPrice("FLAWED_" + gem + "_GEM");
                }

                if (price != -1) {
                    currTotal = jsonObj.get("FLAWED_" + gem).getAsInt() * price;
                } else {
                    currTotal = 0;
                }
            }

            total += currTotal;
        }

        TotalEarning totalEarnings = new TotalEarning();
        totalEarnings.totalEarned = total;

        long time = System.currentTimeMillis() - firstTime;
        int number = (int) Math.floor(total * ((double) 3600000 / time));

        totalEarnings.perHour = numberWithCommas(number);
        totalEarnings.totalEarningString = numberWithCommas((int) total);
        totalEarnings.trackTime = time;

        return totalEarnings;
    }

    public static long getTotalTime() {
        return System.currentTimeMillis() - firstTime;
    }

    private static String numberWithCommas(int x) {
        if (x == -1) {
            return "";
        }
        String[] parts = Integer.toString(x).split("\\.");
        parts[0] = parts[0].replaceAll("(?<=\\d)(?=(\\d{3})+(?!\\d))", ",");
        return String.join(".", parts);
    }

    public static void clearTotalEarned() {
        ItemsPickedUp.first = true;
        List<String> gems = Arrays.asList("RUBY", "AMBER", "TOPAZ", "SAPPHIRE", "AMETHYST", "JASPER", "JADE");
        JsonObject jsonObj = getJsonObject();
        firstTime = System.currentTimeMillis();

        for (String gem : gems) {
            if (jsonObj.has("FLAWED_" + gem)) {
                jsonObj.remove("FLAWED_" + gem);
            }
        }
    }

    public static class TotalEarning {

        public double totalEarned = 0;
        public String perHour = "";
        public String totalEarningString = "";
        public long trackTime = 0;
    }
}
