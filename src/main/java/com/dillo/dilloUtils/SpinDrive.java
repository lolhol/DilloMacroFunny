package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.BlockUtils;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.degreeToRad;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class SpinDrive {

  public static float angleTudaSuda = 0;
  public static float angle = 0;
  public static java.util.Random random = new java.util.Random();
  public static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;

  public static void onStateSpinDrive() {
    boolean lookUnder = true;
    angle += 10 + random.nextFloat() * 10;
    if (angle > 360) {
      angle = 0;
    }

    ArrayList<BlockPos> blocklist = BlockUtils.mainBlockChecks();

    if (blocklist != null && blocklist.size() > 0) {
      KeyBinding.setKeyBindState(jump.getKeyCode(), true);
      BlockPos block = blocklist.get(0);
      BlockUtils.alreadyBroken.add(block);

      float y = block.getY() - 3;

      float anglePlayerToBlock = GetAnglePlayerToBlock.getAnglePlayerToBlock(block);
      angleTudaSuda = angle;

      if (anglePlayerToBlock != -1234567) {
        if (angleTudaSuda > 300) {
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

      int time = (int) (config.headMovement * 24.5);

      IBlockState blockUnder = ids.mc.theWorld.getBlockState(
        new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 2, ids.mc.thePlayer.posZ)
      );

      if (blockUnder.getBlock() == Blocks.stained_glass || blockUnder.getBlock() == Blocks.stained_glass_pane) {
        LookAt.smoothLook(LookAt.getRotation(new BlockPos(x, y, z)), time);
      } else {
        if (lookUnder) {
          if (block.getY() > ids.mc.thePlayer.posY) {
            jumpLook(new BlockPos(x + 0.25, y, z + 0.25), time);
          } else {
            LookAt.smoothLook(LookAt.getRotation(new BlockPos(x, y, z)), time);
          }
        } else {
          if (block.getY() - 0.3 > ids.mc.thePlayer.posY) {
            jumpLook(new BlockPos(x + 0.25, y, z + 0.25), time);
          }

          LookAt.smoothLook(LookAt.getRotation(new BlockPos(x, y, z)), time);
        }
      }
    } else {
      KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      ArmadilloStates.currentState = null;

      ArmadilloMain.newDilloCounter = 0;
      TeleportToNextBlock.teleportToNextBlock();
    }
  }

  public static void jumpLook(BlockPos block, long time) {
    new Thread(() -> {
      try {
        KeyBinding.setKeyBindState(jump.getKeyCode(), true);
        LookAt.smoothLook(LookAt.getRotation(block), time);
        Thread.sleep(10);
        KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      } catch (InterruptedException e) {}
    })
      .start();
  }
}
