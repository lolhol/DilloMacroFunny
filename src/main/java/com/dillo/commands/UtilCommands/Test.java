package com.dillo.commands.UtilCommands;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.NewSpinDrive.random;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.getBestPath;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.data.config;
import com.dillo.dilloUtils.Utils.GetMostOptimalPath;
import com.dillo.dilloUtils.Utils.LookYaw;
import com.dillo.utils.previous.random.ids;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Test {

  public static boolean startRender = false;
  public static boolean first = true;
  public static int tickCount = 0;
  public static GetMostOptimalPath.OptimalPath path = null;
  public static float angleTudaSuda = 0;
  public static float angle = 0;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startRender) {
        if (first) {
          BlockPos refrenceBlock2 = new BlockPos(
            ids.mc.thePlayer.posX,
            ids.mc.thePlayer.posY + 2,
            ids.mc.thePlayer.posZ
          );
          List<BlockPos> returnList = getBlocksLayer(refrenceBlock2);
          refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 0, ids.mc.thePlayer.posZ);
          returnList.addAll(getBlocksLayer(refrenceBlock2));
          refrenceBlock2 = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ);
          returnList.addAll(getBlocksLayer(refrenceBlock2));

          float curYaw = curRotation();

          if (curYaw < 0) {
            curYaw = 360 + curYaw;
          }

          Test.path = getBestPath(returnList, curYaw);

          float displacement = path.displacement;

          LookYaw.lookToYaw(config.headMovement * 10L, displacement);

          first = false;
        }

        if (!first && tickCount >= 2) {
          if (angle < 170) {
            float add = config.headMovement * 7 + random.nextFloat() * 10;

            LookYaw.lookToYaw(config.headMovement * 10L, add);
            angle += add;
          } else {
            angle = 0;
            first = true;
            tickCount = 0;
            angleTudaSuda = 0;
            startRender = false;
          }
        }

        tickCount++;
      }
    }
  }
}
