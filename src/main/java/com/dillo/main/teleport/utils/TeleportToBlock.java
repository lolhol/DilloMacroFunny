package com.dillo.main.teleport.utils;

import static com.dillo.calls.CurrentState.*;
import static com.dillo.config.config.*;
import static com.dillo.main.utils.keybinds.AllKeybinds.SNEAK;
import static com.dillo.main.utils.looks.LookAt.serverSmoothLook;
import static com.dillo.main.utils.looks.LookAt.updateServerLook;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.calls.KillSwitch;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.teleport.TeleportMovePlayer.MoveToVertex;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetter;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetterConfig;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.getSBAtr;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.random.ThreadUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportToBlock {

  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;
  public static boolean isStartLooking = false;
  public static CurrentState newStateType;
  public static boolean tpWalkOverride;
  private static BlockPos nextBlock = null;
  private static long headMoveTime;
  private static long waitTime;

  public static boolean teleportToBlock(BlockPos block, long time, long waitTime, CurrentState newState) {
    newStateType = newState;
    ArmadilloStates.currentState = null;
    headMoveTime = time;
    TeleportToBlock.waitTime = waitTime;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    nextBlock = block;

    Vec3 nextBlockPos = adjustLook(
      ids.mc.thePlayer.getPositionVector(),
      nextBlock,
      new net.minecraft.block.Block[] { Blocks.air },
      false
    );

    if (!walkOnTP || tpWalkOverride) {
      if (nextBlockPos == null) {
        // nextBlockPos = getUnobstructedPos(nextBlock);
        return false;
      }

      if (!serverRotations) {
        LookAt.smoothLook(LookAt.getRotation(nextBlockPos), time);
      } else {
        serverSmoothLook(LookAt.getRotation(nextBlockPos), time);
        isStartLooking = true;
      }

      WaitThenCall.waitThenCall(waitTime + time, TPSTAGE2);
    } else {
      MoveToVertex vertexMover = new MoveToVertex();
      VertexGetter getVertex = new VertexGetter();
      VertexGetterConfig config = new VertexGetterConfig(ids.mc.thePlayer.getPositionVector(), nextBlock, 1.54F);

      MinecraftForge.EVENT_BUS.register(vertexMover);

      VertexGetter.VertexGetterClass vertex = getVertex.getVertex(config);

      if (vertex != null) {
        vertexMover.moveToVertex(vertex, TPSTAGEWALK, true, 60);
      } else {
        if (nextBlockPos == null) {
          return false;
        }

        if (!serverRotations) {
          LookAt.smoothLook(LookAt.getRotation(nextBlockPos), !earlyLook ? time : 52);
        } else {
          serverSmoothLook(LookAt.getRotation(nextBlockPos), time);
          isStartLooking = true;
        }

        WaitThenCall.waitThenCall(waitTime + time, TPSTAGE2);
      }
    }

    return true;
  }

  public static void teleportStage2() {
    new Thread(() -> {
      ArmadilloStates.currentState = null;

      if (!SNEAK.isPressed()) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

      int aotvSlot = GetSBItems.getAOTVSlot();
      int drillSlot = GetSBItems.getDrillSlot();

      if (aotvSlot == -1 || drillSlot == -1) {
        SendChat.chat(prefix.prefix + "Failed to find some item. Stopping.");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        return;
      }

      if (
        !getSBAtr
          .getSkyBlockID(ids.mc.thePlayer.inventory.mainInventory[ids.mc.thePlayer.inventory.currentItem])
          .toLowerCase()
          .contains("void")
      ) SwapToSlot.swapToSlot(aotvSlot);

      ThreadUtils.threadSleepRandom(20);

      ids.mc.playerController.sendUseItem(
        ids.mc.thePlayer,
        ids.mc.theWorld,
        ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem)
      );

      ThreadUtils.threadSleepRandom(20);

      SwapToSlot.swapToSlot(drillSlot);

      if (SNEAK.isPressed()) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
      WaitThenCall.waitThenCall(1, TPSTAGE3);
    })
      .start();
  }

  public static void tpStageWalk() {
    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    ArmadilloStates.currentState = null;
    Vec3 nextBlockPos = adjustLook(
      ids.mc.thePlayer.getPositionVector(),
      nextBlock,
      new net.minecraft.block.Block[] { Blocks.air },
      false
    );

    if (nextBlockPos == null) {
      SendChat.chat(prefix.prefix + "Failed to teleport!");
      ArmadilloStates.currentState = null;
      ArmadilloStates.offlineState = KillSwitch.OFFLINE;
      return;
    }

    LookAt.smoothLook(LookAt.getRotation(nextBlockPos), headMoveTime);
    WaitThenCall.waitThenCall(headMoveTime + waitTime, TPSTAGE2);
  }

  public static void teleportStage3() {
    ArmadilloStates.currentState = null;
    IsOnBlock.isOnBlock(checkOnBlockTime, nextBlock, newStateType);
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isStartLooking) return;
    updateServerLook();
  }
}
