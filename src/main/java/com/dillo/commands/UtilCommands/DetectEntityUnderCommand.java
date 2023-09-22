package com.dillo.commands.UtilCommands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.pathfinding.mit.finder.mods.breaklogs.LogBreaker;
import com.dillo.pathfinding.mit.finder.mods.breaklogs.LogBreakerV2;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

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
    PathFinderConfig newConfig = new PathFinderConfig(
      false,
      false,
      false,
      false,
      false,
      10,
      100000,
      1000,
      BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector().addVector(-0.5, 0, -0.5)),
      new BlockPos(1, 1, 1),
      new Block[] { Blocks.air },
      new Block[] { Blocks.air },
      100,
      0
    );
    v2.run(currentRoute.currentRoute, st, newConfig);
    //b.logBreakerMain(!st, currentRoute.currentRoute);
    st = !st;
  }
}
