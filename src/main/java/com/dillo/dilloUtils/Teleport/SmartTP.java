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
    SmartTp smartPositions = new SmartTp(null, null);

    for (int j = -10; j <= 10; j++) {
      for (int i = -64; i <= 64; i++) {
        for (int k = -64; k <= 64; k++) {
          BlockPos newBlock = makeNewBlock(i, j, k, block);

          Block blockAbove1 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 1, 0, newBlock)).getBlock();
          Block blockAbove2 = ids.mc.theWorld.getBlockState(makeNewBlock(0, 2, 0, newBlock)).getBlock();

          if (
            ids.mc.theWorld.getBlockState(newBlock).getBlock() == Blocks.cobblestone &&
            blockAbove1 == Blocks.air &&
            blockAbove2 == Blocks.air
          ) {
            blocks.add(newBlock);
          }
        }
      }
    }

    for (BlockPos blockPos : blocks) {
      Vec3 blockTp = adjustLook(
        ids.mc.thePlayer.getPosition(),
        blockPos,
        new net.minecraft.block.Block[] { Blocks.air },
        false
      );

      if (blockTp != null) {
        Vec3 finalTp = adjustLook(
          makeNewBlock(0, 1, 0, blockPos),
          finalBlock,
          new net.minecraft.block.Block[] { Blocks.air },
          false
        );

        if (finalTp != null) {
          smartPositions.block1 = blockPos;
          smartPositions.block2 = finalBlock;
        }
      }
    }

    if (smartPositions.block1 != null && smartPositions.block2 != null) {
      nextBlock = smartPositions.block2;
      TeleportToBlock.teleportToBlock(smartPositions.block1, config.tpHeadMoveSpeed, 0, "SMARTTP");
    } else {
      SendChat.chat(prefix.prefix + "Found no teleport locations using smart tp!");
    }
  }

  public static void TPToNext() {
    TeleportToBlock.teleportToBlock(nextBlock, config.tpHeadMoveSpeed, 0, null);
  }

  @Getter
  @AllArgsConstructor
  public static class SmartTp {

    public BlockPos block1 = null;
    public BlockPos block2 = null;

    public void SmartTp() {
      block1 = null;
      block2 = null;
    }
  }
}
