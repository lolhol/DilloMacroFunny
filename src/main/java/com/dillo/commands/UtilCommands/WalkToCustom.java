package com.dillo.commands.UtilCommands;

import com.dillo.pathfinding.mit.finder.main.AStarPathFinder;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WalkToCustom extends Command {

  public static boolean startRender = false;
  StevebotApi api;

  public WalkToCustom(StevebotApi api) {
    super("walkToCustom");
    this.api = api;
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    new Thread(() -> {
      SendChat.chat("Starting");
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      BlockPos endBlock = new BlockPos(x, y, z);
      AStarPathFinder pathFinder = new AStarPathFinder();
      List<BlockPos> foundPath = pathFinder.AStarPathFinder(
        new PathFinderConfig(
          false,
          false,
          false,
          false,
          false,
          5,
          100,
          100,
          ids.mc.thePlayer.getPosition(),
          endBlock,
          new Block[] { Blocks.air },
          new Block[] { Blocks.stained_glass },
          200,
          0
        )
      );

      if (foundPath.isEmpty()) SendChat.chat("EMPTY PATH!"); else SendChat.chat("Done with path!");
    })
      .start();
  }
}
