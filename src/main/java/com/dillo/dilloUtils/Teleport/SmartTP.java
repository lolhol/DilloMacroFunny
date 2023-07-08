package com.dillo.dilloUtils.Teleport;

import static com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos.getUnobstructedPos;
import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class SmartTP {

  private static BlockPos nextBlock = null;

  public static void smartTP(BlockPos finalBlock) {
    BlockPos block = ids.mc.thePlayer.getPosition();
    List<BlockPos> blocks = new ArrayList<>();

    for (int j = -10; j <= 10; j++) {
      for (int i = -10; i <= 10; i++) {
        for (int k = -10; k <= 10; k++) {
          BlockPos newBlock = makeNewBlock(i, j, k, block);

          Block blockAbove1 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 1, 0, newBlock)).getBlock();
          Block blockAbove2 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 2, 0, newBlock)).getBlock();

          if (
            ids.mc.theWorld.getBlockState(newBlock).getBlock() != Blocks.air &&
            blockAbove1 == Blocks.air &&
            blockAbove2 == Blocks.air
          ) {
            blocks.add(newBlock);
          }
        }
      }
    }

    new Thread(() -> {
      SmartTp bestSmartTp = new SmartTp(100000, null, null);

      for (BlockPos blockInList : blocks) {
        SmartTp newSmart = new SmartTp(0, null, null);
        Vec3 nextBlockPos = getUnobstructedPos(blockInList);

        if (nextBlockPos == null) {
          nextBlockPos =
            adjustLook(
              ids.mc.thePlayer.getPosition(),
              blockInList,
              new net.minecraft.block.Block[] { Blocks.air },
              false
            );

          if (nextBlockPos != null) {
            newSmart.points = (float) DistanceFromTo.distanceFromTo(blockInList, finalBlock);
            newSmart.block1 = blockInList;

            Vec3 finalBlockTP = adjustLook(
              makeNewBlock(0.5, 2.6, 0.5, blockInList),
              finalBlock,
              new net.minecraft.block.Block[] { Blocks.air },
              false
            );

            if (finalBlockTP != null) {
              newSmart.block2 = finalBlock;
            } else {
              continue;
            }
          } else {
            continue;
          }
        } else {
          newSmart.points = (float) DistanceFromTo.distanceFromTo(blockInList, finalBlock);
          newSmart.block1 = blockInList;

          Vec3 finalBlockTP = adjustLook(
            makeNewBlock(0.5, 2.6, 0.5, blockInList),
            finalBlock,
            new net.minecraft.block.Block[] { Blocks.air },
            false
          );

          if (finalBlockTP != null) {
            newSmart.block2 = finalBlock;
          } else {
            continue;
          }
        }

        if (newSmart.points > 5 && newSmart.block1 != null) SendChat.chat(String.valueOf(newSmart.points));
        if (newSmart.points < bestSmartTp.points && newSmart.block2 != null) {
          bestSmartTp.block1 = newSmart.block1;
          bestSmartTp.block2 = newSmart.block2;
          bestSmartTp.points = newSmart.points;
        }
      }

      if (bestSmartTp.block1 != null && bestSmartTp.block2 != null) {
        TeleportToBlock.teleportToBlock(bestSmartTp.block1, config.tpHeadMoveSpeed, 0, "SMARTTP");

        nextBlock = bestSmartTp.block2;
      } else {
        ArmadilloStates.offlineState = "offline";
        SendChat.chat(prefix.prefix + "Didnt find a good path :/.");
      }
    })
      .start();
  }

  public static void TPToNext() {
    TeleportToBlock.teleportToBlock(nextBlock, config.tpHeadMoveSpeed, 0, null);
  }

  @Getter
  @AllArgsConstructor
  public static class SmartTp {

    public float points = 0;
    public BlockPos block1 = null;
    public BlockPos block2 = null;

    public void SmartTp() {
      points = 0;
      block1 = null;
      block2 = null;
    }
  }
}
