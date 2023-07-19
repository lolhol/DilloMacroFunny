package com.dillo.dilloUtils.RouteUtils.AutoSetup;

import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.startAutoSetupNuker;

import com.dillo.Events.MillisecondEvent;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SetupMain {

  public static boolean isTurnedOn = false;
  private static List<BlockPos> neededToMineBlocks = new ArrayList<BlockPos>();

  boolean isFirstDone = false;
  boolean alrNuking = false;
  boolean usingBaritone = false;

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (isTurnedOn) {
      if (usingBaritone) return;

      if (!isFirstDone) {
        getVariables();
        isFirstDone = true;
      }

      if (!alrNuking) startAutoSetupNuker(neededToMineBlocks, true);
    }
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

  public static void updateVariablesAutoSetup(List<BlockPos> blocksFound) {
    neededToMineBlocks = blocksFound;
  }
}
