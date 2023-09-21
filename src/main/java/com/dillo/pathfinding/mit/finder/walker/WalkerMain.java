package com.dillo.pathfinding.mit.finder.walker;

import com.dillo.keybinds.KeybindHandler;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WalkerMain {

  boolean isStart = false;
  List<BlockPos> shortList = new ArrayList<>();
  BlockPos currentBlock = null;
  WalkerConfig config = null;

  public WalkerMain() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  public void run(List<BlockPos> shortenedBlocks, WalkerConfig config) {
    this.shortList = shortenedBlocks;
    this.currentBlock = shortenedBlocks.remove(0);
    this.config = config;
    this.isStart = true;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isStart) return;

    if (
      DistanceFromTo.distanceFromToXZ(
        currentBlock,
        BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector())
      ) <
      config.distanceToShift &&
      config.isShift &&
      currentBlock.getY() + 1 == ids.mc.thePlayer.posY &&
      this.currentBlock.equals(config.endBlock)
    ) {
      KeybindHandler.setKeyBindState(ids.mc.gameSettings.keyBindSneak, true);
    }

    if (
      DistanceFromTo.distanceFromToXZ(
        currentBlock,
        BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector())
      ) <
      0.7
    ) {
      nextBlock();
    }

    LookAt.Rotation needed = LookAt.getRotation(currentBlock);
    if (LookAt.done) {
      needed.setPitch(ids.mc.thePlayer.rotationPitch);
      LookAt.smoothLook(needed, 200);
    }

    KeybindHandler.setKeyBindState(ids.mc.gameSettings.keyBindForward, true);
    KeybindHandler.setKeyBindState(ids.mc.gameSettings.keyBindJump, isToJump());
  }

  void nextBlock() {
    if (!shortList.isEmpty()) {
      this.currentBlock = shortList.remove(0);
    } else {
      isStart = false;
      SendChat.chat("Done!");
      KeybindHandler.resetKeybindState();
    }
  }

  boolean isToJump() {
    return (
      ids.mc.thePlayer.posY < currentBlock.getY() &&
      DistanceFromTo.distanceFromToXZ(
        currentBlock,
        BlockUtils.fromVec3ToBlockPos(ids.mc.thePlayer.getPositionVector())
      ) <
      3
    );
  }
}
