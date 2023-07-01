package com.dillo.dilloUtils.BlockUtils;

import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GetUnobstructedPosFromCustom {

  public static Vec3 getUnobstructedPos(BlockPos block, BlockPos block1) {
    EntityPlayer player = ids.mc.thePlayer;

    Vec3 startPos = null;
    if (block1.getX() != ids.mc.thePlayer.posX && block1.getZ() != ids.mc.thePlayer.posZ) {
      startPos = new Vec3(block1.getX() + 0.5, block1.getY() + 1.54, block1.getZ() + 0.5);
    } else {
      startPos = new Vec3(block1.getX() + 0.5, block1.getY() + 0.54, block1.getZ() + 0.5);
    }

    World world = player.worldObj;
    Vec3 centerOfBlock = new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5);

    for (double offsetX = 0.0; offsetX < 0.5; offsetX += 0.05) {
      for (double offsetY = 0.0; offsetY < 0.5; offsetY += 0.05) {
        for (double offsetZ = 0.0; offsetZ < 0.5; offsetZ += 0.05) {
          for (int signX = -1; signX <= 1; signX += 2) {
            for (int signY = -1; signY <= 1; signY += 2) {
              for (int signZ = -1; signZ <= 1; signZ += 2) {
                double x = centerOfBlock.xCoord + offsetX * signX;
                double y = centerOfBlock.yCoord + offsetY * signY;
                double z = centerOfBlock.zCoord + offsetZ * signZ;

                MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(startPos, new Vec3(x, y, z));
                if (
                  movingObjectPosition == null ||
                  movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
                ) {
                  try {
                    return new Vec3(x, y, z);
                  } catch (NullPointerException e) {
                    SendChat.chat(String.valueOf(e));
                  }
                }
              }
            }
          }
        }
      }
    }

    return null;
  }
}
