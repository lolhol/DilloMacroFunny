package com.dillo.pathfinding.mit.finder.mods.breaklogs;

import static com.dillo.main.utils.looks.LookAt.updateServerLook;

import com.dillo.events.DonePathEvent;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.mit.finder.walker.WalkerMain;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.packets.getBlockEnum;
import com.dillo.utils.previous.packets.sendStart;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LogBreakerV2 {

  public boolean isOn = false;
  boolean isOnLook = false;
  public Utils utils = new Utils();
  List<BlockPos> broken = new ArrayList<>();
  boolean isLooked = false;
  WalkerMain walkerMain = new WalkerMain();
  boolean isRan = false;
  int c = 0;
  int bc = 0;
  BlockPos endBlock = null;

  ////////////////////////////////////////

  List<BlockPos> blocks = new ArrayList<>();
  BlockPos curBlock = null;
  int curPos = 0;
  PathFinderConfig config = null;

  public LogBreakerV2() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  public void run(List<BlockPos> blocks, boolean state, PathFinderConfig config) {
    this.isOn = state;
    this.blocks = blocks;
    this.curBlock = blocks.get(curPos);
    this.curPos++;
    this.isRan = false;
    this.config = config;

    this.endBlock = blocks.get(blocks.size() - 1);
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isOn) return;

    if (bc >= 40) {
      broken.clear();
      bc = 0;
    } else {
      bc++;
    }

    float reach = ids.mc.playerController.getBlockReachDistance();
    List<BlockPos> blocksAround = utils.getSurroundingLogs(
      reach,
      reach,
      reach,
      BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector()),
      broken
    );

    if (blocksAround.isEmpty()) {
      isOnLook = false;
      if (!isLooked) {
        isLooked = true;
        LookAt.smoothLook(new LookAt.Rotation(ids.mc.thePlayer.cameraPitch, 0.0F), 200);
      }

      if (!isRan) {
        utils.startPathFinder(this.curBlock);
        isRan = true;
      }

      if (utils.isPathFinderDone()) {
        walkerMain.walkOnPath(utils.retrieveFoundPath(), true, this.endBlock, config, true, false);
        walkerMain.setState(false);
      }

      return;
    } else {
      walkerMain.setState(true);
    }

    isLooked = true;

    if (c < 4) {
      c++;
      return;
    }

    c = 0;

    BlockPos closest = utils.getClosest(blocksAround);

    if (
      DistanceFromTo.distanceFromTo(closest, ids.mc.thePlayer.getPosition()) >
      ids.mc.playerController.getBlockReachDistance()
    ) return;

    isOnLook = true;
    LookAt.smoothLook(LookAt.getRotation(closest), 300);
    broken.add(closest);

    sendStart.sendStartPacket(closest, getBlockEnum.getEnum(closest));
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isOnLook) return;
    updateServerLook();
  }

  @SubscribeEvent
  public void onDone(DonePathEvent pre) {
    if (!isOn) return;
    moveUp();
    SendChat.chat("!!!");
    isRan = false;
  }

  void moveUp() {
    if (curPos + 1 >= blocks.size()) {
      curPos = 0;
    } else {
      curPos++;
    }

    this.curBlock = blocks.get(curPos);
  }
}
