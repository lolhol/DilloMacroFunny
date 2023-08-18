package com.dillo.utils;

import static com.dillo.utils.previous.random.IsSameBlock.isSameBlock;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;

public class RayTracingUtils {

  public static Block[] blocksToIgnore = null;
  public static ConcurrentLinkedQueue<BlockPos> blockPosConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();
  public static List<BlockPos> foundCollisions = new ArrayList<>();
  private static boolean isCheck = false;
  private static Vec3 destBlock1 = null;
  private static BlockPos destBlock2 = null;
  public static boolean isPercent = false;
  public static int minObstructions = 0;

  public static Vec3 adjustLook(Vec3 block1, BlockPos destBlock, Block[] blocksToIgnore, boolean isCheck) {
    double playerHeight = 1.54;

    RayTracingUtils.blocksToIgnore = blocksToIgnore;
    RayTracingUtils.destBlock1 = block1;
    RayTracingUtils.destBlock2 = destBlock;

    RayTracingUtils.isCheck = isCheck;

    Vec3 destBlockCenter = new Vec3(destBlock.getX() + 0.5, destBlock.getY() + 0.5, destBlock.getZ() + 0.5);

    //RenderOneBlockMod.renderOneBlock(destBlockCenter, true);

    double distToBlockCenter = getDistance(
      new Vec3(block1.xCoord, block1.yCoord + playerHeight, block1.zCoord),
      destBlockCenter
    );

    // TODO: Review @this code (the next like 10 lines)

    CollisionResult collision = getCollisionBlock(
      block1.xCoord,
      block1.yCoord + playerHeight,
      block1.zCoord,
      destBlockCenter.xCoord,
      destBlockCenter.yCoord,
      destBlockCenter.zCoord,
      distToBlockCenter
    );

    if (collision == null) {
      return null;
    }

    double radiusStep = 0.1;
    double radiusMax = Math.sqrt(3) / 2 + radiusStep;

    for (double radius = radiusStep; radius < radiusMax; radius += radiusStep) {
      double angleStep = (radiusMax / radius) * 5;
      for (double angle = 0; angle < 360 + angleStep; angle += angleStep) {
        Vec3 vec = getCylinderBaseVec(
          new double[] { block1.xCoord, block1.yCoord + playerHeight, block1.zCoord },
          new double[] { destBlockCenter.xCoord, destBlockCenter.yCoord, destBlockCenter.zCoord },
          angle,
          radius
        );

        Vec3 point = new Vec3(
          destBlockCenter.xCoord + vec.xCoord,
          destBlockCenter.yCoord + vec.yCoord,
          destBlockCenter.zCoord + vec.zCoord
        );

        CollisionResult collisionPoint = getCollisionBlock(
          block1.xCoord,
          block1.yCoord + playerHeight,
          block1.zCoord,
          point.xCoord,
          point.yCoord,
          point.zCoord,
          distToBlockCenter + Math.sqrt(3) / 2
        );

        try {
          if (collisionPoint != null && isSameBlock(collisionPoint.blockPos, destBlock)) {
            return point;
          }
        } catch (Exception ignored) {}
      }
    }

    return null;
  }

