package com.dillo.pathfinding.mit.finder.walker;

import com.dillo.pathfinding.mit.finder.utils.ActionTypes;
import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.RayTracingUtils;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;

public class Utils {

  public static boolean isCloseToEnd = false;

  public static List<BlockPos> getShortList(List<BlockNodeClass> blocks) {
    boolean added = false;

    List<BlockPos> returnBlocks = new ArrayList<>();
    blocks.remove(0);
    BlockPos curBlock = BlockUtils.getCenteredBlock(blocks.get(0).blockPos);
    //returnBlocks.add(curBlock);

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

      if (added) {
        added = false;
        returnBlocks.add(blocks.get(i - 1).blockPos);
        curBlock = blocks.get(i - 1).blockPos;
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
          curBlockClassNode.actionType == ActionTypes.BREAK ||
          curBlockClassNode.actionType == ActionTypes.JUMP
        ) {
          returnBlocks.add(blocks.get(i - 1).blockPos);
          curBlock = blocks.get(i - 1).blockPos;

          curCount = 0;
          break;
        }
      }
      /*if (curCount >= 4) {
        curCount = 0;
        returnBlocks.add(blocks.get(i - 1).blockPos);
        curBlock = blocks.get(i - 1).blockPos;
        continue;
      }

      curCount++;*/
    }

    returnBlocks.add(blocks.get(blocks.size() - 1).blockPos);

    return returnBlocks;
  }

  public static List<BlockPos> getShortListV2(List<BlockNodeClass> blocks) {
    boolean added = false;

    List<BlockPos> returnBlocks = new ArrayList<>();
    BlockPos curBlock = BlockUtils.getCenteredBlock(blocks.get(0).blockPos);
    returnBlocks.add(curBlock);

    int curCount = 0;
    for (int i = 1; i < blocks.size(); i++) {
      BlockNodeClass curBlockClassNode = blocks.get(i);
      BlockPos curBlockArList = blocks.get(i).blockPos;
      BlockPos centered = BlockUtils.getCenteredBlock(curBlockArList);

      if (isGood(BlockUtils.fromBlockPosToVec3(centered))) {
        returnBlocks.add(centered);
      }
    }

    returnBlocks.add(blocks.get(blocks.size() - 1).blockPos);

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
      DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition()) < 2 &&
      Math.abs(ids.mc.thePlayer.getPositionVector().yCoord - block.getY()) < 0.001
    );
  }

  public static boolean isCloseToJumpBlock() {
    BlockPos playerPosition = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);
    EnumFacing playerFacing = ids.mc.thePlayer.getHorizontalFacing();

    BlockPos block1Pos = playerPosition.offset(playerFacing);
    BlockPos block2Pos = playerPosition.offset(playerFacing, 1);

    return BlockUtils.isBlockSolid(block1Pos) || BlockUtils.isBlockSolid(block2Pos);
  }

  private static Vec3 goodPoints(ArrayList<Vec3> path) {
    ArrayList<Vec3> reversed = new ArrayList<>(path);
    Collections.reverse(reversed);
    for (Vec3 vec : reversed
      .stream()
      .filter(vec -> new BlockPos(vec).getY() == ids.mc.thePlayer.getPosition().getY())
      .collect(Collectors.toList())) {
      if (isGood(vec)) {
        return vec;
      }
    }
    return null;
  }

  private static boolean isGood(Vec3 point) {
    if (point == null) return false;
    Vec3 topPoint = point.add(new Vec3(0, 2, 0));

    Vec3 topPos = ids.mc.thePlayer.getPositionVector().addVector(0, 1, 0);
    Vec3 botPos = ids.mc.thePlayer.getPositionVector();
    Vec3 underPos = ids.mc.thePlayer.getPositionVector().addVector(0, -1, 0);

    Vec3 directionTop = getLook(topPoint);
    directionTop = scaleVec(directionTop, 0.5f);
    for (int i = 0; i < Math.round(topPoint.distanceTo(ids.mc.thePlayer.getPositionEyes(1))) * 2; i++) {
      IBlockState topBlockState = ids.mc.theWorld.getBlockState(new BlockPos(topPos));
      if (
        topBlockState.getBlock().getCollisionBoundingBox(ids.mc.theWorld, new BlockPos(topPos), topBlockState) != null
      ) return false;

      IBlockState botBlockState = ids.mc.theWorld.getBlockState(new BlockPos(botPos));
      if (
        botBlockState.getBlock().getCollisionBoundingBox(ids.mc.theWorld, new BlockPos(botPos), botBlockState) != null
      ) return false;

      IBlockState underBlockState = ids.mc.theWorld.getBlockState(new BlockPos(underPos));
      if (
        underBlockState.getBlock().getCollisionBoundingBox(ids.mc.theWorld, new BlockPos(underPos), underBlockState) !=
        null
      ) return false;

      underPos = underPos.add(directionTop);
    }
    return true;
  }

  public static Vec3 getLook(final Vec3 vec) {
    final double diffX = vec.xCoord - ids.mc.thePlayer.posX;
    final double diffY = vec.yCoord - (ids.mc.thePlayer.posY + ids.mc.thePlayer.getEyeHeight());
    final double diffZ = vec.zCoord - ids.mc.thePlayer.posZ;
    final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
    return getVectorForRotation(
      (float) (-(MathHelper.atan2(diffY, dist) * 180.0 / 3.141592653589793)),
      (float) (MathHelper.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0)
    );
  }

  public static Vec3 getVectorForRotation(final float pitch, final float yaw) {
    final float f2 = -MathHelper.cos(-pitch * 0.017453292f);
    return new Vec3(
      MathHelper.sin(-yaw * 0.017453292f - 3.1415927f) * f2,
      MathHelper.sin(-pitch * 0.017453292f),
      MathHelper.cos(-yaw * 0.017453292f - 3.1415927f) * f2
    );
  }

  public static Vec3 scaleVec(Vec3 vec, float scale) {
    return new Vec3(vec.xCoord * scale, vec.yCoord * scale, vec.zCoord * scale);
  }
}
