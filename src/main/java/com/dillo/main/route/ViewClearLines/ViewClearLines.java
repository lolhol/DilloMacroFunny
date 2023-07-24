package com.dillo.main.route.ViewClearLines;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.RenderLine;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewClearLines {

  private static List<BlockPos> routeBlocks = new ArrayList<>();
  public static boolean isRender = false;

  public static void viewClearLines() {
    if (currentRoute.currentRoute.size() > 0) {
      SendChat.chat(prefix.prefix + "Displaying lines!");
      routeBlocks = currentRoute.currentRoute;
      //SendChat.chat(String.valueOf(routeBlocks.size()));
      isRender = true;
    } else {
      SendChat.chat(prefix.prefix + "There are no blocks in ur route...");
    }
  }

  public static void clearLines() {
    isRender = false;
    routeBlocks = new ArrayList<>();
    SendChat.chat(prefix.prefix + "Stopped rendering!");
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (isRender) {
      for (int i = 0; i < routeBlocks.size(); i++) {
        BlockPos block1 = routeBlocks.get(i);
        BlockPos block2 = null;

        if (i + 1 >= routeBlocks.size()) {
          block2 = routeBlocks.get(0);
        } else {
          block2 = routeBlocks.get(i + 1);
        }

        if (block1 != null && block2 != null) {
          RenderLine.drawLine(
            new Vec3(block1.getX() + 0.5, block1.getY() + 2.54, block1.getZ() + 0.5),
            new Vec3(block2.getX() + 0.5, block2.getY() + 0.5, block2.getZ() + 0.5),
            1F,
            Color.white,
            event.partialTicks
          );
        }
      }
    }
  }
}
