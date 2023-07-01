package com.dillo.utils.renderUtils.renderModules;

import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.RenderBox;
import java.awt.*;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderOneBlockMod {

  private static boolean startRender = false;
  private static Vec3 blockPos = null;

  public static void renderOneBlock(Vec3 block, boolean state) {
    startRender = state;
    blockPos = block;
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (startRender && blockPos != null) {
      try {
        Vec3 playerVec = new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
        Vec3 blockVec = new Vec3(ids.mc.thePlayer.posX + 3, ids.mc.thePlayer.posY + 4, ids.mc.thePlayer.posZ + 5);
        RenderBox.drawBox(blockPos.xCoord, blockPos.yCoord, blockPos.zCoord, Color.BLUE, 0.5F, event.partialTicks);
        //RenderLine.drawLine(playerVec, blockVec, 0.5F, Color.CYAN, event.partialTicks);
      } catch (Exception e) {}
    }
  }
}
