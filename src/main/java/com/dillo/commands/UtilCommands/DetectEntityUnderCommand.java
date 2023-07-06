package com.dillo.commands.UtilCommands;

import static com.dillo.ArmadilloMain.ArmadilloMain.test;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class DetectEntityUnderCommand extends Command {

  public boolean startRender = false;

  public DetectEntityUnderCommand() {
    super("testE");
  }

  @DefaultHandler
  public void handle(int x, int y, int z) {
    //SendChat.chat(String.valueOf(getAngleToBlockPos(new BlockPos(x, y, z))));
    SendChat.chat("Testing");
    currentRoute.curPlayerPos = ids.mc.thePlayer.getPosition();
    test = !test;
  }

  public static double getAngleToBlockPos(BlockPos blockPos) {
    Minecraft mc = Minecraft.getMinecraft();
    EntityLivingBase player = mc.thePlayer;

    double xDiff = blockPos.getX() + 0.5 - player.posX;
    double zDiff = blockPos.getZ() + 0.5 - player.posZ;
    double yaw = Math.toDegrees(Math.atan2(-xDiff, zDiff));

    return yaw - (player.rotationYaw % 360);
  }
}
