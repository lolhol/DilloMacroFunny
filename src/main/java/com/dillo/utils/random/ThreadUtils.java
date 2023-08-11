package com.dillo.utils.random;

import com.dillo.utils.RandomisationUtils;

public class ThreadUtils {

  public static void threadSleepRandom(long time) {
    try {
      long add = RandomisationUtils.randomNumberBetweenInt(-20, 20);

      Thread.sleep(time + (add < 0 && Math.abs(add) > time ? 0 : add));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
