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

  boolean start = false;
  boolean isStart = false;
  int init = 0;
  boolean isStartLooking = false;
  int ticksUntilCheck = 0;
  int count = 5;
  int fCount = 0;

  BlockPos closestBlock = null;

  List<BlockPos> logList = new ArrayList<>();
  List<BlockPos> finishedList = new ArrayList<>();

  Utils utils = new Utils();

  public void init(boolean isStart) {
    this.logList = utils.getSurroundingLogs(3, 3, 3, ids.mc.thePlayer.getPosition());

    closestBlock = utils.getClosest(logList);

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
    this.fCount = 0;
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

    if (count < 4) {
      count++;
      return;
    } else {
      count = 0;
    }

    //checkLogs();

    ticksUntilCheck++;

    //SendChat.chat(ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem).getDisplayName());

    if (start) {
      if (fCount > 1) {
        reset();
        this.isStart = false;
        start = false;
        return;
      }

      fCount++;
    }

    if (
      ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem).getItem() != Items.golden_axe
    ) {
      SwapToSlot.swapToItem(Items.golden_axe);
      return;
    }

    if (
      DistanceFromTo.distanceFromTo(closestBlock, ids.mc.thePlayer.getPosition()) >
      ids.mc.playerController.getBlockReachDistance()
    ) {
      return;
    }

    RenderOneBlockMod.renderOneBlock(BlockUtils.fromBlockPosToVec3(closestBlock), true);

    if (BlockUtils.getBlock(closestBlock) != Blocks.log) {
      return;
    }

    isStartLooking = true;
    LookAt.serverSmoothLook(LookAt.getRotation(closestBlock), 150);

    sendStart.sendStartPacket(closestBlock, getBlockEnum.getEnum(closestBlock));
    start = true;
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isStartLooking) return;
    updateServerLook();
  }
}
