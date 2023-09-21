package com.dillo.commands.UtilCommands;

import static com.dillo.armadillomacro.walker;

import com.dillo.pathfinding.mit.finder.main.AStarPathFinder;
import com.dillo.pathfinding.mit.finder.main.OnPathRenderer;
import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.mit.finder.walker.Utils;
import com.dillo.pathfinding.mit.finder.walker.WalkerConfig;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderPoints;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class WalkToCustom extends Command {

  public static boolean startRender = false;

  public WalkToCustom() {
    super("walkToCustom");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    RenderPoints.renderPoint(null, 0.2, false);

    AStarPathFinder pathFinder = new AStarPathFinder();
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    new Thread(() -> {
      OnPathRenderer.renderList(null, false);
      long start = System.currentTimeMillis();

      //RenderOneBlockMod.renderOneBlock(ids.mc.thePlayer.getPositionVector().addVector(-0.5, 0, -0.5), true);

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
        new BlockPos(x, y, z),
        new Block[] { Blocks.air },
        new Block[] { Blocks.air },
        100,
        0
      );

      List<BlockNodeClass> route = pathFinder.run(newConfig);

      if (route == null) {
        SendChat.chat("Didnt find a route.");
        return;
      }

      SendChat.chat("Took " + (System.currentTimeMillis() - start) + "ms. And the route size is " + route.size());
      // Un comment @this to display the init route.
      //OnPathRenderer.renderList(route, true);
      // ==============================
      // Add @this to display a shorter path
      List<BlockPos> shortSegment = Utils.getShortList(route);
      shortSegment.forEach(a -> {
        RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(a), true);
      });
      // ==============================

      //List<BlockPos> shortSegment = Utils.getShortList(route);
      /*shortSegment.forEach(a -> {
        RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(a), true);
      });*/

      walker.run(shortSegment, new WalkerConfig(true, 4, shortSegment.get(shortSegment.size() - 1)));
    })
      .start();
  }
}
