package com.dillo.utils.renderUtils.renderModules;

import com.dillo.utils.renderUtils.RenderLine;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderMultipleLines {

  private static boolean render = false;
  private static List<List<BlockPos>> blocks = new ArrayList<>();

  public static void renderMultipleLines(BlockPos block1, BlockPos block2, boolean startStop) {
    render = startStop;

    if (render) {
      List<BlockPos> blockList = new ArrayList<>();
      blockList.add(block1);
      blockList.add(block2);

      blocks.add(blockList);
    } else {
      render = false;
      blocks = new ArrayList<>();
    }
  }

  public static void stopRenderSpecificLine(BlockPos block1) {
    blocks.remove(block1);
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (render && blocks.size() > 0) {
      for (List<BlockPos> block : blocks) {
        BlockPos blockOne = block.get(0);
        BlockPos blockTwo = block.get(1);

        RenderLine.drawLine(
          new Vec3(blockOne.getX() + 0.5, blockOne.getY(), blockOne.getZ() + 0.5),
          new Vec3(blockTwo.getX() + 0.5, blockTwo.getY(), blockTwo.getZ() + 0.5),
          0.5F,
          Color.CYAN,
          event.partialTicks
        );
      }
    }
  }
}
