package com.dillo.pathfinding.mit.finder.mods.breaklogs;

import com.dillo.events.MillisecondEvent;
import com.dillo.pathfinding.mit.finder.walker.event.DoneWalking;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LogBreaker {

  boolean breakerState;
  List<BlockPos> blocks = new ArrayList<>();
  BlockPos curBlock = null;
  Utils utils = new Utils();
  State curState = null;
  boolean isDoneWalking = false;
  LogNuker nuker = new LogNuker();
  int add = 0;
  boolean isPathEngaged = false;
  int time = 0;

  public void logBreakerMain(boolean breakerState, List<BlockPos> coordinates) {
    this.breakerState = breakerState;

    if (breakerState) {
      this.blocks = coordinates;
      this.curBlock = utils.getClosest(coordinates);
      add = coordinates.indexOf(curBlock);
      curState = State.INIT;
    } else {
      reset();
    }
  }

  @SubscribeEvent
  public void onMs(MillisecondEvent event) {
    if (!breakerState || curState == null) {
      if (curState == State.BREAKING || curState == State.WAITINGFORDONE) {
        nuker.reset();
      }

      return;
    }

    switch (curState) {
      case INIT:
        if (!isPathEngaged) utils.startPathFinder(this.curBlock);

        this.curState = State.FINDINGPATH;
        break;
      case FINDINGPATH:
        if (!utils.isPathFinderDone()) {
          return;
        }

        this.curState = State.WALKING;
        List<BlockPos> path = utils.retrieveFoundPath();
        path.forEach(a -> {
          RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(a), true);
        });

        //walker.walkOnPath(path, true, this.curBlock, utils.config, true, false);
        break;
      case WALKING:
        if (this.isDoneWalking) {
          this.isDoneWalking = false;
          this.curState = State.BREAKING;
        }

        break;
      case BREAKING:
        BlockPos closest = utils.getClosest(utils.getSurroundingLogs(3, 3, 3, ids.mc.thePlayer.getPosition()));
        //KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
        this.curState = State.WAITINGFORDONE;

        if (add >= blocks.size() - 1) {
          add = 0;
        } else {
          add++;
        }

        try {
          this.curBlock = blocks.get(add);
        } catch (IndexOutOfBoundsException e) {
          SendChat.chat(String.valueOf(add) + "!!!!!!!!!!!!");
        }

        utils.startPathFinder(this.curBlock);
        isPathEngaged = true;

        //LookAt.smoothLook(LookAt.getRotation(curBlock), 200);
        nuker.init(true);
        break;
      case WAITINGFORDONE:
        if (nuker.isDone()) {
          RenderOneBlockMod.renderOneBlock(null, false);

          this.curState = State.INIT;
        }

        break;
    }
  }

  void reset() {
    this.breakerState = false;
    this.curBlock = null;
    isPathEngaged = false;
  }

  enum State {
    WALKING,
    FINDINGPATH,
    BREAKING,
    WAITINGFORDONE,
    INIT,
  }

  @SubscribeEvent
  public void onDoneWalk(DoneWalking event) {
    isDoneWalking = true;
  }
}
