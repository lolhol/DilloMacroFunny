package com.dillo.main.esp.other;

import static com.dillo.config.config.armadildo;
import static com.dillo.config.config.armadilloDefeatr;

import com.dillo.utils.previous.random.ids;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StopRenderStand {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void cancelRendering(RenderLivingEvent.Pre<? extends EntityLivingBase> event) {
    EntityLivingBase entity = event.entity;

    String lower = entity.getName().toLowerCase();

    if (
      entity instanceof EntityArmorStand &&
      lower.contains(ids.mc.thePlayer.getName().toLowerCase()) &&
      lower.contains("armadillo") &&
      (armadilloDefeatr || armadildo)
    ) {
      event.setCanceled(true);
    }
  }
}
