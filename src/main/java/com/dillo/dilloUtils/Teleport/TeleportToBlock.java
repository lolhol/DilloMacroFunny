package com.dillo.dilloUtils.Teleport;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class TeleportToBlock {

  public static boolean newInputState;
  public static String newStateType;
  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  private static BlockPos nextBlock = null;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;

  public static boolean teleportToBlock(BlockPos block, long time, long waitTime, String newState) {
    newStateType = newState;
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    nextBlock = block;

    Vec3 nextBlockPos = GetUnobstructedPos.getUnobstructedPos(block);

    if (nextBlockPos == null) {
      SendChat.chat(prefix.prefix + "Failed to teleport!");
      return false;
    }

    LookAt.smoothLook(LookAt.getRotation(nextBlockPos), time);

    WaitThenCall.waitThenCall(waitTime + time, "tpStage2");
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

    WaitThenCall.waitThenCall(20, "tpStage3");
  }

  public static void teleportStage3() {
    ArmadilloStates.currentState = null;
    IsOnBlock.isOnBlock(500, nextBlock, newStateType);
  }
}
