package com.dillo.main.route.AutoSetup;

import static com.dillo.commands.baritone.StartAutoSetupWithBaritone.main;
import static com.dillo.main.route.Nuker.NukerMain.*;
import static com.dillo.main.route.Nuker.StartNuker.stopNuker;
import static com.dillo.utils.BlockUtils.makeNewBlock;

import com.dillo.events.MillisecondEvent;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.route.Nuker.NukerMain;
import com.dillo.main.route.Utils.GetBlocksForNuker;
import com.dillo.pathfinding.baritone.automine.AutoMineBaritone;
import com.dillo.pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.pathfinding.baritone.automine.config.MiningType;
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
  List<BlockPos> baritoneFailBlocks = new ArrayList<BlockPos>();
  int baritoneWait = 0;

  public void reset() {
    prev = 2;
    isTurnedOn = false;
    isFirstDone = false;
    alrNuking = false;
    usingBaritone = false;
    if (autoMineBaritone != null) {
      autoMineBaritone.disableBaritone();
    }
    stopNuker();
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

      // Remove this for skyblock
      add(Blocks.dirt);
      add(Blocks.grass);
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

        if (nuker.isDone()) {
          usingBaritone = true;
          pauseNuker();

          BlockPos block = getBaritoneWalkBlock(nuking, ids.mc.thePlayer.getPosition());

          if (block != null) {
            SendChat.chat(String.valueOf(block));

            if (!main.baritoneFailBlocks.contains(block)) {
              autoMineBaritone =
                new AutoMineBaritone(
                  new BaritoneConfig(
                    MiningType.DYNAMIC,
                    false,
                    true,
                    false,
                    200,
                    8,
                    null,
                    blocksAllowedToMine,
                    256,
                    256
                  )
                );
              autoMineBaritone.mineFor(block);
            } else {
              SendChat.chat("RESET");
              usingBaritone = false;
              //reset();
            }
          } else {
            SendChat.chat("RESET!!!");
            reset();
          }
        }
      }
    } else {
      isAutoSetupOn = false;
    }
  }

  public boolean isAutoSetupOnline() {
    return isAutoSetupOn;
  }

  public void addBlockToBaritoneFailList(BlockPos block) {
    usingBaritone = false;
    main.baritoneFailBlocks.add(block);
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

  public void reEnable() {
    usingBaritone = false;
    prev = 100;
    unpauseNuker();
  }

  public void reStart() {
    main.baritoneFailBlocks.clear();
    usingBaritone = false;
    prev = 100;
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
        )
      ) {
        if (i > highestI) {
          highestI = i;

          if (!main.baritoneFailBlocks.contains(cur) && !main.baritoneFailBlocks.contains(makeNewBlock(0, 1, 0, cur))) {
            if (ids.mc.theWorld.getBlockState(cur).getBlock() == Blocks.air) furthestPos = cur; else furthestPos =
              makeNewBlock(0, 1, 0, cur);
          }
        }
      }
    }

    return furthestPos;
  }

  public static void updateVariablesAutoSetup(List<BlockPos> blocksFound) {
    neededToMineBlocks = blocksFound;
  }
}
