package com.dillo.dilloUtils;

import static com.dillo.ArmadilloMain.CurrentState.STATEDILLONOGETTINGON;
import static com.dillo.data.config.attemptToClearOnSpot;
import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.DriveLook.addPitch;
import static com.dillo.dilloUtils.DriveLook.addYaw;
import static com.dillo.dilloUtils.StateDillo.canDilloOn;
import static com.dillo.dilloUtils.Teleport.IsOnBlock.yaw;
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
  public static GetMostOptimalPath.OptimalPath path = null;
  public static int driveClearCount = 0;

  public static void newSpinDrive() {
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(jump.getKeyCode(), true);

    if (isLeft) {
      addYaw(config.headMovement * 100L, -config.headRotationMax);
    } else {
      addYaw(config.headMovement * 100L, config.headRotationMax);
    }

    upDownMovement(config.headMovement * 100L, 30, 200, 100);

    new Thread(() -> {
      try {
        Thread.sleep(config.headMovement * 100L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      KeyBinding.setKeyBindState(jump.getKeyCode(), false);

      if (ArmadilloStates.isOnline()) {
        startAgain();
      }
    })
      .start();
  }

  private static void upDownMovement(long totalTime, float amount, float totalAngl, float fallOffAngle) {
    addPitch(totalTime / 3, -amount);

    new Thread(() -> {
      try {
        Thread.sleep(totalTime / 3);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      addPitch((totalTime / 3) * 2, yaw);
    })
      .start();
  }

  public static void startAgain() {
    if (
      canDilloOn() && driveClearCount < 2 && !smartTpBlocks.contains(currentRoute.currentBlock) && attemptToClearOnSpot
    ) {
      isClear = true;
      ArmadilloStates.currentState = STATEDILLONOGETTINGON;
      driveClearCount++;
    } else {
      ArmadilloStates.currentState = null;
      SendChat.chat(prefix.prefix + "Done breaking! Moving to next vein!");
      TeleportToNextBlock.teleportToNextBlock();
      // ADD A FASTER TP TO NEXT BLOCK MODULE VIA MAKING IT DISMOUNT A BIT EARLY
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
    LookYaw.lookToYaw(config.rod_drill_switch_time, displacement);
  }

  public static boolean canAdd(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    return blockType == Blocks.stained_glass || blockType == Blocks.stained_glass_pane;
  }
}
