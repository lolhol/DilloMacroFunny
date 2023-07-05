package com.dillo.utils;

import static com.dillo.utils.previous.random.IsSameBlock.isSameBlock;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class RayTracingUtils {

  public static Block[] blocksToIgnore = null;

  public static Vec3 adjustLook(BlockPos destBlock, Block[] blocksToIgnore) {
    double playerHeight = 1.64;
    RayTracingUtils.blocksToIgnore = blocksToIgnore;

    Vec3 destBlockCenter = new Vec3(destBlock.getX() + 0.5, destBlock.getY() + 0.5, destBlock.getZ() + 0.5);

    double distToBlockCenter = getDistance(
      new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + playerHeight, ids.mc.thePlayer.posZ),
      destBlockCenter
    );

    CollisionResult collision = getCollisionBlock(
      ids.mc.thePlayer.posX,
      ids.mc.thePlayer.posY + playerHeight,
      ids.mc.thePlayer.posZ,
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
          new double[] { ids.mc.thePlayer.posX, ids.mc.thePlayer.posY + playerHeight, ids.mc.thePlayer.posZ },
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
          ids.mc.thePlayer.posX,
          ids.mc.thePlayer.posY + playerHeight,
          ids.mc.thePlayer.posZ,
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

  public static CollisionResult getCollisionBlock(
    double x1,
    double y1,
    double z1,
    double x2,
    double y2,
    double z2,
    double maxDist
  ) {
    BlockPos destBlock = new BlockPos(x2, y2, z2);

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

    return collidingBlocks.size() > 0 ? collidingBlocks.get(0) : null;
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

    if (Double.isInfinite(d)) {
      return null;
    } else {
      double[] out = new double[ro.length];

      for (int i = 0; i < ro.length; i++) {
        out[i] = ro[i] + rd[i] * d;
      }
      return out;
    }
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

  static class CollisionResult {

    BlockPos blockPos;
    double[] output;

    public CollisionResult(BlockPos blockPos, double[] output) {
      this.blockPos = blockPos;
      this.output = output;
    }
  }
}
