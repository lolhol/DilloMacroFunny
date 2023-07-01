package com.dillo.dilloUtils;

import com.dillo.utils.previous.random.ids;
import com.dillo.utils.radToDegree;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class GetAnglePlayerToBlock {

  public static float getAnglePlayerToBlock(BlockPos block) {
    double sideX = block.getX() + 0.5 - ids.mc.thePlayer.posX;
    double sideZ = block.getZ() + 0.5 - ids.mc.thePlayer.posZ;

    if (sideX == 0) {
      return -123456;
    }

    double tan = sideZ / sideX;
    double anglePlayerToBlock = radToDegree.radToDegree(Math.atan(tan));

    if (sideX < 0) {
      anglePlayerToBlock += 180;
    }

    return (float) anglePlayerToBlock;
  }

  public static float getAngleFromOneBlockToAnother(BlockPos block1, BlockPos block2) {
    double sideX = (block1.getX() + 0.5 - block2.getX() + 0.5) + 0.0001;
    double sideZ = (block1.getZ() + 0.5 - block2.getZ() + 0.5) + 0.0001;

    if (sideX == 0) {
      return -123456;
    }

    double tan = sideZ / sideX;
    double blockToBlockAngle = radToDegree.radToDegree(Math.atan(tan));

    if (sideX < 0) {
      blockToBlockAngle += 180;
    }

    return (float) blockToBlockAngle;
  }

  public static float getRequiredYaw(EntityLivingBase player, BlockPos blockPos) {
    double dX = blockPos.getX() + 0.5 - player.posX;
    double dZ = blockPos.getZ() + 0.5 - player.posZ;
    double dY = (blockPos.getY() + 0.5) - (player.posY + player.getEyeHeight());

    double distanceXZ = MathHelper.sqrt_double(dX * dX + dZ * dZ);
    double distanceY = MathHelper.sqrt_double(dX * dX + dY * dY + dZ * dZ);

    float yaw = (float) Math.toDegrees(-Math.atan2(dX, dZ));
    float pitch = (float) Math.toDegrees(-Math.atan2(dY, distanceXZ));

    // Adjust yaw if the block is to the right of the player
    if (dX < 0) {
      yaw += 180.0f;
    }

    if (yaw < 0.0f) {
      yaw += 360.0f;
    }

    return yaw;
  }
}
