package com.dillo.pathfinding.mit.finder.walker;

import static com.dillo.armadillomacro.walker;
import static com.dillo.pathfinding.mit.finder.walker.WalkerMain.BlockWalkerState.WAITING;
import static com.dillo.pathfinding.mit.finder.walker.WalkerMain.BlockWalkerState.WALKING;

import com.dillo.keybinds.KeybindHandler;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.pathfinding.mit.finder.main.AStarPathFinder;
import com.dillo.pathfinding.mit.finder.main.OnPathRenderer;
import com.dillo.pathfinding.mit.finder.utils.BlockNodeClass;
import com.dillo.pathfinding.mit.finder.utils.PathFinderConfig;
import com.dillo.pathfinding.mit.finder.walker.event.DoneRotating;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderPoints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WalkerMain {

  List<BlockPos> blocks = new ArrayList<>();
  public HashSet<BlockPos> walkedOnBlocks = new HashSet<>();
  boolean isWalk = false;
  BlockWalkerState state = null;
  BlockWalkerState preRotateState = null;
  BlockPos curBlock = null;
  BlockPos nextBlock = null;
  boolean isDoneWithPath;
  int failTime = 0;
  Vec3 beforePlayerPos = null;
  int notMovingTicks = 0;

  PathFinderConfig config = null;
  BlockPos endBlock = null;

  public static boolean test = false;

  public void walkOnPath(List<BlockPos> blocks, boolean walkerState, BlockPos endBlock, PathFinderConfig config) {
    this.config = config;
    this.endBlock = endBlock;

    com.dillo.utils.previous.chatUtils.SendChat.chat("!!");

    if (walkerState) {
      KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
      this.isWalk = true;
      this.curBlock = blocks.get(0);
      blocks.remove(0);
      this.nextBlock = blocks.get(0);
      this.blocks = blocks;
      this.state = WALKING;
    } else {
      this.reset();
    }
    //dgjnjdg
  }

  public void reset() {
    this.isWalk = false;
    this.blocks.clear();
    this.walkedOnBlocks.clear();
    this.isDoneWithPath = false;
    KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
  }

  public boolean isNotMoving() {
    if (beforePlayerPos == null) {
      beforePlayerPos = ids.mc.thePlayer.getPositionVector();
      return false;
    }

    if (DistanceFromTo.distanceFromTo(beforePlayerPos, ids.mc.thePlayer.getPositionVector()) < 0.1) {
      beforePlayerPos = ids.mc.thePlayer.getPositionVector();
      return true;
    }

    beforePlayerPos = ids.mc.thePlayer.getPositionVector();
    return false;
  }

  public void restartFinder() {
    RenderPoints.renderPoint(null, 0.2, false);

    AStarPathFinder pathFinder = new AStarPathFinder();
    RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

    new Thread(() -> {
      OnPathRenderer.renderList(null, false);
      OnPathRenderer.renderList(null, false);
      long start = System.currentTimeMillis();

      List<BlockNodeClass> route = pathFinder.AStarPathFinder(this.config);

      if (route == null) {
        com.dillo.utils.previous.chatUtils.SendChat.chat("Didnt find a route.");
        return;
      }

      com.dillo.utils.previous.chatUtils.SendChat.chat(
        "Took " + (System.currentTimeMillis() - start) + "ms. And the route size is " + route.size()
      );

      List<BlockPos> shortSegment = Utils.getShortList(route);
      shortSegment.forEach(a -> {
        RenderMultipleBlocksMod.renderMultipleBlocks(BlockUtils.fromBlockPosToVec3(a), true);
      });

      walker.walkOnPath(shortSegment, true, this.endBlock, this.config);
    })
      .start();
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (test) {
      SendChat.chat(String.valueOf(Utils.isCloseToJumpBlock()));
    }

    if (!isWalk || state == null) return;

    if (notMovingTicks >= 5) {
      notMovingTicks = 0;

      if (isNotMoving()) {
        failTime++;
      } else {
        failTime = 0;
      }
    } else {
      notMovingTicks++;
    }

    if (failTime >= 4) {
      failTime = 0;
      this.reset();
      this.restartFinder();
    }

    switch (state) {
      case WALKING:
        if (Utils.isCloseToNextBlock(this.curBlock)) {
          if (isDoneWithPath) {
            reset();
            SendChat.chat("Done with path!");
            return;
          }

          this.state = BlockWalkerState.NEXT_BLOCK;
          this.curBlock = this.nextBlock;

          if (!blocks.isEmpty()) {
            this.nextBlock = this.blocks.get(0);
            blocks.remove(0);
          } else {
            this.isDoneWithPath = true;
          }

          break;
        }

        LookAt.Rotation rotation = LookAt.getRotation(curBlock);
        rotation.pitch = 0.0F;
        LookAt.smoothLook(rotation, 20);

        KeybindHandler.updateKeys(true, false, false, false, false, false, false, Utils.isCloseToJumpBlock());
      case NEXT_BLOCK:
        LookAt.Rotation rot = LookAt.getRotation(curBlock);
        rot.pitch = 0.0F;

        LookAt.smoothLook(rot, 200);
        this.state = WAITING;
      case DONE_ROTATION:
        this.state = WALKING;
    }
  }

  @SubscribeEvent
  public void onDoneRotate(DoneRotating event) {
    if (!isWalk || state == null) return;

    if (!event.isDoneRotate) return;
    MinecraftForge.EVENT_BUS.post(new DoneRotating(false));
    this.state = BlockWalkerState.DONE_ROTATION;
  }

  enum BlockWalkerState {
    ROTATING,
    WALKING,
    WAITING,
    JUMPING,
    FALLING,
    START_BREAK,
    BREAKING,
    DONE_ROTATION,
    NEXT_BLOCK,
  }
}
