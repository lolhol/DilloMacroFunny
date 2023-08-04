package com.dillo.main.utils.jump;

import com.dillo.config.config;
import com.dillo.events.utilevents.CurJumpProgress;
import com.dillo.utils.previous.SendChat;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GetProjectedTime {

  public static List<Long> allJumpTimes = new ArrayList<>();
  public static boolean startState;
  public static double max;
  public static long startTime;

  public static double getProjectedTime() {
    double avg = getAvg(allJumpTimes);

    SendChat.chat(String.valueOf(avg) + "111");

    if (avg == -1) {
      return (double) config.headMovement / 2;
    }

    return avg;
  }

  public static void startJump(boolean state) {
    if (!state) {
      clear();
      return;
    }

    startState = true;
    startTime = System.currentTimeMillis();
  }

  // More code after this... :/

  public static void clear() {
    max = 0;
    startState = false;
  }

  public static void add(long time) {
    if (allJumpTimes.size() == 0) {
      allJumpTimes.add(time);
    }

    if (time - 100 < getAvg(allJumpTimes) && time < config.headMovement) {
      allJumpTimes.add(time);
    }

    SendChat.chat(String.valueOf(time) + "!!!");
  }

  public static double getAvg(List<Long> list) {
    double total = 0;

    for (double time : list) {
      total += time;
      //SendChat.chat(time + " | " + list.size());
    }

    if (total == 0) {
      return -1;
    }

    return total / list.size();
  }

  @SubscribeEvent
  public void onChangeJump(CurJumpProgress event) {
    if (!startState) return;
    max += event.progress;

    if (1.5 - max < 0.7) {
      clear();
      SendChat.chat(String.valueOf(System.currentTimeMillis() - startTime));
      add(System.currentTimeMillis() - startTime);
    } else {
      //SendChat.chat(String.valueOf(1.5 - max) + "???");
    }
  }
}
