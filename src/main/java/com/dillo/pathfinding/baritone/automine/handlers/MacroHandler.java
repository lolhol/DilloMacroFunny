package com.dillo.pathfinding.baritone.automine.handlers;

import com.dillo.pathfinding.baritone.automine.render.BlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import java.util.List;

public class MacroHandler {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean pickaxeSkillReady = true;

    public static boolean enabled = false;

    public static boolean finishedCommission = false;

    public static boolean restartHappening = false;

    public static boolean goldenGoblin = true;

    public static boolean kickOccurred = false;
    public static boolean miningSpeedActive = false;
    public static boolean outOfSoulflow = false;
    public static int miningSpeed = 1500;
    static BlockRenderer blockRenderer = new BlockRenderer();
    List<BlockPos> coords;
}
