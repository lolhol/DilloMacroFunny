package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.BlockUtils;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.degreeToRad;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class MoreLegitSpinDrive {

  public static float angleTudaSuda = 0;
  public static float angle = 0;
  public static java.util.Random random = new java.util.Random();
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
  private static List<BlockPos> blockLookRoute = new ArrayList<>();
  private static int currentPos = 0;
  private static float mainAngle = 0;

  public static void getBlocks() {
    BlockPos playerPosition = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ);

    double y = ids.mc.thePlayer.posY + 2;

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        BlockPos block = new BlockPos(playerPosition.getX() + i, y, playerPosition.getZ() + j);

        if (!block.equals(playerPosition)) {
          blockLookRoute.add(new BlockPos(playerPosition.getX() + i, y, playerPosition.getZ() + j));
        }
      }
    }

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        BlockPos block = new BlockPos(playerPosition.getX() + i, y, playerPosition.getZ() + j);
        if (!block.equals(playerPosition)) {
          blockLookRoute.add(new BlockPos(playerPosition.getX() + i, y, playerPosition.getZ() + j));
        }
      }
    }
  }

  public static BlockPos makeNewBlock(double addx, double addy, double addz, BlockPos prevBlock) {
    return new BlockPos(prevBlock.getX() + addx, prevBlock.getY() + addy, prevBlock.getZ() + addz);
  }

  public static Block getBlock(BlockPos blockPos) {
    return ids.mc.theWorld.getBlockState(blockPos).getBlock();
  }

  public static void betterDrive() {
    boolean lookUnder = true;

    float angleAdd = 10 + random.nextFloat() * 10;
    angle += angleAdd;
    mainAngle += angleAdd;
    if (angle > 360) {
      angle = 0;
    }

    KeyBinding.setKeyBindState(jump.getKeyCode(), true);

    if (mainAngle < config.angleRotation) {
      BlockPos block = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + 1, ids.mc.thePlayer.posZ);
      float y = block.getY() - 3;

      float anglePlayerToBlock = GetAnglePlayerToBlock.getAnglePlayerToBlock(block);
      angleTudaSuda = angle;

      if (anglePlayerToBlock != -1234567) {
        if (angleTudaSuda > 360) {
          angleTudaSuda = 360 - angleTudaSuda;
        }

        angleTudaSuda += anglePlayerToBlock - 90;
      } else {
        lookUnder = false;
        y -= 5;
      }

      float radians = (float) degreeToRad.degreeToRad(angleTudaSuda);
      float dx = (float) (Math.cos(radians) * 5);
      float dz = (float) (Math.sin(radians) * 5);

      float x = (float) (block.getX() + dx + 0.5);
      float z = (float) (block.getZ() + dz + 0.5);

      long time = (int) (config.headMovement * 24.5);

      IBlockState blockUnder = ids.mc.theWorld.getBlockState(
        new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 2, ids.mc.thePlayer.posZ)
      );

      LookAt.smoothLook(LookAt.getRotation(new BlockPos(x, y + 2, z)), time);
    } else {
      KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      angle = 0;
      mainAngle = 0;
      ArmadilloStates.currentState = null;
      TeleportToNextBlock.teleportToNextBlock();
    }
  }
}
