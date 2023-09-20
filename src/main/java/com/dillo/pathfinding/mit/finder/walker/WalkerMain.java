package com.dillo.pathfinding.mit.finder.walker;

import com.dillo.events.PlayerMoveEvent;
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
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.random.ThreadUtils;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import com.dillo.utils.renderUtils.renderModules.RenderPoints;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.dillo.armadillomacro.walker;
import static com.dillo.main.utils.looks.LookAt.updateServerLook;
import static com.dillo.pathfinding.mit.finder.walker.WalkerMain.BlockWalkerState.WAITING;
import static com.dillo.pathfinding.mit.finder.walker.WalkerMain.BlockWalkerState.WALKING;

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
  boolean isTeleport = false;
  boolean isTped = false;
  boolean isStartLooking = false;

  PathFinderConfig config = null;
  BlockPos endBlock = null;
  TpState tpState = null;

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
    boolean shift,
    boolean isTeleport
  ) {
    this.isTped = false;
    this.isTeleport = isTeleport;
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
    KeybindHandler.updateKeys(true, false, false, false, false, false, true, false);
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

    if (DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), this.curBlock) > 10 && isTeleport && !isTped) {
      this.state = BlockWalkerState.TELEPORTING;
      this.tpState = TpState.SWITCHING;
    }

    switch (state) {
      case TELEPORTING:
        this.state = BlockWalkerState.WAITINGV2;
        KeybindHandler.updateKeys(false, false, false, false, false, false, false, false);
        this.isTped = true;

        new Thread(() -> {
          ThreadUtils.sleepThread(50);
          int slot = GetSBItems.getAOTVSlot();
          SwapToSlot.swapToSlot(slot);

          ThreadUtils.sleepThread(50);

          LookAt.serverSmoothLook(LookAt.getRotation(this.curBlock), 50);

          ThreadUtils.sleepThread(50);

          ThreadUtils.sleepThread(50);

          ids.mc.thePlayer.sendQueue.addToSendQueue(
            new C08PacketPlayerBlockPlacement(
              new BlockPos(-1, -1, -1),
              255,
              ids.mc.thePlayer.inventory.getStackInSlot(slot),
              0,
              0,
              0
            )
          );

          this.state = WALKING;
        })
          .start();

        return;
      case WALKING:
        isStartLooking = false;
        if (
          DistanceFromTo.distanceFromToXZ(ids.mc.thePlayer.getPosition(), this.endBlock) < 3 &&
          ids.mc.thePlayer.posY - 1 == endBlock.getY() &&
          shiftWhenClosedToEnd
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
            BlockUtils.getCenteredBlock(this.curBlock)
          ) <
          0.7
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
          if (LookAt.done) {
            LookAt.smoothLook(rotation, 150L);
          }

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

        if (nextBlock != null) {
          LookAt.Rotation rot = LookAt.getRotation(nextBlock);
          rot.pitch = 0.0F;
          LookAt.smoothLook(rot, 200);
        }

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

        break;
      case FINISH_TP:
        //blocks.get();

        break;
      case WAITINGV2:
        break;
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
    FINISH_TP,
    WAITINGV2,
    DONE_ROTATION,
    NEXT_BLOCK,
    TELEPORTING,
  }

  enum TpState {
    SWITCHING,
    CLICK,
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isStartLooking) return;
    updateServerLook();
  }
}
