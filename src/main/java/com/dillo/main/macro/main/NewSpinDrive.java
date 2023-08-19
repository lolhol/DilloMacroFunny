package com.dillo.main.macro.main;

import static com.dillo.calls.CurrentState.STATEDILLONOGETTINGON;
import static com.dillo.config.config.attemptToClearOnSpot;
import static com.dillo.main.macro.main.StateDillo.canDilloOn;
import static com.dillo.main.teleport.macro.SmartTP.smartTpBlocks;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.isThrowRod;
import static com.dillo.main.utils.GetMostOptimalPath.getBestPath;
import static com.dillo.main.utils.GetMostOptimalPath.isClear;
import static com.dillo.main.utils.keybinds.AllKeybinds.JUMP;
import static com.dillo.main.utils.looks.DriveLook.addPitch;
import static com.dillo.main.utils.looks.DriveLook.addYaw;
import static com.dillo.utils.BlockUtils.getBlocksLayer;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.events.macro.OnStartJumpEvent;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.main.utils.GetMostOptimalPath;
import com.dillo.main.utils.looks.LookYaw;
import com.dillo.utils.RandomisationUtils;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.random.ThreadUtils;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NewSpinDrive {

  public static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
  public static boolean isLeft = false;
  public static float angle = 0;
  public static java.util.Random random = new java.util.Random();
  public static GetMostOptimalPath.OptimalPath path = null;
  public static List<BlockPos> originalBlocks = new ArrayList<>();
  public static int driveClearCount = 0;
  public static boolean isFirst = true;
  private static boolean isDone;

  public static void newSpinDrive() {
    new Thread(() -> {
      KeyBinding.setKeyBindState(JUMP.getKeyCode(), true);

      upDownMovement(config.headMovement, config.headMoveUp + RandomisationUtils.randomNumberBetweenInt(-5, 5));
      addYaw(config.headMovement, config.headRotationMax + RandomisationUtils.randomNumberBetweenInt(-25, 25));

      ThreadUtils.threadSleepRandom(config.headMovement);

      KeyBinding.setKeyBindState(jump.getKeyCode(), false);

      Pair<Boolean, Integer> pair = isAdminPreventing(originalBlocks);

      if (ArmadilloStates.isOnline()) {
        // Later => add the % vein

        //TODO: test @this
        /*try {
          Thread.sleep(RandomisationUtils.randomNumberBetweenInt(50, 100));
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }*/

        SendChat.chat(prefix.prefix + "Done breaking! Moving to next vein!");

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
          SendChat.chat(prefix.prefix + "Initiated TP.");
          isThrowRod = true;
          ArmadilloStates.currentState = null;
          TeleportToNextBlock.teleportToNextBlock();
          // ADD A FASTER TP TO NEXT BLOCK MODULE VIA MAKING IT DISMOUNT A BIT EARLY
        }
      } else {
        SendChat.chat(prefix.prefix + "Detected Macro stop after rotation! Stopping.");
        ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        ArmadilloStates.currentState = null;
      }
    })
      .start();
  }

  private static Pair<Boolean, Integer> isAdminPreventing(List<BlockPos> prevBlocks) {
    int air = 0;
    int nonair = 0;

    for (BlockPos block : prevBlocks) {
      if (
        ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass ||
        ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane
      ) {
        nonair++;
      } else {
        air++;
      }
    }

    return new Pair<>(prevBlocks.size() == air || prevBlocks.size() - 3 < air, nonair);
  }

  private static void upDownMovement(long totalTime, float amount) {
    new Thread(() -> {
      addPitch(totalTime / 2, -amount);
      try {
        Thread.sleep(totalTime / 2);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      addPitch(totalTime / 2, amount / 2);
    })
      .start();
  }

  public static void startAgain() {}

  public static void putAllTogether() {
    //com.dillo.utils.previous.SendChaxt.chat(isLeft ? "left" : "right");

    List<BlockPos> returnList = new ArrayList<>();

    /*returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 2, ids.mc.thePlayer.posZ))
    );*/
    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ))
    );
    returnList.addAll(
      getBlocksLayer(new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ))
    );

    isLeft = false;
    path = getBestPath(returnList, 0);
    float displacement = path.displacement;
    LookYaw.lookToYaw(config.rod_drill_switch_time + 200, displacement, true);
  }

  public static void debugText(List<BlockPos> startBlocks) {
    List<BlockPos> blocks = new ArrayList<>();

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 5; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos block = ids.mc.thePlayer.getPosition().add(x, y, z).add(-1, 0, -1);
          //RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(block), true);

          if (
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass ||
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane
          ) blocks.add(block);
        }
      }
    }

    double both = (double) blocks.size() / (double) startBlocks.size();

    SendChat.chat(100 - (both * 100) + "%");
  }

  public static List<BlockPos> getBlocks() {
    List<BlockPos> blocks = new ArrayList<>();
    //RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 4; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos block = ids.mc.thePlayer.getPosition().add(x, y, z).add(-1, 0, -1);
          //RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(block), true);

          if (
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass ||
            ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane
          ) blocks.add(block);
        }
      }
    }

    return blocks;
  }

  public static GetMostOptimalPath.OptimalPath getBestSidePath(List<BlockPos> blocks) {
    float total1 = 0;
    float total2 = 0;

    isLeft = true;
    GetMostOptimalPath.OptimalPath path1 = getBestPath(blocks, 0);
    isLeft = false;
    GetMostOptimalPath.OptimalPath path2 = getBestPath(blocks, 0);

    total1 += path1.path.size();
    total2 += path2.path.size();

    total1 += path1.displacement;
    total2 += path2.displacement;

    if (total1 > total2) {
      isLeft = true;
      return path1;
    } else {
      isLeft = false;
      return path2;
    }
  }

  @SubscribeEvent
  public void onJump(OnStartJumpEvent event) {
    if (!ArmadilloStates.isOnline() || !isFirst || isDone) return;
    isDone = true;
    //SendChat.chat(String.valueOf(System.currentTimeMillis()));
    //KeyBinding.setKeyBindState(jump.getKeyCode(), true);
  }
}
