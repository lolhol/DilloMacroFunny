package com.dillo.dilloUtils;

import static com.dillo.ArmadilloMain.CurrentState.STATEDILLONOGETTINGON;
import static com.dillo.data.config.attemptToClearOnSpot;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.StateDillo.canDilloOn;
import static com.dillo.dilloUtils.Teleport.SmartTP.smartTpBlocks;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.getBestPath;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.isClear;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.dilloUtils.Utils.GetMostOptimalPath;
import com.dillo.dilloUtils.Utils.LookYaw;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class NewSpinDrive {

  public static boolean isLeft = false;
  public static float angle = 0;
  public static java.util.Random random = new java.util.Random();
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
  public static List<DilloDriveBlockDetection.BlockAngle> returnBlocks = new ArrayList<>();
  public static float lastBlockAngle = 0;
  public static GetMostOptimalPath.OptimalPathRotation path = null;
  public static int driveClearCount = 0;

  public static void newSpinDrive() {
    if (angle < config.headRotationMax + 60) {
      KeyBinding.setKeyBindState(jump.getKeyCode(), true);
      float add = config.headMovement * 7 + random.nextFloat() * 10;

      if (isLeft) {
        LookYaw.lookToYaw(config.headMovement * 5L, -add);
      } else {
        LookYaw.lookToYaw(config.headMovement * 5L, add);
      }

      angle += add;
    } else {
      KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      angle = 0;
      lastBlockAngle = 0;

      if (
        canDilloOn() &&
        driveClearCount < 2 &&
        !smartTpBlocks.contains(currentRoute.currentBlock) &&
        attemptToClearOnSpot
      ) {
        isClear = true;
        ArmadilloStates.currentState = STATEDILLONOGETTINGON;
        driveClearCount++;
      } else {
        ArmadilloStates.currentState = null;
        SendChat.chat(prefix.prefix + "Done breaking! Moving to next vein!");
        TeleportToNextBlock.teleportToNextBlock();
      }
    }
  }

  public static void putAllTogether() {
    float leftRightChoice = random.nextFloat();

    if (leftRightChoice < 0.5) {
      isLeft = true;
    } else {
      isLeft = false;
    }

    //com.dillo.utils.previous.SendChat.chat(isLeft ? "left" : "right");

    List<BlockPos> returnList = new ArrayList<>();

    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ))
    );
    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ))
    );
    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ))
    );
    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 3, ids.mc.thePlayer.posZ))
    );

    float curYaw = curRotation();

    if (curYaw < 0) {
      curYaw = 360 + curYaw;
    }

    path = getBestPath(returnList, curYaw);

    float displacement = path.displacement;

    LookYaw.lookToYaw(config.rod_drill_switch_time + (config.headMovement * 10L), displacement);
  }

  public static boolean canAdd(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    return blockType == Blocks.stained_glass || blockType == Blocks.stained_glass_pane;
  }
}
