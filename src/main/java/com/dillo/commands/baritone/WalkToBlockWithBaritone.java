package com.dillo.commands.baritone;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class WalkToBlockWithBaritone extends Command {

  public WalkToBlockWithBaritone() {
    super("walkBaritone");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    /*SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    autoMineBaritone.goTo(blockToWalk);*/

  }
}
