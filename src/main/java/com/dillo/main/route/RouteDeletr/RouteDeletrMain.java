package com.dillo.main.route.RouteDeletr;

import static com.dillo.main.utils.looks.LookAt.updateServerLook;
import static com.dillo.utils.BlockUtils.makeNewBlock;
import static com.dillo.utils.GetSBItems.*;
import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.events.MillisecondEvent;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.packets.sendStart;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RouteDeletrMain {

  boolean isEnabled = false;
  RouteDeletrConfig curConf;
  List<BlockPos> broken = new ArrayList<>();
  int currTicks = 0;
  int resetServerBroken = 0;
  boolean isServerLook = false;
  List<BlockPos> brokenWithBoom = new ArrayList<>();

  public long timePoint_nextNuke = System.currentTimeMillis();

  public void startStop(RouteDeletrConfig config, boolean isStart) {
    if (isStart) {
      curConf = config;
      isEnabled = true;
    } else {
      reset();
    }
  }

  void reset() {
    isEnabled = false;
    curConf = null;
    broken.clear();
    currTicks = 0;
    resetServerBroken = 0;
    isServerLook = false;
    brokenWithBoom.clear();
    timePoint_nextNuke = System.currentTimeMillis();
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (isEnabled && resetServerBroken < 600) {
      brokenWithBoom.clear();
    } else {
      resetServerBroken++;
    }

    if (!isEnabled || currTicks <= curConf.totalCheckTicks) {
      currTicks++;
      return;
    }
    broken.clear();
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isEnabled || !isServerLook) return;
    updateServerLook();
  }

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (!isEnabled || System.currentTimeMillis() < timePoint_nextNuke) return;
    timePoint_nextNuke = System.currentTimeMillis() + (1000 / curConf.nukerBPS);

    List<BlockPos> cobbleBlocksAround = getCobbleBlocks(ids.mc.thePlayer.getPosition(), curConf.range);

    BlockPos currentBlock;
    if (curConf.onlyOnRoute) {
      currentBlock = getBlockOnRoute(cobbleBlocksAround, currentRoute.currentRoute);
    } else {
      currentBlock = cobbleBlocksAround.size() > 0 ? cobbleBlocksAround.get(0) : null;
    }

    if (
      currentBlock == null ||
      DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), currentBlock) > curConf.range ||
      broken.contains(currentBlock)
    ) {
      return;
    }

    int drillSlot = getDrillSlot();

    if (curConf.isBreakCobble && !isServerLook && drillSlot != -1) {
      if (isEquippedDrill()) {
        sendStart.sendStartPacket(currentBlock, EnumFacing.fromAngle(ids.mc.thePlayer.rotationYaw));
        broken.add(currentBlock);
      } else {
        SwapToSlot.swapToSlot(drillSlot);
      }
    }

    if (
      curConf.boomBox &&
      ids.mc.theWorld.getBlockState(currentBlock).getBlock() != Blocks.air &&
      !brokenWithBoom.contains(currentBlock)
    ) {
      if (!isServerLook) {
        LookAt.serverSmoothLook(LookAt.getRotation(currentBlock), curConf.msServerLookTime);
        isServerLook = true;

        int boomSlot = getBoomSlot();

        if (boomSlot == -1) {
          isServerLook = false;
          return;
        }

        SwapToSlot.swapToSlot(getBoomSlot());

        new Thread(() -> {
          try {
            Thread.sleep(curConf.msServerLookTime + 200);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }

          rightClick();
          reset();

          brokenWithBoom.add(currentBlock);

          isServerLook = false;
        })
          .start();
      }
    }
  }

  public boolean isEquippedDrill() {
    String name = getSbItemName(ids.mc.thePlayer.getCurrentEquippedItem()).toLowerCase();
    return name.contains("drill") || name.contains("pickaxe") || name.contains("gauntlet");
  }

  public BlockPos getBlockOnRoute(List<BlockPos> blocks, List<BlockPos> route) {
    for (BlockPos block : blocks) {
      if (isOnRoute(route, block)) {
        return block;
      }
    }

    return null;
  }

  public boolean isOnRoute(List<BlockPos> routeBlocks, BlockPos block) {
    return routeBlocks.contains(block);
  }

  public List<BlockPos> getCobbleBlocks(BlockPos around, float radius) {
    List<BlockPos> returnBlocks = new ArrayList<>();

    Iterable<BlockPos> blocks = BlockPos.getAllInBox(
      makeNewBlock(-radius, -1, -radius, around),
      makeNewBlock(radius, 1, radius, around)
    );

    for (BlockPos block : blocks) {
      if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.cobblestone) {
        returnBlocks.add(block);
      }
    }

    return returnBlocks;
  }
}
