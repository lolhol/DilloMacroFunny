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
import com.dillo.pathfinding.mit.finder.walker.event.DoneWalking;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderPoints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.init.Blocks;
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
  int countV1 = 0;
  Vec3 beforePlayerPos = null;
  int notMovingTicks = 0;
  boolean movingC = false;
  boolean shifting = false;

  PathFinderConfig config = null;
  BlockPos endBlock = null;

  boolean shiftWhenClosedToEnd = false;

  public void walkOnPath(List<BlockPos> blocks, boolean walkerState, BlockPos endBlock, PathFinderConfig config) {
    this.config = config;
    this.endBlock = endBlock;
    //sprint = false;

    if (walkerState) {
      KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
      this.isWalk = true;
      this.curBlock = blocks.get(0);
      blocks.remove(0);
      this.nextBlock = blocks.get(0);
      this.blocks = blocks;

      /*LookAt.Rotation rot = LookAt.getRotation(curBlock);
      rot.pitch = 0.0F;
      LookAt.smoothLook(rot, 200);*/

      this.state = WALKING;
    } else {
      this.reset();
    }
  }

  public void walkOnPath(
    List<BlockPos> blocks,
    boolean walkerState,
    BlockPos endBlock,
    PathFinderConfig config,
    boolean shift
  ) {
    this.config = config;
    this.endBlock = endBlock;
    this.shiftWhenClosedToEnd = shift;
    //sprint = false;

    if (walkerState) {
      KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
      this.isWalk = true;
      this.curBlock = blocks.get(0);
      blocks.remove(0);
      this.nextBlock = blocks.get(0);
      this.blocks = blocks;

      /*LookAt.Rotation rot = LookAt.getRotation(curBlock);
      rot.pitch = 0.0F;
      LookAt.smoothLook(rot, 200);*/

      this.state = WALKING;
    } else {
      this.reset();
    }
  }

  public void reset() {
    Utils.isCloseToEnd = false;
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
      long start = System.currentTimeMillis();
      this.config.startingBlock = ids.mc.thePlayer.getPosition();

      List<BlockNodeClass> route = pathFinder.run(this.config);

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

  public boolean isBlockToFall(BlockPos block) {
    return block.getY() < ids.mc.thePlayer.posY;
  }

  boolean isOnGround() {
    return BlockUtils.getBlock(BlockUtils.makeNewBlock(0, -1, 0, ids.mc.thePlayer.getPosition())) != Blocks.air;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    //RenderOneBlockMod.renderOneBlock(BlockUtils.fromBlockPosToVec3(ids.mc.thePlayer.getPosition()), true);

    if (!isWalk || state == null) return;

    if (state != WAITING) {
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
        return;
      }
    }

    switch (state) {
      case WALKING:
        if (
          DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), this.endBlock) < 3 &&
          shiftWhenClosedToEnd &&
          this.curBlock.getY() == ids.mc.thePlayer.posY
        ) {
          Utils.isCloseToEnd = true;
          shifting = true;
          KeybindHandler.updateKeys(true, false, false, false, false, false, shifting, Utils.isCloseToJumpBlock());
        } else {
          shifting = false;
        }

        if (
          DistanceFromTo.distanceFromToXZ(
            BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector()),
            this.curBlock
          ) <
          2
        ) {
          this.state = BlockWalkerState.NEXT_BLOCK;
          return;
        }

        if (isOnGround()) {
          if (Utils.isCloseToNextBlock(this.curBlock)) {
            this.state = BlockWalkerState.NEXT_BLOCK;
            break;
          }

          LookAt.Rotation rotation = LookAt.getRotation(curBlock);
          rotation.pitch = 0.0F;
          LookAt.smoothLook(rotation, 20);

          movingC = false;
        }

        KeybindHandler.updateKeys(true, false, false, false, false, false, shifting, Utils.isCloseToJumpBlock());
        break;
      case NEXT_BLOCK:
        if (isDoneWithPath) {
          reset();
          MinecraftForge.EVENT_BUS.post(new DoneWalking());
          return;
        }

        this.curBlock = this.nextBlock;
        if (!blocks.isEmpty()) {
          this.nextBlock = this.blocks.get(0);
          this.blocks.remove(0);

          if (blocks.isEmpty()) {
            //sprint = true;
          }
          //SendChat.chat(String.valueOf(blocks.size()));
        } else {
          this.isDoneWithPath = true;
        }

        LookAt.Rotation rot = LookAt.getRotation(curBlock);
        rot.pitch = 0.0F;

        LookAt.smoothLook(rot, 200);
        this.state = WAITING;
        break;
      case DONE_ROTATION:
        this.state = WALKING;
        break;
      case FALLING:
        if (ids.mc.thePlayer.posY == this.curBlock.getY()) {
          //SendChat.chat(String.valueOf(ids.mc.thePlayer.posY == this.curBlock.getY()));

          if (countV1 == 10) {
            this.state = BlockWalkerState.NEXT_BLOCK;
            countV1 = 0;
          }

          countV1++;
        }

        break;
      case WAITING:
        countV1++;
        if (countV1 == 4) {
          countV1 = 0;
          this.state = BlockWalkerState.DONE_ROTATION;
        }
    }
  }

  @SubscribeEvent
  public void onDoneRotate(DoneRotating event) {
    if (!isWalk || state == null) return;

    if (!event.isDoneRotate) return;
    MinecraftForge.EVENT_BUS.post(new DoneRotating(false));

    if (!movingC) return;

    this.state = BlockWalkerState.DONE_ROTATION;

    movingC = false;
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
