package com.dillo.commands.baritone;

import com.dillo.Pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.Pathfinding.baritone.automine.config.WalkBaritoneConfig;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.util.BlockPos;

public class WalkToBlockWithBaritone extends Command {

  public WalkToBlockWithBaritone() {
    super("walkBaritone");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    AutoMineBaritone autoMineBaritone = new AutoMineBaritone(new WalkBaritoneConfig(0, 256, 5));
    autoMineBaritone.goTo(blockToWalk);
  }
}
