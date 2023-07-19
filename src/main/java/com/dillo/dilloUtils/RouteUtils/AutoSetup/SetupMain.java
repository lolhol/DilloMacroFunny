package com.dillo.dilloUtils.RouteUtils.AutoSetup;

import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;
import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.*;

import com.dillo.Events.MillisecondEvent;
import com.dillo.Pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.Pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.Pathfinding.baritone.automine.config.MiningType;
import com.dillo.Pathfinding.baritone.automine.config.WalkBaritoneConfig;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain;
import com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SetupMain {

  public static boolean isAutoSetupOn = false;

  public static boolean isTurnedOn = false;
  private static List<BlockPos> neededToMineBlocks = new ArrayList<BlockPos>();

  boolean isFirstDone = false;
  boolean alrNuking = false;
  public static boolean usingBaritone = false;
  NukerMain nuker = new NukerMain();
  AutoMineBaritone autoMineBaritone;

  public void reset() {
    prev = 2;
    isTurnedOn = false;
    isFirstDone = false;
    alrNuking = false;
    usingBaritone = false;
    autoMineBaritone.disableBaritone();
  }

  final List<Block> blocksAllowedToMine = new ArrayList<Block>() {
    {
      add(Blocks.stone);
      add(Blocks.gold_ore);
      add(Blocks.emerald_ore);
      add(Blocks.redstone_ore);
      add(Blocks.iron_ore);
      add(Blocks.coal_ore);
      add(Blocks.stained_glass_pane);
      add(Blocks.stained_glass);
      add(Blocks.air);
    }
  };

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (isTurnedOn) {
      isAutoSetupOn = true;
      if (usingBaritone) return;

      if (!isFirstDone) {
        getVariables();
        isFirstDone = true;
      }

      if (neededToMineBlocks.size() > 0) {
        if (!alrNuking) {
          alrNuking = true;
          startAutoSetupNuker(neededToMineBlocks, true);
        }

        if (nuker.getNukesPerSecond() < 1) {
          pauseNuker();

          BlockPos block = getBaritoneWalkBlock(nuking, ids.mc.thePlayer.getPosition());

          if (block != null) {
            usingBaritone = true;

            autoMineBaritone =
              new AutoMineBaritone(
                new BaritoneConfig(MiningType.DYNAMIC, false, true, false, 200, 8, null, blocksAllowedToMine, 256, 256)
              );
            autoMineBaritone.mineFor(block);
          } else {
            reset();
          }
        }
      }
    } else {
      isAutoSetupOn = false;
    }
  }

  public static void baritoneFailed() {
    for (int i = -1; 1 <= i; i++) {
      for (int j = -1; 0 <= j; j++) {
        for (int k = -1; 1 <= k; k++) {
          BlockPos curBlock = makeNewBlock(i, j, k, ids.mc.thePlayer.getPosition());
          nuking.add(0, curBlock);
        }
      }
    }

    usingBaritone = false;
    unpauseNuker();
  }

  public static void reEnable() {
    usingBaritone = false;
    prev = 2;
    unpauseNuker();
  }

  public void getVariables() {
    GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "AUTOSETUP");
  }

  void startFromBlock(BlockPos block) {
    if (neededToMineBlocks.contains(block)) {
      int position = neededToMineBlocks.indexOf(block);
      List<BlockPos> newArr = new ArrayList<>();

      for (int i = position - 15; i < neededToMineBlocks.size(); i++) {
        if (i < 0) continue;
        newArr.add(neededToMineBlocks.get(i));
      }

      neededToMineBlocks = newArr;
    }
  }

  BlockPos getBaritoneWalkBlock(List<BlockPos> curNukerBlocks, BlockPos lastKnownPos) {
    BlockPos furthestPos = null;
    int highestI = 0;

    for (int i = 0; i < curNukerBlocks.size(); i++) {
      BlockPos cur = curNukerBlocks.get(i);

      if (
        (
          ids.mc.theWorld.getBlockState(makeNewBlock(0, 1, 0, cur)).getBlock() == Blocks.air &&
          ids.mc.theWorld.getBlockState(cur).getBlock() == Blocks.air &&
          ids.mc.theWorld.getBlockState(makeNewBlock(0, -1, 0, cur)).getBlock() != Blocks.air
        ) ||
        (
          ids.mc.theWorld.getBlockState(cur).getBlock() != Blocks.air &&
          ids.mc.theWorld.getBlockState(makeNewBlock(0, 1, 0, cur)).getBlock() == Blocks.air &&
          ids.mc.theWorld.getBlockState(makeNewBlock(0, 2, 0, cur)).getBlock() == Blocks.air
        )
      ) {
        if (i > highestI) {
          highestI = i;
          furthestPos = cur;
        }
      }
    }

    return furthestPos;
  }

  public static void updateVariablesAutoSetup(List<BlockPos> blocksFound) {
    neededToMineBlocks = blocksFound;
  }
}
