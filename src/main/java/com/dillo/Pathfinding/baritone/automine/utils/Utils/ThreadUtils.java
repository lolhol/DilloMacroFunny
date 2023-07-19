package com.dillo.Pathfinding.baritone.automine.utils.Utils;

public class ThreadUtils {

  public static void sleep(int time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException ignored) {}
  }
}
