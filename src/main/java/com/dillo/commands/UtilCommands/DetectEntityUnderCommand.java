package com.dillo.commands.UtilCommands;

import com.dillo.pathfinding.mit.finder.mods.breaklogs.LogBreaker;
import com.dillo.pathfinding.mit.finder.mods.breaklogs.LogBreakerV2;
import com.dillo.utils.previous.chatUtils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class DetectEntityUnderCommand extends Command {

  public LogBreaker b = null;
  boolean st = false;

  LogBreakerV2 v2 = new LogBreakerV2();

  public DetectEntityUnderCommand(LogBreaker breaker) {
    super("testE");
    this.b = breaker;
  }

  @DefaultHandler
  public void handle() {
    SendChat.chat("Starting!");
    v2.run(null, st);
    //b.logBreakerMain(!st, currentRoute.currentRoute);
    st = !st;
  }
}
