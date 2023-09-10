package com.dillo.pathfinding.mit.finder.walker;

import com.dillo.pathfinding.mit.finder.utils.ActionTypes;
import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.RayTracingUtils;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Utils {

  public static List<BlockPos> getShortList(List<BlockNodeClass> blocks) {
    List<BlockPos> returnBlocks = new ArrayList<>();
    BlockPos curBlock = BlockUtils.getCenteredBlock(blocks.get(0).blockPos);
    returnBlocks.add(curBlock);

    List<Vec3> pointList = new ArrayList<>();
    pointList.add(new Vec3(BlockSideVecs.LEFT.dx, 0, BlockSideVecs.LEFT.dz));
    pointList.add(new Vec3(BlockSideVecs.RIGHT.dx, 0, BlockSideVecs.RIGHT.dz));
    pointList.add(new Vec3(BlockSideVecs.BACKLEFT.dx, 0, BlockSideVecs.BACKLEFT.dz));
    pointList.add(new Vec3(BlockSideVecs.BACKRIGHT.dx, 0, BlockSideVecs.BACKRIGHT.dz));
    pointList.add(new Vec3(0, 0, 0));

    int curCount = 0;
    for (int i = 1; i < blocks.size(); i++) {
      BlockNodeClass curBlockClassNode = blocks.get(i);
      BlockPos curBlockArList = blocks.get(i).blockPos;
      BlockPos centered = BlockUtils.getCenteredBlock(curBlockArList);

      List<RayTracingUtils.CollisionResult> blocksIntersected = RayTracingUtils.getCollisionVecsList(
        curBlock.getX() + 0.5,
        curBlock.getY() - 1.5,
        curBlock.getZ() + 0.5,
        centered.getX(),
        centered.getY() - 1.5,
        centered.getZ(),
        DistanceFromTo.distanceFromTo(curBlock, centered)
      );

      if (blocksIntersected != null) {
        int airAmount = 0;
        for (RayTracingUtils.CollisionResult block : blocksIntersected) {
          Block blockType = BlockUtils.getBlockType(block.blockPos);
          if (blockType == Blocks.air) {
            airAmount++;
          }
        }

        //SendChat.chat(String.valueOf(airAmount));

        if (airAmount > 2) {
          returnBlocks.add(blocks.get(i - 1).blockPos);
          curBlock = blocks.get(i - 1).blockPos;
          curCount = 0;
          continue;
        }
      }

      for (Vec3 vec : pointList) {
        Vec3 cur = new Vec3(
          curBlockArList.getX() + vec.xCoord + 0.5,
          curBlockArList.getY() + 1.1,
          curBlockArList.getZ() + vec.zCoord + 0.5
        );

        //RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(curBlockArList), true);

        Vec3 vec3 = new Vec3(
          curBlock.getX() + vec.xCoord + 0.5,
          curBlock.getY() + 1.1,
          curBlock.getZ() + vec.zCoord + 0.5
        );

        MovingObjectPosition obj = ids.mc.theWorld.rayTraceBlocks(vec3, cur, true, true, true);

        //RenderPoints.renderPoint(vec3, 0.2, true);

        if (
          (obj != null && !obj.hitVec.equals(cur)) ||
          curBlockClassNode.actionType == ActionTypes.FALL ||
          curBlockClassNode.actionType == ActionTypes.BREAK ||
          curBlockClassNode.actionType == ActionTypes.JUMP ||
          Math.abs(curBlockArList.getY() - curBlock.getY()) > 0.001
        ) {
          returnBlocks.add(blocks.get(i - 1).blockPos);
          curBlock = blocks.get(i - 1).blockPos;
          curCount = 0;
          break;
        }
      }

      if (curCount >= 4) {
        curCount = 0;
        returnBlocks.add(blocks.get(i - 1).blockPos);
        curBlock = blocks.get(i - 1).blockPos;
        continue;
      }

      curCount++;
    }

    return returnBlocks;
  }

  public static boolean isTrenchInWay(BlockPos startBlock, BlockPos endBlock) {
    List<RayTracingUtils.CollisionResult> collisionResults = RayTracingUtils.getCollisionVecsList(
      startBlock.getX() + 0.5,
      startBlock.getY() - 1,
      startBlock.getZ() + 0.5,
      endBlock.getX() + 0.5,
      endBlock.getY() - 1,
      endBlock.getZ() + 0.5,
      DistanceFromTo.distanceFromTo(startBlock, endBlock)
    );

    return collisionResults != null && collisionResults.size() > 7;
  }

  public static boolean isLookingAtYaw(float yaw) {
    return (ids.mc.thePlayer.rotationYaw % 360 - 20 < yaw && ids.mc.thePlayer.rotationYaw % 360 + 20 > yaw);
  }

  public static boolean isCloseToNextBlock(BlockPos block) {
    return (
      DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition()) < 1.5 &&
      Math.abs(ids.mc.thePlayer.getPositionVector().yCoord - block.getY()) < 0.001
    );
  }

  public static boolean isCloseToJumpBlock() {
    BlockPos playerPosition = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
    EnumFacing playerFacing = ids.mc.thePlayer.getHorizontalFacing();

    BlockPos block1Pos = playerPosition.offset(playerFacing);
    BlockPos block2Pos = playerPosition.offset(playerFacing, 1);

    return (BlockUtils.isBlockSolid(block1Pos) || BlockUtils.isBlockSolid(block2Pos));
  }
}
