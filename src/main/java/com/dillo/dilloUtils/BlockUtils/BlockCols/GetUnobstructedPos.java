package com.dillo.dilloUtils.BlockUtils.BlockCols;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GetUnobstructedPos {

  public static Vec3 getUnobstructedPos(BlockPos block) {
    EntityPlayer player = ids.mc.thePlayer; // Get the player object
    Vec3 playerPos = new Vec3(
      player.getPosition().getX() - 1,
      player.getPosition().getY() + 1.54,
      player.getPosition().getZ() - 1
    );

    World world = player.worldObj;
    Vec3 centerOfBlock = new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5);

    //RenderPoints.renderPoint(centerOfBlock, 0.4, true);

    for (double offsetX = 0.0; offsetX < 0.5; offsetX += 0.05) {
      for (double offsetY = 0.0; offsetY < 0.5; offsetY += 0.05) {
        for (double offsetZ = 0.0; offsetZ < 0.5; offsetZ += 0.05) {
          for (int signX = -1; signX <= 1; signX += 2) {
            for (int signY = -1; signY <= 1; signY += 2) {
              for (int signZ = -1; signZ <= 1; signZ += 2) {
                double x = centerOfBlock.xCoord + offsetX * signX;
                double y = centerOfBlock.yCoord + offsetY * signY;
                double z = centerOfBlock.zCoord + offsetZ * signZ;

                MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(playerPos, new Vec3(x, y, z));
                if (
                  movingObjectPosition == null ||
                  movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
                ) {
                  return new Vec3(x, y, z);
                }
              }
            }
          }
        }
      }
    }

    return null;
  }

  public static List<Block> getCollisionHitType(BlockPos block) {
    List<Block> collisionHitTypes = new ArrayList<>();

    EntityPlayer player = ids.mc.thePlayer; // Get the player object
    Vec3 playerPos = player.getPositionEyes(0);

    World world = player.worldObj;
    Vec3 centerOfBlock = new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5);

    //RenderPoints.renderPoint(centerOfBlock, 0.4, true);

    for (double offsetX = 0.0; offsetX < 0.5; offsetX += 0.05) {
      for (double offsetY = 0.0; offsetY < 0.5; offsetY += 0.05) {
        for (double offsetZ = 0.0; offsetZ < 0.5; offsetZ += 0.05) {
          for (int signX = -1; signX <= 1; signX += 2) {
            for (int signY = -1; signY <= 1; signY += 2) {
              for (int signZ = -1; signZ <= 1; signZ += 2) {
                double x = centerOfBlock.xCoord + offsetX * signX;
                double y = centerOfBlock.yCoord + offsetY * signY;
                double z = centerOfBlock.zCoord + offsetZ * signZ;

                MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(playerPos, new Vec3(x, y, z));
                if (
                  movingObjectPosition == null ||
                  movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
                ) {
                  // Check if the block at the given position is a glass pane
                  BlockPos blockAtPos = new BlockPos(x, y, z);
                  Block blockType = ids.mc.theWorld.getBlockState(blockAtPos).getBlock();

                  if (blockType != Blocks.air && !collisionHitTypes.contains(blockType)) {
                    collisionHitTypes.add(blockType);
                  }

                  AxisAlignedBB collisionBox = blockType.getCollisionBoundingBox(
                    world,
                    blockAtPos,
                    world.getBlockState(blockAtPos)
                  );
                  if (collisionBox == null || collisionBox.getAverageEdgeLength() >= 1.0) {
                    continue;
                  }
                }
              }
            }
          }
        }
      }
    }

    return collisionHitTypes;
  }
}
