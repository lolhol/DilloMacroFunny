package com.dillo.commands.UtilCommands;

import static com.dillo.ArmadilloMain.ArmadilloMain.test;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.getYawBlockAround;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.getBestPath;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;
import static com.dillo.utils.GetAngleToBlock.calcAngleFromYaw;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class DetectEntityUnderCommand extends Command {

  public boolean startRender = false;

  public DetectEntityUnderCommand() {
    super("testE");
  }

  @DefaultHandler
  public void handle() {
    BlockPos refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ);
    List<BlockPos> returnList = getBlocksLayer(refrenceBlock2);

    float curYaw = curRotation();

    if (curYaw < 0) {
      curYaw = 360 + curYaw;
    }

    SendChat.chat(String.valueOf(getBestPath(returnList, curYaw).displacement));
    //SendChat.chat("Testing");
    //currentRoute.curPlayerPos = ids.mc.thePlayer.getPosition();
    //test = !test;
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
