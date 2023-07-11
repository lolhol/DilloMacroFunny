package com.dillo.dilloUtils;

import static com.dillo.dilloUtils.DilloDriveBlockDetection.getBlocksLayer;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.getBestPath;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.FromBlockToHP;
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
  public static GetMostOptimalPath.OptimalPath path = null;

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
      ArmadilloStates.currentState = null;
      SendChat.chat(prefix.prefix + "Done breaking! Moving to next vein!");
      TeleportToNextBlock.teleportToNextBlock();
    }
  }

  public static List<BlockPos> getBlocksAround(double yPos) {
    BlockPos referencePoint = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + yPos, ids.mc.thePlayer.posZ);
    List<BlockPos> blocks = new ArrayList<>();

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        BlockPos newBlock = new BlockPos(referencePoint.getX() + i, referencePoint.getY(), referencePoint.getZ() + j);

        if (!newBlock.equals(referencePoint)) {
          if (canAdd(newBlock)) {
            blocks.add(newBlock);
          }
        }
      }
    }

    //SendChat.chat(String.valueOf(blocks.size()));

    return blocks;
  }

  public static void putAllTogether() {
    float leftRightChoice = random.nextFloat();

    if (leftRightChoice < 0.5) {
      isLeft = true;
    } else {
      isLeft = false;
    }

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

  private static BlockPos getBestBlock(List<BlockPos> blocks, BlockPos block) {
    BlockPos currentBestBlock = blocks.get(0);
    float currentBestAngle = GetAnglePlayerToBlock.getAngleFromOneBlockToAnother(currentBestBlock, block);

    for (BlockPos bl : blocks) {
      float currAngle = GetAnglePlayerToBlock.getAngleFromOneBlockToAnother(currentBestBlock, bl);

      if (currentBestAngle > currAngle) {
        currentBestBlock = bl;
        currentBestAngle = currAngle;
      }
    }

    return currentBestBlock;
  }

  public static boolean canAddIgnorePanes(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    return blockType == Blocks.stained_glass;
  }

  public static boolean canAdd(BlockPos block) {
    Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

    return blockType == Blocks.stained_glass || blockType == Blocks.stained_glass_pane;
  }

  private static void maxAngle(List<BlockPos> blocks) {
    BlockPos block = blocks.get(0);
    lastBlockAngle = FromBlockToHP.getYaw(block.getX(), block.getY(), block.getZ());

    for (BlockPos blockPos : blocks) {
      float blockAngle = FromBlockToHP.getYaw(blockPos.getX(), blockPos.getY(), blockPos.getZ());

      if (blockAngle > lastBlockAngle) {
        lastBlockAngle = blockAngle;
      }
    }
  }
}
