package com.dillo.commands.UtilCommands;

import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class WalkToCustom extends Command {

  public static boolean startRender = false;

  public WalkToCustom() {
    super("walkToCustom");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    long time = System.currentTimeMillis();
    Vec3 found = adjustLook(
      ids.mc.thePlayer.getPositionVector(),
      new BlockPos(x, y, z),
      new net.minecraft.block.Block[] { Blocks.air },
      false,
      10
    );

    SendChat.chat("Took " + String.valueOf((double) System.currentTimeMillis() - time));

    LookAt.smoothLook(LookAt.getRotation(found), 200);
  }
}
