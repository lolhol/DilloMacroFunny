package com.dillo.Pathfinding.baritone.automine.handlers;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.HIGHEST;

import com.dillo.Pathfinding.baritone.automine.render.BlockRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class MacroHandler {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static boolean pickaxeSkillReady = true;

  public static boolean enabled = false;

  public static boolean finishedCommission = false;

  public static boolean restartHappening = false;

  public static boolean goldenGoblin = true;

  public static boolean kickOccurred = false;

  static BlockRenderer blockRenderer = new BlockRenderer();

  public static boolean miningSpeedActive = false;

  public static boolean outOfSoulflow = false;

  List<BlockPos> coords;

  public static int miningSpeed = 1500;
}
