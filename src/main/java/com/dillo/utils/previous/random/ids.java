package com.dillo.utils.previous.random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;

public class ids {

  public static final Minecraft mc = Minecraft.getMinecraft();
  public static final EntityPlayerSP player = mc.thePlayer;
  public static final WorldClient world = mc.theWorld;
  public static final PlayerControllerMP controller = mc.playerController;
}
