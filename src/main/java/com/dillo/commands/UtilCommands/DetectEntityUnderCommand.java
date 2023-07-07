package com.dillo.commands.UtilCommands;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.getBestPath;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.util.List;
import net.minecraft.util.BlockPos;

public class DetectEntityUnderCommand extends Command {

  public DetectEntityUnderCommand() {
    super("testE");
  }

  @DefaultHandler
  public void handle() {
    Test.first = true;
    BlockPos refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ);
    List<BlockPos> returnList = getBlocksLayer(refrenceBlock2);

    float curYaw = curRotation();

    if (curYaw < 0) {
      curYaw = 360 + curYaw;
    }

    SendChat.chat(String.valueOf(getBestPath(returnList, curYaw).displacement));
    Test.path = getBestPath(returnList, curYaw);
    Test.startRender = !Test.startRender;
    //SendChat.chat("Testing");
    //currentRoute.curPlayerPos = ids.mc.thePlayer.getPosition();
    //test = !test;
  }
}
