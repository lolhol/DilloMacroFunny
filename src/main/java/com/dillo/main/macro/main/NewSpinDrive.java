package com.dillo.main.macro.main;

import static com.dillo.calls.CurrentState.STATEDILLONOGETTINGON;
import static com.dillo.config.config.attemptToClearOnSpot;
import static com.dillo.main.macro.main.StateDillo.canDilloOn;
import static com.dillo.main.teleport.macro.SmartTP.smartTpBlocks;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.isClearing;
import static com.dillo.main.utils.GetMostOptimalPath.getBestPath;
import static com.dillo.main.utils.GetMostOptimalPath.isClear;
import static com.dillo.main.utils.looks.DriveLook.addPitch;
import static com.dillo.main.utils.looks.DriveLook.addYaw;
import static com.dillo.main.utils.looks.LookYaw.curRotation;
import static com.dillo.utils.BlockUtils.getBlocksLayer;

import com.dillo.calls.ArmadilloStates;
import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.main.utils.GetMostOptimalPath;
import com.dillo.main.utils.looks.LookYaw;
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
  public static boolean isFirst = true;

  public static void newSpinDrive() {
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(jump.getKeyCode(), true);

    if (isLeft) {
      addYaw(config.headMovement, -config.headRotationMax);
    } else {
      addYaw(config.headMovement, config.headRotationMax);
    }

    if (!isClearing) {
      upDownMovement(config.headMovement, 20, 200, 100);
      isClearing = false;
    }

    new Thread(() -> {
      try {
        Thread.sleep(config.headMovement);
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
    new Thread(() -> {
      addPitch(totalTime / 3, -amount);

      try {
        Thread.sleep((totalTime / 4) * 2);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      addPitch(totalTime / 3, amount);
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
