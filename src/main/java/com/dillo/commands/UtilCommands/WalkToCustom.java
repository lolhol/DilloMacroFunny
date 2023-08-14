package com.dillo.commands.UtilCommands;

import com.dillo.pathfinding.mit.finder.main.AStarPathFinder;
import com.dillo.pathfinding.mit.finder.main.OnPathRenderer;
import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.List;

public class WalkToCustom extends Command {

  public static boolean startRender = false;

  public WalkToCustom() {
    super("walkToCustom");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    AStarPathFinder pathFinder = new AStarPathFinder();
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    new Thread(() -> {
      OnPathRenderer.renderList(null, false);
      long start = System.currentTimeMillis();

      RenderOneBlockMod.renderOneBlock(ids.mc.thePlayer.getPositionVector().addVector(-0.5, 0, -0.5), true);

      List<BlockNodeClass> route = pathFinder.AStarPathFinder(
        new PathFinderConfig(
          false,
          false,
          false,
          false,
          false,
          10,
          1000,
          100,
          BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector().addVector(-0.5, 0, -0.5)),
          new BlockPos(x, y, z),
          new Block[] { Blocks.air },
          new Block[] { Blocks.air },
          100,
          0
        )
      );

      if (route == null) {
        SendChat.chat("Didnt find a route.");
        return;
      }

      OnPathRenderer.renderList(route, true);

      SendChat.chat("Took " + (System.currentTimeMillis() - start) + "ms. And the route size is " + route.size());
    })
      .start();
  }
}
