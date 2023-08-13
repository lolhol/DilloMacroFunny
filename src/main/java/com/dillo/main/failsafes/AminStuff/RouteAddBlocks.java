package com.dillo.main.failsafes.AminStuff;

import static com.dillo.main.route.Utils.GetBlocksForNuker.polarGetBlocks;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RouteAddBlocks {

  int count = 0;
  List<BlockPos> last = new ArrayList<>();

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!config.blockAddRouteFail || !ArmadilloStates.isOnline()) return;

    if (count < 1000) {
      count++;
      return;
    }

    count = 0;

    new Thread(() -> {
      if (isAddedPlaceBlocks()) {
        ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        ArmadilloStates.currentState = null;
        SendChat.chat(prefix.prefix + "Block add Failsafe has triggered!");
      }
    })
      .start();
  }

  private boolean isAddedPlaceBlocks() {
    if (last.isEmpty()) {
      last = getBlocks(currentRoute.currentRoute);
      return false;
    }

    List<BlockPos> blocksNew = getBlocks(currentRoute.currentRoute);
    int dif = diffAmount(last, blocksNew);
    last = blocksNew;

    return dif >= config.amountOBlocksAdded;
  }

  private int diffAmount(List<BlockPos> prev, List<BlockPos> newList) {
    return Math.abs(prev.size() - newList.size());
  }

  public List<BlockPos> getBlocks(List<BlockPos> blocksOnRoute) {
    List<BlockPos> blocks = new ArrayList<>();

    for (int i = 0; i < blocksOnRoute.size(); i++) {
      int second = i + 1;

      if (i == blocksOnRoute.size() - 1 && blocksOnRoute.size() > 2) {
        second = 0;
      }

      if (second < blocksOnRoute.size()) {
        BlockPos block = blocksOnRoute.get(i);
        BlockPos secondBlock = blocksOnRoute.get(second);

        if (BlockUtils.isBlockLoaded(block) && BlockUtils.isBlockLoaded(secondBlock)) {
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

    return blocks;
  }
}
