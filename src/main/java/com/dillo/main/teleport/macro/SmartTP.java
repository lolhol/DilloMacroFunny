package com.dillo.main.teleport.macro;

import static com.dillo.calls.CurrentState.ARMADILLO;
import static com.dillo.calls.CurrentState.SMARTTP;
import static com.dillo.config.config.smartTpDepth;
import static com.dillo.config.config.smartTpRange;
import static com.dillo.main.teleport.macro.TeleportToNextBlock.attemptedToSmartTP;
import static com.dillo.utils.BlockUtils.makeNewBlock;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.config.config;
import com.dillo.main.teleport.utils.TeleportToBlock;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class SmartTP {

  private static final List<BlockPos> allKnownPillars = new ArrayList<>();
  public static HashSet<BlockPos> smartTpBlocks = new HashSet<>();
  public static HashMap<BlockPos, BlockPos> allTpLocations = new HashMap<>();
  private static BlockPos nextBlock = null;
  private static boolean triedAPull = false;
  private static boolean override = false;

  public static void smartTP(BlockPos finalBlock, boolean reTpOnFail) {
    smartTpBlocks.add(finalBlock);

    new Thread(() -> {
      BlockPos block = ids.mc.thePlayer.getPosition();
      SmartTp smartPositions = new SmartTp(null, null);

      if (override) {
        smartPositions = getSmartBlockScanning(block, finalBlock);
      } else {
        triedAPull = true;

        if (allTpLocations.containsKey(finalBlock)) {
          if (isCanTp(allTpLocations.get(finalBlock))) {
            smartPositions = new SmartTp(allTpLocations.get(finalBlock), finalBlock);
          } else {
            smartPositions = null;
          }
        } else {
          smartPositions = null;
        }
      }

      if (smartPositions != null && smartPositions.block1 != null && smartPositions.block2 != null) {
        nextBlock = smartPositions.block2;

        if (!allTpLocations.containsKey(finalBlock)) {
          allTpLocations.put(finalBlock, smartPositions.block1);
        }
        override = false;

        TeleportToBlock.teleportToBlock(smartPositions.block1, config.tpHeadMoveSpeed, 0, SMARTTP);
      } else {
        if (triedAPull && !override) {
          override = true;
          smartTP(finalBlock, reTpOnFail);
        } else {
          triedAPull = false;
          override = false;
          SendChat.chat(prefix.prefix + "Found no teleport locations using smart tp!");
          attemptedToSmartTP = true;
          TeleportToNextBlock.teleportToNextBlock();
        }
      }
    })
      .start();
  }

  public static SmartTp getSmartBlockScanning(BlockPos reference, BlockPos finalBlock) {
    if (allKnownPillars.size() > 0) {
      List<BlockPos> inRangeBlocks = getAllInRangeBlocks(allKnownPillars);
      SmartTp returnTp = getTpableBlocks(inRangeBlocks, finalBlock);

      if (returnTp != null) return returnTp;
    }

    List<BlockPos> blocks = getPilas(reference);
    return getTpableBlocks(blocks, finalBlock);
  }

  public static SmartTp getTpableBlocks(List<BlockPos> blocks, BlockPos finalBlock) {
    for (BlockPos blockPos : blocks) {
      Vec3 blockTp = adjustLook(
        ids.mc.thePlayer.getPositionVector(),
        blockPos,
        new net.minecraft.block.Block[] { Blocks.air },
        false
      );

      if (blockTp != null) {
        Vec3 finalTp = adjustLook(
          new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5),
          finalBlock,
          new net.minecraft.block.Block[] { Blocks.air },
          false
        );

        if (finalTp != null) {
          return new SmartTp(blockPos, finalBlock);
        }
      }
    }

    return null;
  }

  public static boolean isCanTp(BlockPos block) {
    return (
      adjustLook(ids.mc.thePlayer.getPositionVector(), block, new net.minecraft.block.Block[] { Blocks.air }, false) !=
      null
    );
  }

  public static List<BlockPos> getPilas(BlockPos reference) {
    long start = System.currentTimeMillis();
    List<BlockPos> blocks = new ArrayList<>();
    int total = 0;

    for (int y = -smartTpDepth; y <= smartTpDepth; y++) {
      for (int x = -smartTpRange; x <= smartTpRange; x++) {
        for (int z = -smartTpRange; z <= smartTpRange; z++) {
          BlockPos newBlock = makeNewBlock(x, y, z, reference);

          total++;

          if (ids.mc.theWorld.getBlockState(newBlock).getBlock() == Blocks.cobblestone) {
            Block blockUnderThree = ids.mc.theWorld.getBlockState(makeNewBlock(0, -3, 0, newBlock)).getBlock();
            if (blockUnderThree != Blocks.cobblestone) continue;
            Block blockAbove1 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 1, 0, newBlock)).getBlock();
            if (blockAbove1 != Blocks.air) continue;
            Block blockAbove2 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 2, 0, newBlock)).getBlock();
            if (blockAbove2 != Blocks.air) continue;
            Block blockUnderOne = ids.mc.theWorld.getBlockState(makeNewBlock(0, -1, 0, newBlock)).getBlock();
            if (blockUnderOne != Blocks.cobblestone) continue;
            Block blockUnderTwo = ids.mc.theWorld.getBlockState(makeNewBlock(0, -2, 0, newBlock)).getBlock();
            if (blockUnderTwo == Blocks.cobblestone) {
              if (!allKnownPillars.contains(newBlock)) allKnownPillars.add(newBlock);

              blocks.add(newBlock);
            }
          }
        }
      }
    }

    blocks.sort((BlockPos block1, BlockPos block2) -> {
      return DistanceFromTo.distanceFromTo(block1, reference) < DistanceFromTo.distanceFromTo(block2, reference)
        ? -1
        : 1;
    });

    if (config.debugText) {
      SendChat.chat(String.valueOf(System.currentTimeMillis() - start));
      SendChat.chat(
        "Scanned: " +
        total +
        " with avg time per block -> " +
        (double) (System.currentTimeMillis() - start) /
        total +
        "."
      );
    }

    return blocks;
  }

  public static List<BlockPos> getAllInRangeBlocks(List<BlockPos> blocks) {
    List<BlockPos> returnList = new ArrayList<>();
    double neededDist = Math.sqrt(
      smartTpRange * smartTpRange + smartTpDepth * smartTpDepth + smartTpRange * smartTpRange
    );

    for (BlockPos block : blocks) {
      double distance = DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition().add(-1, 0, -1));

      if (distance < neededDist) {
        returnList.add(block);
      }
    }

    return returnList;
  }

  public static void TPToNext() {
    TeleportToBlock.teleportToBlock(nextBlock, config.tpHeadMoveSpeed, 0, ARMADILLO);
  }

  @Getter
  @AllArgsConstructor
  public static class SmartTp {

    public BlockPos block1;
    public BlockPos block2;
  }
}