  public static Vec3 adjustLook(
    Vec3 block1,
    BlockPos destBlock,
    Block[] blocksToIgnore,
    boolean isCheck,
    int minObstructions
  ) {
    double playerHeight = 1.54;

    RayTracingUtils.blocksToIgnore = blocksToIgnore;
    RayTracingUtils.destBlock1 = block1;
    RayTracingUtils.destBlock2 = destBlock;

    RayTracingUtils.isCheck = isCheck;

    Vec3 destBlockCenter = new Vec3(destBlock.getX() + 0.5, destBlock.getY() + 0.5, destBlock.getZ() + 0.5);

    //RenderOneBlockMod.renderOneBlock(destBlockCenter, true);

    double distToBlockCenter = getDistance(
      new Vec3(block1.xCoord, block1.yCoord + playerHeight, block1.zCoord),
      destBlockCenter
    );

    if (minObstructions == -1) {
      CollisionResult collision = getCollisionBlock(
        block1.xCoord,
        block1.yCoord + playerHeight,
        block1.zCoord,
        destBlockCenter.xCoord,
        destBlockCenter.yCoord,
        destBlockCenter.zCoord,
        distToBlockCenter
      );

      if (collision == null) {
        return null;
      }
    }

    double radiusStep = 0.1;
    double radiusMax = Math.sqrt(3) / 2 + radiusStep;

    List<Vec3> points = new ArrayList<>();

    for (double radius = radiusStep; radius < radiusMax; radius += radiusStep) {
      double angleStep = (radiusMax / radius) * 5;
      for (double angle = 0; angle < 360 + angleStep; angle += angleStep) {
        Vec3 vec = getCylinderBaseVec(
          new double[] { block1.xCoord, block1.yCoord + playerHeight, block1.zCoord },
          new double[] { destBlockCenter.xCoord, destBlockCenter.yCoord, destBlockCenter.zCoord },
          angle,
          radius
        );

        Vec3 point = new Vec3(
          destBlockCenter.xCoord + vec.xCoord,
          destBlockCenter.yCoord + vec.yCoord,
          destBlockCenter.zCoord + vec.zCoord
        );

        CollisionResult collisionPoint = getCollisionBlock(
          block1.xCoord,
          block1.yCoord + playerHeight,
          block1.zCoord,
          point.xCoord,
          point.yCoord,
          point.zCoord,
          distToBlockCenter + Math.sqrt(3) / 2
        );

        try {
          if (collisionPoint != null && isSameBlock(collisionPoint.blockPos, destBlock)) {
            points.add(point);
          }
        } catch (Exception ignored) {}
      }
    }

    SendChat.chat("Found " + points.size());

    if (!points.isEmpty() && points.size() >= minObstructions) {
      // TODO: Test @this algorithm for speed
      return getValidPoint(points);
    }

    return null;
  }

  private static Vec3 getValidPoint(List<Vec3> points) {
    double bestAvg = 11100010;
    Vec3 bestPoint = null;

    for (Vec3 point : points) {
      double avg = getAvgDist(points, point);

      if (avg < bestAvg) {
        bestAvg = avg;
        bestPoint = point;
      }
    }

    return bestPoint;
  }

  private static double getAvgDist(List<Vec3> points, Vec3 vec) {
    double totalDist = 0;

    for (Vec3 point : points) {
      totalDist += getDistance(point, vec);
    }

    return points.isEmpty() ? 0 : totalDist / points.size();
  }

  public static CollisionResult getCollisionBlock(
    double x1,
    double y1,
    double z1,
    double x2,
    double y2,
    double z2,
    double maxDist
  ) {
    double dx = x2 - x1;
    double dy = y2 - y1;
    double dz = z2 - z1;

    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

    double stepX = dx / length;
    double stepY = dy / length;
    double stepZ = dz / length;

    double xCur = x1;
    double yCur = y1;
    double zCur = z1;

    List<CollisionResult> collidingBlocks = new ArrayList<>();
    for (int i = 0; i < maxDist + 1; i++) {
      for (int sx = -1; sx <= 1; sx++) {
        for (int sy = -1; sy <= 1; sy++) {
          for (int sz = -1; sz <= 1; sz++) {
            if (sx * stepX + sy * stepY + sz * stepZ < 0) {
              continue;
            }

            int x = (int) Math.floor(xCur + sx);
            int y = (int) Math.floor(yCur + sy);
            int z = (int) Math.floor(zCur + sz);

            IBlockState blockState = ids.mc.theWorld.getBlockState(new BlockPos(x, y, z));
            Block block = blockState.getBlock();

            if (isContains(blocksToIgnore, block)) {
              if (!foundCollisions.contains(new BlockPos(x, y, z))) foundCollisions.add(new BlockPos(x, y, z));

              if (!isCheck) {
                continue;
              } else {
                if (!canCheck(new BlockPos(x, y, z), destBlock1, destBlock2)) {
                  continue;
                }
              }
            }

            double[] ro = new double[] { x1, y1, z1 };
            double[] rd = new double[] { stepX, stepY, stepZ };
            double[][] aabb = new double[][] { { x, y, z }, { x + 1, y + 1, z + 1 } };
            double[] output = intersection(ro, rd, aabb);
            if (output == null) {
              continue;
            }

            if (getDistanceB(ro, new double[] { x + 0.5, y + 0.5, z + 0.5 }) > maxDist) {
              continue;
            }

            collidingBlocks.add(new CollisionResult(new BlockPos(x, y, z), output));
          }
        }
      }

      xCur += stepX;
      yCur += stepY;
      zCur += stepZ;
    }

    collidingBlocks.sort(Comparator.comparingDouble(a -> getDistanceB(new double[] { x1, y1, z1 }, a.output)));

    return !collidingBlocks.isEmpty() ? collidingBlocks.get(0) : null;
  }

