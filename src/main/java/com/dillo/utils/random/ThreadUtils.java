package com.dillo.utils.random;

import com.dillo.utils.RandomisationUtils;

public class ThreadUtils {

  public static void threadSleepRandom(long time) {
    try {
      Thread.sleep(time + RandomisationUtils.randomNumberBetweenInt(time - 20, time + 20));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
