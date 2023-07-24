package com.dillo.main.route.Utils;

import static com.dillo.config.config.nukerDigUnder;
import static com.dillo.main.route.AutoSetup.SetupMain.updateVariablesAutoSetup;
import static com.dillo.main.route.LegitRouteClear.LegitRouteClear.startLegit;
import static com.dillo.main.route.Nuker.NukerMain.nukerStart;
import static com.dillo.main.route.Utils.IsAbleToMine.isAbleToMine;
import static com.dillo.utils.BlockUtils.makeNewBlock;

import com.dillo.config.config;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import java.util.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class GetBlocksForNuker {

  public static List<BlockPos> Blockss = new ArrayList<>();
  public static int currBlocksOfAir = 0;
  private static String call = null;

  public static void getBlocks(List<BlockPos> blocksOnRoute, String callNext) {
    call = callNext;

    new Thread(() -> {
      currBlocksOfAir = 0;
      List<BlockPos> blocks = new ArrayList<>();

      for (int i = 0; i < blocksOnRoute.size(); i++) {
        int second = i + 1;

        if (i == blocksOnRoute.size() - 1 && blocksOnRoute.size() > 2) {
          second = 0;
        }

        if (second < blocksOnRoute.size()) {
          BlockPos block = blocksOnRoute.get(i);

          if (!config.polarBlockDetection) {
            if (nukerDigUnder) {
              blocks.addAll(digHoleUnder(block));
            }

            blocks.addAll(
              findBlocks(
                new Vec3(block.getX() + 0.5, block.getY() + 1.64, block.getZ() + 0.5),
                BlockUtils.fromBlockPosToVec3(blocksOnRoute.get(second)),
                1
              )
            );
          } else {
            BlockPos secondBlock = blocksOnRoute.get(second);

            blocks.addAll(
              polarGetBlocks(
                block.getX(),
                block.getY(),
                block.getZ(),
                secondBlock.getX(),
                secondBlock.getY(),
                secondBlock.getZ()
              )
            );
          }
        }
      }

      Blockss = blocks;

      doneGettingBlocks();
    })
      .start();
  }

  private static List<BlockPos> digHoleUnder(BlockPos block) {
    List<BlockPos> blocks = new ArrayList<>();

    for (int x = -1; x <= 1; x++) {
      for (int y = -3; y <= 0; y++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos curBlock = makeNewBlock(x, y, z, block);

          if (isAbleToMine(curBlock)) {
            blocks.add(curBlock);
          }
        }
      }
    }

    blocks.sort((a, b) -> {
      return DistanceFromTo.distanceFromTo(block, a) < DistanceFromTo.distanceFromTo(block, b) ? -1 : 1;
    });

    return blocks;
  }

  public static void doneGettingBlocks() {
    if (call == "nuker") {
      nukerStart();
    } else if (call == "legit") {
      startLegit();
    } else if (call == "AUTOSETUP") {
      updateVariablesAutoSetup(Blockss);
    }
  }

  public static List<BlockPos> findBlocks(Vec3 pos1, Vec3 pos2, double cylRad) {
    List<BlockPos> finalBlockPos = new ArrayList<>();

    pos2 = new Vec3(pos2.xCoord + 0.5, pos2.yCoord, pos2.zCoord + 0.5);

    double dx = pos2.xCoord - pos1.xCoord;
    double dy = pos2.yCoord - pos1.yCoord;
    double dz = pos2.zCoord - pos1.zCoord;
    double blockOAir = 0;

    finalBlockPos.add(new BlockPos(pos1.xCoord, pos1.yCoord, pos1.zCoord));
    finalBlockPos.add(new BlockPos(pos1.xCoord, pos1.yCoord + 1, pos1.zCoord));
    finalBlockPos.add(new BlockPos(pos1.xCoord, pos1.yCoord + 2, pos1.zCoord));

    if (Math.abs(dz) < 0.000001) {
      dz = 0.000001;
    }

    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
    double stepX = dx / length;
    double stepY = dy / length;
    double stepZ = dz / length;

    double planeCoef = pos1.xCoord * dx + pos1.yCoord * dy + pos1.zCoord * dz;

    double xN = 0;
    double yN = 0;
    double zN = planeCoef / dz;
    double dxN = xN - pos1.xCoord;
    double dyN = yN - pos1.yCoord;
    double dzN = zN - pos1.zCoord;
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

    for (int i = 0; i < length; i++) {
      for (int degree = 0; degree < 360; degree += 10) {
        double angle = degree * (Math.PI / 180);
        double dxP = dxN * Math.cos(angle) + dxM * Math.sin(angle);
        double dyP = dyN * Math.cos(angle) + dyM * Math.sin(angle);
        double dzP = dzN * Math.cos(angle) + dzM * Math.sin(angle);

        double newX = stepX * i + pos1.xCoord + dxP * cylRad;
        double newY = stepY * i + pos1.yCoord + dyP * cylRad;
        double newZ = stepZ * i + pos1.zCoord + dzP * cylRad;

        BlockPos block = new BlockPos(newX, newY, newZ);

        boolean found = false;

        for (int j = 0; j < finalBlockPos.size() - 1; j++) {
          if (
            finalBlockPos.get(j).getX() == block.getX() &&
            finalBlockPos.get(j).getY() == block.getY() &&
            finalBlockPos.get(j).getZ() == block.getZ()
          ) {
            found = true;
          }
        }

        if (!found) {
          if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.air) {
            blockOAir++;
          } else {
            finalBlockPos.add(block);
          }
        }
      }
    }

    finalBlockPos.add(new BlockPos(pos2.xCoord, pos2.yCoord, pos2.zCoord));
    finalBlockPos.add(new BlockPos(pos2.xCoord, pos2.yCoord + 1, pos2.zCoord));
    finalBlockPos.add(new BlockPos(pos2.xCoord, pos2.yCoord + 2, pos2.zCoord));

    currBlocksOfAir = (int) blockOAir;

    if (cylRad > 0.6) {
      List<BlockPos> midList = findBlocks(pos1, pos2, 0.5);

      for (BlockPos block : midList) {
        if (!finalBlockPos.contains(block)) {
          finalBlockPos.add(block);
        }
      }
    }

    finalBlockPos.sort((a, b) -> {
      if (
        DistanceFromTo.distanceFromTo(a, BlockUtils.fromVec3ToBlockPos(pos1)) <
        DistanceFromTo.distanceFromTo(b, BlockUtils.fromVec3ToBlockPos(pos1))
      ) {
        return -1;
      } else if (
        DistanceFromTo.distanceFromTo(a, BlockUtils.fromVec3ToBlockPos(pos1)) ==
        DistanceFromTo.distanceFromTo(b, BlockUtils.fromVec3ToBlockPos(pos1))
      ) {
        return 0;
      } else {
        return 1;
      }
    });

    return finalBlockPos;
  }

  public static List<BlockPos> middle(List<BlockPos> alrAdded, Vec3 block1, Vec3 block2) {
    List<BlockPos> finalBlockPos = new ArrayList<>();

    double dx = block2.xCoord - block1.xCoord;
    double dy = block2.yCoord - block1.yCoord;
    double dz = block2.zCoord - block1.zCoord;

    if (Math.abs(dz) < 0.000001) {
      dz = 0.000001;
    }

    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
    double stepX = dx / length;
    double stepY = dy / length;
    double stepZ = dz / length;

    for (int i = 0; i < length; i++) {
      BlockPos newBlock = new BlockPos(stepX * i + block1.xCoord, stepY * i + block1.yCoord, stepZ * i + block1.zCoord);

      if (ids.mc.theWorld.getBlockState(newBlock).getBlock() != Blocks.air && !alrAdded.contains(newBlock)) {
        finalBlockPos.add(newBlock);
      }
    }

    return finalBlockPos;
  }

  public static List<BlockPos> polarGetBlocks(double x1, double y1, double z1, double x2, double y2, double z2) {
    List<Integer> mineX = new ArrayList<>();
    List<Integer> mineY = new ArrayList<>();
    List<Integer> mineZ = new ArrayList<>();

    y1 = y1; // dumb 18 fucked up coordinates
    y2 = y2;
    mineX.add((int) Math.floor(x1));
    mineY.add((int) Math.floor(y1));
    mineZ.add((int) Math.floor(z1));
    mineX.add((int) Math.floor(x1));
    mineY.add((int) Math.floor(y1) + 1);
    mineZ.add((int) Math.floor(z1));
    mineX.add((int) Math.floor(x1));
    mineY.add((int) Math.floor(y1) + 2);
    mineZ.add((int) Math.floor(z1));
    x1 = x1 + 0.5;
    y1 = y1 + 2.62 - 3.0 / 32.0; // shifting apparently lowers eye level by 3/32 block, needs testing. **should be fixed need more tests
    z1 = z1 + 0.5;
    x2 = x2 + 0.5;
    y2 = y2 + 0.5; // either Y or Y + 0.5 depending on what 18's macro looks at. currently his macro looks at Y
    z2 = z2 + 0.5;
    double changeX = (x2 - x1) / 100.0; // if someone can find a better number based on math than this im all ears. I was just too lazy, 10k should be fine
    double changeY = (y2 - y1) / 100.0;
    double changeZ = (z2 - z1) / 100.0;
    double curX = x1;
    double curY = y1;
    double curZ = z1;
    int blockX;
    int blockY;
    int blockZ;

    for (int counter = 1; counter <= 100; counter++) {
      blockX = (int) Math.floor(curX);
      blockY = (int) Math.floor(curY);
      blockZ = (int) Math.floor(curZ);
      curX += changeX;
      curY += changeY;
      curZ += changeZ;

      if (blockX != (int) Math.floor(curX) || blockY != (int) Math.floor(curY) || blockZ != (int) Math.floor(curZ)) {
        mineX.add(blockX);
        mineY.add(blockY);
        mineZ.add(blockZ);
        mineX.add(blockX);
        mineY.add(blockY + 1);
        mineZ.add(blockZ);

        if (blockX != (int) Math.floor(curX - 0.1)) {
          mineX.add(blockX - 1);
          mineY.add(blockY);
          mineZ.add(blockZ);
          mineX.add(blockX - 1);
          mineY.add(blockY + 1);
          mineZ.add(blockZ);
        }
        if (blockX != (int) Math.floor(curX + 0.1)) {
          mineX.add(blockX + 1);
          mineY.add(blockY);
          mineZ.add(blockZ);
          mineX.add(blockX + 1);
          mineY.add(blockY + 1);
          mineZ.add(blockZ);
        }
        if (blockY != (int) Math.floor(curY - 0.1)) {
          mineX.add(blockX);
          mineY.add(blockY - 1);
          mineZ.add(blockZ);
          mineX.add(blockX);
          mineY.add(blockY);
          mineZ.add(blockZ);
        }
        if (blockY != (int) Math.floor(curY + 0.1)) {
          mineX.add(blockX);
          mineY.add(blockY + 1);
          mineZ.add(blockZ);
          mineX.add(blockX);
          mineY.add(blockY + 2);
          mineZ.add(blockZ);
        }
        if (blockZ != (int) Math.floor(curZ - 0.1)) {
          mineX.add(blockX);
          mineY.add(blockY);
          mineZ.add(blockZ - 1);
          mineX.add(blockX);
          mineY.add(blockY + 1);
          mineZ.add(blockZ - 1);
        }
        if (blockZ != (int) Math.floor(curZ + 0.1)) {
          mineX.add(blockX);
          mineY.add(blockY);
          mineZ.add(blockZ + 1);
          mineX.add(blockX);
          mineY.add(blockY + 1);
          mineZ.add(blockZ + 1);
        }
      }
    }

    List<BlockPos> blocks = interpreterPolars(mineX, mineY, mineZ);
    return removeDupe(blocks);
  }

  public static List<BlockPos> removeDupe(List<BlockPos> blocks) {
    HashSet<BlockPos> alrChecked = new HashSet<>();
    List<BlockPos> newSet = new ArrayList<>();

    for (BlockPos i : blocks) {
      if (!alrChecked.contains(i)) {
        newSet.add(i);
        alrChecked.add(i);
      }
    }

    return newSet;
  }

  public static List<BlockPos> interpreterPolars(List<Integer> mineX, List<Integer> mineY, List<Integer> mineZ) {
    List<BlockPos> returnList = new ArrayList<>();

    if (mineX.size() == mineY.size() && mineY.size() == mineZ.size()) {
      for (int i = 0; i < mineX.size(); i++) {
        returnList.add(new BlockPos(mineX.get(i), mineY.get(i), mineZ.get(i)));
      }
    }

    return returnList;
  }
}
