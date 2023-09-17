package com.dillo.pathfinding.mit.finder.mods.breaklogs;

import static com.dillo.main.utils.looks.LookAt.updateServerLook;

import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.packets.getBlockEnum;
import com.dillo.utils.previous.packets.sendStart;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LogNuker {

  boolean isHard = false;
  boolean isStart = false;
  int init = 0;
  boolean isStartLooking = false;
  int ticksUntilCheck = 0;
  int count = 5;

  List<BlockPos> logList = new ArrayList<>();
  List<BlockPos> finishedList = new ArrayList<>();

  Utils utils = new Utils();

  public void init(boolean isStart) {
    this.logList = utils.getSurroundingLogs(3, 3, 3, ids.mc.thePlayer.getPosition());

    int i = 0;
    while (i < logList.size()) {
      if (
        BlockUtils.getBlock(logList.get(i)) != Blocks.log ||
        DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), logList.get(i)) >
        ids.mc.playerController.getBlockReachDistance()
      ) {
        logList.remove(i);
      } else {
        i++;
      }
    }

    init = logList.size();
    finishedList.clear();

    if (logList.isEmpty()) {
      this.isStart = false;
      return;
    }

    this.isStart = isStart;
    MinecraftForge.EVENT_BUS.register(this);
  }

  public void reset() {
    MinecraftForge.EVENT_BUS.unregister(this);
    count = 5;
  }

  public boolean isDone() {
    return !isStart;
  }

  void checkLogs() {
    while (!finishedList.isEmpty()) {
      BlockPos block = finishedList.get(0);

      if (BlockUtils.getBlock(block) != Blocks.air) {
        logList.add(block);
      }

      finishedList.remove(block);
    }
  }

  boolean isDoneNow() {
    return init - logList.size() > 4;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isStart) return;

    if (isDoneNow()) {
      this.isStart = false;
      reset();
      return;
    }

    if (count < 4) {
      count++;
      return;
    } else {
      count = 0;
    }

    //checkLogs();

    ticksUntilCheck++;

    //SendChat.chat(ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem).getDisplayName());

    if (
      ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem).getItem() != Items.golden_axe
    ) {
      SwapToSlot.swapToItem(Items.golden_axe);
      return;
    }

    BlockPos closest = utils.getClosest(logList);
    if (logList.isEmpty() || closest == null) {
      this.isStart = false;
      reset();
      return;
    }

    if (
      DistanceFromTo.distanceFromTo(closest, ids.mc.thePlayer.getPosition()) >
      ids.mc.playerController.getBlockReachDistance()
    ) {
      return;
    }

    /*if (isSendStop) {
      isSendStop = false;
      sendStop.sendStopPacket(closest, getBlockEnum.getEnum(closest));
    }*/

    finishedList.add(closest);
    logList.remove(closest);

    RenderOneBlockMod.renderOneBlock(BlockUtils.fromBlockPosToVec3(closest), true);

    if (BlockUtils.getBlock(closest) != Blocks.log) {
      return;
    }

    isStartLooking = true;
    LookAt.serverSmoothLook(LookAt.getRotation(closest), 50);

    sendStart.sendStartPacket(closest, getBlockEnum.getEnum(closest));
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isStartLooking) return;
    updateServerLook();
  }
}
