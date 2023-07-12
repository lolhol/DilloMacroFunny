package com.dillo.dilloUtils.Teleport;

import static com.dillo.ArmadilloMain.CurrentState.*;
import static com.dillo.data.config.*;
import static com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos.getUnobstructedPos;
import static com.dillo.dilloUtils.TpUtils.WalkForward.walkForward;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class TeleportToBlock {

  public static boolean newInputState;
  public static CurrentState newStateType;
  public static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  private static BlockPos nextBlock = null;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;

  public static boolean teleportToBlock(BlockPos block, long time, long waitTime, CurrentState newState) {
    newStateType = newState;
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    nextBlock = block;

    if (!walkOnTP) {
      Vec3 nextBlockPos = adjustLook(
        ids.mc.thePlayer.getPositionVector(),
        nextBlock,
        new net.minecraft.block.Block[] { Blocks.air },
        false
      );

      if (nextBlockPos == null) {
        // nextBlockPos = getUnobstructedPos(nextBlock);
        return false;
      }

      LookAt.smoothLook(LookAt.getRotation(nextBlockPos), time);

      WaitThenCall.waitThenCall(waitTime + time, TPSTAGE2);
    } else {
      walkForward(forwardForTicks, TPSTAGEWALK);
    }

    return true;
  }

  public static void teleportStage2() {
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    int aotvSlot = GetSBItems.getAOTVSlot();
    if (aotvSlot != -1) {
      swapToSlot.swapToSlot(GetSBItems.getAOTVSlot());
      ids.mc.playerController.sendUseItem(
        ids.mc.thePlayer,
        ids.mc.theWorld,
        ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem)
      );
    }

    int drillSlot = GetSBItems.getDrillSlot();
    if (drillSlot != -1) {
      swapToSlot.swapToSlot(drillSlot);
    }

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

    WaitThenCall.waitThenCall(20, TPSTAGE3);
  }

  public static void tpStageWalk() {
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

    LookAt.smoothLook(LookAt.getRotation(nextBlockPos), 100);

    WaitThenCall.waitThenCall(300, TPSTAGE2);
  }

  public static void teleportStage3() {
    ArmadilloStates.currentState = null;
    IsOnBlock.isOnBlock(checkOnBlockTime, nextBlock, newStateType);
  }
}
