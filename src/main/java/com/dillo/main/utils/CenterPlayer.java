package com.dillo.main.utils;

import static com.dillo.calls.CurrentState.CENTERSTAGE2;
import static com.dillo.main.utils.keybinds.AllKeybinds.SNEAK;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.main.teleport.utils.WaitThenCall;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class CenterPlayer {

  private static CurrentState state = null;

  public static void centerPlayerOnBlockUnder(CurrentState newState) {
    state = newState;
    BlockPos blockUnder = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 1, ids.mc.thePlayer.posZ);

    if (!canBeBroken(blockUnder)) {
      throwRod.throwRodInv();
    }

    LookAt.smoothLook(LookAt.getRotation(blockUnder), 200);
    WaitThenCall.waitThenCall(230, CENTERSTAGE2);
  }

  public static void centerStage2() {
    ArmadilloStates.currentState = null;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);

    int aotvSlot = GetSBItems.getAOTVSlot();
    if (aotvSlot != -1) {
      SwapToSlot.swapToSlot(GetSBItems.getAOTVSlot());
      ids.mc.playerController.sendUseItem(
        ids.mc.thePlayer,
        ids.mc.theWorld,
        ids.mc.thePlayer.inventory.getStackInSlot(ids.mc.thePlayer.inventory.currentItem)
      );
    }

    int drillSlot = GetSBItems.getDrillSlot();
    if (drillSlot != -1) {
      SwapToSlot.swapToSlot(drillSlot);
    }

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

    ArmadilloStates.currentState = state;
  }

  public static boolean canBeBroken(BlockPos block) {
    BlockPos playerPos = ids.mc.thePlayer.getPosition();

    Vec3 startVec = new Vec3(playerPos.getX() + 0.5, playerPos.getY() + 1, playerPos.getZ() + 0.5);
    Vec3 endVec = new Vec3(block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5);

    MovingObjectPosition result = ids.mc.theWorld.rayTraceBlocks(startVec, endVec, false);

    if (result == null || result.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
      // No obstruction found
      return false;
    }

    if ((result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) && result.hitVec != endVec) {
      // Obstruction found
      return true;
    }

    return false;
  }
}