  public static CollisionResult getCollisionVecs(
    double x1,
    double y1,
    double z1,
    double x2,
    double y2,
    double z2,
    double maxDist,
    Block[] blocksToIgnore
  ) {
    double dx = x2 - x1;
    double dy = y2 - y1;
    double dz = z2 - z1;

    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

    double stepX = dx / length;
    double stepY = dy / length;
    double stepZ = dz / length;

    double xCur = x1;
    double yCur = y1;
    double zCur = z1;

    List<CollisionResult> collidingBlocks = new ArrayList<>();
    for (int i = 0; i < maxDist + 1; i++) {
      for (int sx = -1; sx <= 1; sx++) {
        for (int sy = -1; sy <= 1; sy++) {
          for (int sz = -1; sz <= 1; sz++) {
            if (sx * stepX + sy * stepY + sz * stepZ < 0) {
              continue;
            }

            int x = (int) Math.floor(xCur + sx);
            int y = (int) Math.floor(yCur + sy);
            int z = (int) Math.floor(zCur + sz);

            IBlockState blockState = ids.mc.theWorld.getBlockState(new BlockPos(x, y, z));
            Block block = blockState.getBlock();

            if (isContains(blocksToIgnore, block)) {
              continue;
            }

            double[] ro = new double[] { x1, y1, z1 };
            double[] rd = new double[] { stepX, stepY, stepZ };
            double[][] aabb = new double[][] { { x, y, z }, { x + 1, y + 1, z + 1 } };
            double[] output = intersection(ro, rd, aabb);

            if (getDistanceB(ro, new double[] { x + 0.5, y + 0.5, z + 0.5 }) > maxDist) {
              continue;
            }

            collidingBlocks.add(new CollisionResult(new BlockPos(x, y, z), output));
          }
        }
      }

      xCur += stepX;
      yCur += stepY;
      zCur += stepZ;
    }

    return !collidingBlocks.isEmpty() ? collidingBlocks.get(0) : null;
  }

  public static List<CollisionResult> getCollisionVecsList(
    double x1,
    double y1,
    double z1,
    double x2,
    double y2,
    double z2,
    double maxDist
  ) {
    double dx = x2 - x1;
    double dy = y2 - y1;
    double dz = z2 - z1;

    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

    double stepX = dx / length;
    double stepY = dy / length;
    double stepZ = dz / length;

    double xCur = x1;
    double yCur = y1;
    double zCur = z1;

    List<CollisionResult> collidingBlocks = new ArrayList<>();
    for (int i = 0; i < maxDist + 1; i++) {
      for (int sx = -1; sx <= 1; sx++) {
        for (int sy = -1; sy <= 1; sy++) {
          for (int sz = -1; sz <= 1; sz++) {
            if (sx * stepX + sy * stepY + sz * stepZ < 0) {
              continue;
            }

            int x = (int) Math.floor(xCur + sx);
            int y = (int) Math.floor(yCur + sy);
            int z = (int) Math.floor(zCur + sz);

            double[] ro = new double[] { x1, y1, z1 };
            double[] rd = new double[] { stepX, stepY, stepZ };
            double[][] aabb = new double[][] { { x, y, z }, { x + 1, y + 1, z + 1 } };
            double[] output = intersection(ro, rd, aabb);

            if (getDistanceB(ro, new double[] { x + 0.5, y + 0.5, z + 0.5 }) > maxDist) {
              continue;
            }

            collidingBlocks.add(new CollisionResult(new BlockPos(x, y, z), output));
          }
        }
      }

      xCur += stepX;
      yCur += stepY;
      zCur += stepZ;
    }

    return !collidingBlocks.isEmpty() ? collidingBlocks : null;
  }

