package com.dillo.main.esp.route;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.CheckRoute.GetFailPointsList;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.DistFromXPlayer;
import com.dillo.utils.previous.BoxRenderer;
import com.dillo.utils.renderUtils.RenderString;
import java.awt.*;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockOnRouteESP {

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (config.renderRoute && currentRoute.currentRoute != null && currentRoute.currentRoute.size() > 0) {
      try {
        for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
          BlockPos block = currentRoute.currentRoute.get(i);

          if (DistFromXPlayer.distFromXPlayer(block.getX(), block.getY(), block.getZ()) <= config.routeDist) {
            if (GetFailPointsList.failListPoints.size() > 0 && GetFailPointsList.failListPoints.contains(i - 1)) {
              BoxRenderer.drawBox(
                block.getX(),
                block.getY(),
                block.getZ(),
                Color.white,
                (float) config.espWidth / 10,
                event.partialTicks
              );
            } else {
              BoxRenderer.drawBox(
                block.getX(),
                block.getY(),
                block.getZ(),
                SelectedColor.getSelectedColor(),
                (float) config.espWidth / 10,
                event.partialTicks
              );
            }

            RenderString.renderStr(
              String.valueOf(i + 1),
              block.getX() + 0.5,
              block.getY() - 1,
              block.getZ() + 0.5,
              event.partialTicks,
              false
            );
          } else if (i == 0) {
            BoxRenderer.drawBox(
              block.getX(),
              block.getY(),
              block.getZ(),
              SelectedColor.getSelectedColor(),
              (float) config.espWidth / 10,
              event.partialTicks
            );
            RenderString.renderStr(
              "start",
              block.getX() + 0.5,
              block.getY() - 1,
              block.getZ() + 0.5,
              event.partialTicks,
              false
            );
          }
        }
      } catch (Exception e) {
        System.out.println("LLL");
      }
    }
  }
}
