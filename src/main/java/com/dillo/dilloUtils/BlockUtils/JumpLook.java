package com.dillo.dilloUtils.BlockUtils;

import com.dillo.dilloUtils.LookAt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JumpLook {

  private static boolean startJump = false;
  private static long timePoint = System.currentTimeMillis();
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;

  public static void jumpAndLook(BlockPos block, long time) {
    LookAt.smoothLook(LookAt.getRotation(block), time);
    KeyBinding.setKeyBindState(jump.getKeyCode(), true);
    timePoint = System.currentTimeMillis() + 15;
    startJump = true;
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (startJump) {
      if (timePoint - System.currentTimeMillis() == 0) {
        KeyBinding.setKeyBindState(jump.getKeyCode(), false);
        startJump = false;
      }
    }
  }
}
