package com.dillo.MITGUI.GUIUtils.CurRatesUtils;

import static com.dillo.data.config.hypixelBZFetches;

import com.dillo.data.config;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GetCurGemPrice {

  public static boolean canCheck = false;
  public static List<List<Double>> gemPrices = new ArrayList<>();
  public static long currTime = System.currentTimeMillis();

  public static List<List<Double>> getCurrGemPrice() {
    List<List<Double>> prices = new ArrayList<List<Double>>();
    List<String> gems = Arrays.asList(
      "ROUGH_RUBY_GEM",
      "FLAWED_RUBY_GEM",
      "ROUGH_AMBER_GEM",
      "FLAWED_AMBER_GEM",
      "ROUGH_TOPAZ_GEM",
      "FLAWED_TOPAZ_GEM",
      "ROUGH_SAPPHIRE_GEM",
      "FLAWED_SAPPHIRE_GEM",
      "ROUGH_AMETHYST_GEM",
      "FLAWED_AMETHYST_GEM",
      "ROUGH_JASPER_GEM",
      "FLAWED_JASPER_GEM",
      "ROUGH_JADE_GEM",
      "FLAWED_JADE_GEM"
    );
    String result;

    try {
      URL url = new URL("https://api.hypixel.net/skyblock/bazaar");
      Scanner sc = new Scanner(url.openStream());
      StringBuffer sb = new StringBuffer();

      while (sc.hasNext()) {
        sb.append(sc.next());
      }

      result = sb.toString();

      JsonParser parser = new JsonParser();
      JsonObject json = parser.parse(result).getAsJsonObject();
      JsonObject products = json.getAsJsonObject("products");

      for (int i = 0; i < gems.size(); i += 2) {
        List<Double> newList = new ArrayList<Double>();
        double rough = prices(gems.get(i), products);
        double flawed = prices(gems.get(i + 1), products);
        newList.add(rough);
        newList.add(flawed);

        prices.add(newList);
      }
    } catch (IOException e) {}

    gemPrices = prices;

    return prices;
  }

  public static double prices(String string, JsonObject json) {
    JsonObject ruby = json.getAsJsonObject(string);
    JsonArray rubyStuff = ruby.getAsJsonArray("sell_summary");
    JsonObject rubyPrice = rubyStuff.get(0).getAsJsonObject();

    return rubyPrice.get("pricePerUnit").getAsDouble();
  }

  public static double getPrice(String string) {
    if (hypixelBZFetches) {
      if (System.currentTimeMillis() >= currTime + config.hypixelBzFetchTime * 1000L) {
        currTime = System.currentTimeMillis();
        new Thread(GetCurGemPrice::getCurrGemPrice).start();
      }
    }

    List<String> gems = Arrays.asList(
      "ROUGH_RUBY_GEM",
      "FLAWED_RUBY_GEM",
      "ROUGH_AMBER_GEM",
      "FLAWED_AMBER_GEM",
      "ROUGH_TOPAZ_GEM",
      "FLAWED_TOPAZ_GEM",
      "ROUGH_SAPPHIRE_GEM",
      "FLAWED_SAPPHIRE_GEM",
      "ROUGH_AMETHYST_GEM",
      "FLAWED_AMETHYST_GEM",
      "ROUGH_JASPER_GEM",
      "FLAWED_JASPER_GEM",
      "ROUGH_JADE_GEM",
      "FLAWED_JADE_GEM"
    );
    int currGem = 0;

    for (String gem : gems) {
      if (gem.toLowerCase().equals(string.toLowerCase())) {
        break;
      }

      currGem++;
    }

    if (gemPrices.size() < 1) {
      return 240;
    } else {
      switch (currGem) {
        case 0:
          return gemPrices.get(0).get(0);
        case 1:
          return gemPrices.get(0).get(1);
        case 2:
          return gemPrices.get(1).get(0);
        case 3:
          return gemPrices.get(1).get(1);
        case 4:
          return gemPrices.get(2).get(0);
        case 5:
          return gemPrices.get(2).get(1);
        case 6:
          return gemPrices.get(3).get(0);
        case 7:
          return gemPrices.get(3).get(1);
        case 8:
          return gemPrices.get(4).get(0);
        case 9:
          return gemPrices.get(4).get(1);
        case 10:
          return gemPrices.get(5).get(0);
        case 11:
          return gemPrices.get(5).get(1);
        case 12:
          return gemPrices.get(6).get(0);
        case 13:
          return gemPrices.get(6).get(1);
        case 14:
          return gemPrices.get(7).get(0);
        case 15:
          return gemPrices.get(7).get(1);
      }
    }

    return -1;
  }
}