  public static MovingObjectPosition rayTraceBlocks(
    Vec3 vec31,
    Vec3 vec32,
    boolean stopOnLiquid,
    boolean ignoreBlockWithoutBoundingBox,
    boolean returnLastUncollidableBlock,
    Predicate<? super BlockPos> predicate,
    boolean visualize,
    boolean fullBlocks
  ) {
    if (!(Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))) {
      if (!(Double.isNaN(vec32.xCoord) || Double.isNaN(vec32.yCoord) || Double.isNaN(vec32.zCoord))) {
        MovingObjectPosition movingobjectposition;
        int i = MathHelper.floor_double(vec32.xCoord);
        int j = MathHelper.floor_double(vec32.yCoord);
        int k = MathHelper.floor_double(vec32.zCoord);
        int l = MathHelper.floor_double(vec31.xCoord);
        int i1 = MathHelper.floor_double(vec31.yCoord);
        int j1 = MathHelper.floor_double(vec31.zCoord);
        BlockPos blockpos = new BlockPos(l, i1, j1);
        IBlockState iblockstate = ids.mc.theWorld.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (visualize) blockPosConcurrentLinkedQueue.add(blockpos);
        if (
          !predicate.test(blockpos) &&
          (
            !ignoreBlockWithoutBoundingBox ||
            block.getCollisionBoundingBox(ids.mc.theWorld, blockpos, iblockstate) != null
          ) &&
          block.canCollideCheck(iblockstate, stopOnLiquid) &&
          (movingobjectposition = collisionRayTrace(block, blockpos, vec31, vec32, fullBlocks)) != null
        ) {
          return movingobjectposition;
        }
        MovingObjectPosition movingobjectposition2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
          EnumFacing enumfacing;
          if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
            return null;
          }
          if (l == i && i1 == j && j1 == k) {
            return returnLastUncollidableBlock ? movingobjectposition2 : null;
          }
          boolean flag2 = true;
          boolean flag = true;
          boolean flag1 = true;
          double d0 = 999.0;
          double d1 = 999.0;
          double d2 = 999.0;
          if (i > l) {
            d0 = (double) l + 1.0;
          } else if (i < l) {
            d0 = (double) l + 0.0;
          } else {
            flag2 = false;
          }
          if (j > i1) {
            d1 = (double) i1 + 1.0;
          } else if (j < i1) {
            d1 = (double) i1 + 0.0;
          } else {
            flag = false;
          }
          if (k > j1) {
            d2 = (double) j1 + 1.0;
          } else if (k < j1) {
            d2 = (double) j1 + 0.0;
          } else {
            flag1 = false;
          }
          double d3 = 999.0;
          double d4 = 999.0;
          double d5 = 999.0;
          double d6 = vec32.xCoord - vec31.xCoord;
          double d7 = vec32.yCoord - vec31.yCoord;
          double d8 = vec32.zCoord - vec31.zCoord;
          if (flag2) {
            d3 = (d0 - vec31.xCoord) / d6;
          }
          if (flag) {
            d4 = (d1 - vec31.yCoord) / d7;
          }
          if (flag1) {
            d5 = (d2 - vec31.zCoord) / d8;
          }
          if (d3 == -0.0) {
            d3 = -1.0E-4;
          }
          if (d4 == -0.0) {
            d4 = -1.0E-4;
          }
          if (d5 == -0.0) {
            d5 = -1.0E-4;
          }
          if (d3 < d4 && d3 < d5) {
            enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
            vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
          } else if (d4 < d5) {
            enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
            vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
          } else {
            enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
            vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
          }
          l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
          i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
          j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
          blockpos = new BlockPos(l, i1, j1);
          IBlockState iblockstate1 = ids.mc.theWorld.getBlockState(blockpos);
          Block block1 = iblockstate1.getBlock();
          if (visualize) blockPosConcurrentLinkedQueue.add(blockpos);
          if (
            ignoreBlockWithoutBoundingBox &&
            block1.getCollisionBoundingBox(ids.mc.theWorld, blockpos, iblockstate1) == null
          ) continue;
          if (predicate.test(blockpos)) continue;
          if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
            MovingObjectPosition movingobjectposition1 = collisionRayTrace(block1, blockpos, vec31, vec32, fullBlocks);
            if (movingobjectposition1 == null) continue;
            return movingobjectposition1;
          }
          movingobjectposition2 =
            new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
        }
        return returnLastUncollidableBlock ? movingobjectposition2 : null;
      }
      return null;
    }
    return null;
  }

  public static MovingObjectPosition rayTraceBlocks(
    Vec3 vec31,
    Vec3 vec32,
    boolean stopOnLiquid,
    boolean ignoreBlockWithoutBoundingBox,
    boolean returnLastUncollidableBlock,
    Predicate<? super BlockPos> predicate
  ) {
    return rayTraceBlocks(
      vec31,
      vec32,
      stopOnLiquid,
      ignoreBlockWithoutBoundingBox,
      returnLastUncollidableBlock,
      predicate,
      false,
      false
    );
  }

  public static MovingObjectPosition collisionRayTrace(
    Block block,
    BlockPos pos,
    Vec3 start,
    Vec3 end,
    boolean fullBlocks
  ) {
    start = start.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
    end = end.addVector(-pos.getX(), -pos.getY(), -pos.getZ());

    Vec3 vec3 = start.getIntermediateWithXValue(end, fullBlocks ? 0.0 : block.getBlockBoundsMinX());
    Vec3 vec31 = start.getIntermediateWithXValue(end, fullBlocks ? 1.0 : block.getBlockBoundsMaxX());
    Vec3 vec32 = start.getIntermediateWithYValue(end, fullBlocks ? 0.0 : block.getBlockBoundsMinY());
    Vec3 vec33 = start.getIntermediateWithYValue(end, fullBlocks ? 1.0 : block.getBlockBoundsMaxY());
    Vec3 vec34 = start.getIntermediateWithZValue(end, fullBlocks ? 0.0 : block.getBlockBoundsMinZ());
    Vec3 vec35 = start.getIntermediateWithZValue(end, fullBlocks ? 1.0 : block.getBlockBoundsMaxZ());

    if (!isVecInsideYZBounds(block, vec3, fullBlocks)) {
      vec3 = null;
    }
    if (!isVecInsideYZBounds(block, vec31, fullBlocks)) {
      vec31 = null;
    }
    if (!isVecInsideXZBounds(block, vec32, fullBlocks)) {
      vec32 = null;
    }
    if (!isVecInsideXZBounds(block, vec33, fullBlocks)) {
      vec33 = null;
    }
    if (!isVecInsideXYBounds(block, vec34, fullBlocks)) {
      vec34 = null;
    }
    if (!isVecInsideXYBounds(block, vec35, fullBlocks)) {
      vec35 = null;
    }

    Vec3 vec36 = null;

    if (vec3 != null) {
      vec36 = vec3;
    }
    if (vec31 != null && (vec36 == null || start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36))) {
      vec36 = vec31;
    }
    if (vec32 != null && (vec36 == null || start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36))) {
      vec36 = vec32;
    }
    if (vec33 != null && (vec36 == null || start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36))) {
      vec36 = vec33;
    }
    if (vec34 != null && (vec36 == null || start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36))) {
      vec36 = vec34;
    }
    if (vec35 != null && (vec36 == null || start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36))) {
      vec36 = vec35;
    }
    if (vec36 == null) {
      return null;
    }
    EnumFacing enumfacing = null;
    if (vec36 == vec3) {
      enumfacing = EnumFacing.WEST;
    }
    if (vec36 == vec31) {
      enumfacing = EnumFacing.EAST;
    }
    if (vec36 == vec32) {
      enumfacing = EnumFacing.DOWN;
    }
    if (vec36 == vec33) {
      enumfacing = EnumFacing.UP;
    }
    if (vec36 == vec34) {
      enumfacing = EnumFacing.NORTH;
    }
    if (vec36 == vec35) {
      enumfacing = EnumFacing.SOUTH;
    }
    return new MovingObjectPosition(vec36.addVector(pos.getX(), pos.getY(), pos.getZ()), enumfacing, pos);
  }

  private static boolean isVecInsideYZBounds(Block block, Vec3 point, boolean fullBlocks) {
    return (
      point != null &&
      point.yCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinY()) &&
      point.yCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxY()) &&
      point.zCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinZ()) &&
      point.zCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxZ())
    );
  }

  private static boolean isVecInsideXZBounds(Block block, Vec3 point, boolean fullBlocks) {
    return (
      point != null &&
      point.xCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinX()) &&
      point.xCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxX()) &&
      point.zCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinZ()) &&
      point.zCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxZ())
    );
  }

  private static boolean isVecInsideXYBounds(Block block, Vec3 point, boolean fullBlocks) {
    return (
      point != null &&
      point.xCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinX()) &&
      point.xCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxX()) &&
      point.yCoord >= (fullBlocks ? 0.0 : block.getBlockBoundsMinY()) &&
      point.yCoord <= (fullBlocks ? 1.0 : block.getBlockBoundsMaxY())
    );
  }

  private static IBlockState getBlockState(BlockPos blockPos) {
    if (ids.mc.theWorld == null) return null;
    return ids.mc.theWorld.getBlockState(blockPos);
  }

  private static boolean canCheck(BlockPos block, Vec3 routeBlock1, BlockPos routeBlock2) {
    if (
      (
        DistanceFromTo.distanceFromTo(block, BlockUtils.fromVec3ToBlockPos(routeBlock1)) < 4.61 ||
        DistanceFromTo.distanceFromTo(block, routeBlock2) < 4.61
      ) &&
      (
        ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass ||
        ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.stained_glass_pane
      )
    ) {
      return true;
    }

    SendChat.chat(String.valueOf(DistanceFromTo.distanceFromTo(block, BlockUtils.fromVec3ToBlockPos(routeBlock1))));

    return false;
  }

  public static boolean isContains(Block[] blocks, Block match) {
    for (Block block : blocks) {
      if (block == match) {
        return true;
      }
    }

    return false;
  }

  public static double[] intersection(double[] ro, double[] rd, double[][] aabb) {
    double d = distance(ro, rd, aabb);

    double[] out = new double[ro.length];

    for (int i = 0; i < ro.length; i++) {
      out[i] = ro[i] + rd[i] * d;
    }

    return out;
  }

  public static double distance(double[] ro, double[] rd, double[][] aabb) {
    int dims = ro.length;
    double lo = Double.NEGATIVE_INFINITY;
    double hi = Double.POSITIVE_INFINITY;

    for (int i = 0; i < dims; i++) {
      double dimLo = (aabb[0][i] - ro[i]) / rd[i];
      double dimHi = (aabb[1][i] - ro[i]) / rd[i];

      if (dimLo > dimHi) {
        double tmp = dimLo;
        dimLo = dimHi;
        dimHi = tmp;
      }

      if (dimHi < lo || dimLo > hi) {
        return Double.POSITIVE_INFINITY;
      }

      if (dimLo > lo) lo = dimLo;
      if (dimHi < hi) hi = dimHi;
    }

    return lo > hi ? Double.POSITIVE_INFINITY : lo;
  }

  public static Vec3 getCylinderBaseVec(double[] a, double[] b, double degree, double radius) {
    double x1 = a[0];
    double y1 = a[1];
    double z1 = a[2];
    double x2 = b[0];
    double y2 = b[1];
    double z2 = b[2];

    double dx = x2 - x1;
    double dy = y2 - y1;
    double dz = z2 - z1;

    if (Math.abs(dz) < 0.000001) {
      dz = 0.000001;
    }

    double planeCoef = x1 * dx + y1 * dy + z1 * dz;

    double xN = 0;
    double yN = 0;
    double zN = planeCoef / dz;
    double dxN = xN - x1;
    double dyN = yN - y1;
    double dzN = zN - z1;
    double lenN = Math.sqrt(dxN * dxN + dyN * dyN + dzN * dzN);
    dxN = dxN / lenN;
    dyN = dyN / lenN;
    dzN = dzN / lenN;

    double dxM = dy * dzN - dz * dyN;
    double dyM = dz * dxN - dx * dzN;
    double dzM = dx * dyN - dy * dxN;
    double cLen = Math.sqrt(dxM * dxM + dyM * dyM + dzM * dzM);
    dxM = dxM / cLen;
    dyM = dyM / cLen;
    dzM = dzM / cLen;

    double angle = degree * (Math.PI / 180);
    double dxP = dxN * Math.cos(angle) + dxM * Math.sin(angle);
    double dyP = dyN * Math.cos(angle) + dyM * Math.sin(angle);
    double dzP = dzN * Math.cos(angle) + dzM * Math.sin(angle);

    return new Vec3(dxP * radius, dyP * radius, dzP * radius);
  }

  public static double getDistance(Vec3 a, Vec3 b) {
    double dx = b.xCoord - a.xCoord;
    double dy = b.yCoord - a.yCoord;
    double dz = b.zCoord - a.zCoord;

    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public static double getDistanceB(double[] a, double[] b) {
    double dx = b[0] - a[0];
    double dy = b[1] - a[1];
    double dz = b[2] - a[2];

    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public static class CollisionResult {

    public BlockPos blockPos;
    public double[] output;

    public CollisionResult(BlockPos blockPos, double[] output) {
      this.blockPos = blockPos;
      this.output = output;
    }
  }
}
