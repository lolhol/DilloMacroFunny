package com.dillo.dilloUtils.Teleport;

import static com.dillo.ArmadilloMain.CurrentState.*;
import static com.dillo.ArmadilloMain.KillSwitch.ONLINE;
import static com.dillo.data.config.actuallySwitchAOTV;
import static com.dillo.dilloUtils.BlockUtils.GetUnobstructedPosFromCustom.getUnobstructedPosUnlessNull;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class TeleportToNextBlock {

  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;
  public static boolean isThrowRod = true;
  private static int clearAttempts = 0;

  public static void teleportToNextBlock() {
    if (ArmadilloStates.offlineState == ONLINE) {
      BlockPos nextBlock = GetNextBlock.getNextBlock();
      nextBlockInList = nextBlock;
      isTeleporting = true;

      if (nextBlock == null) {
        SendChat.chat(prefix.prefix + "FAILED TO TELEPORT FOR SOME REASON! DM GODBRIGERO!");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = ONLINE;

        return;
      }

      if (actuallySwitchAOTV) swapToSlot.swapToSlot(GetSBItems.getAOTVSlot());

      if (isThrowRod) {
        LookWhileGoingDown.lookUntilState(NEXTBLOCKSTAGE2, nextBlock, config.tpHeadMoveSpeed);
        ArmadilloStates.currentState = STARTCHECKDILLO;
      } else {
        ArmadilloStates.currentState = NEXTBLOCKSTAGE2;
      }
    }
  }

  public static void teleportToNextBlockStage2() {
    boolean result = TeleportToBlock.teleportToBlock(nextBlockInList, 200, config.tpWait, ARMADILLO);
    if (!result) {
      Vec3 hitVec = getUnobstructedPosUnlessNull(ids.mc.thePlayer.getPosition(), nextBlockInList);

      if (
        DistanceFromTo.distanceFromTo(BlockUtils.fromVec3ToBlockPos(hitVec), ids.mc.thePlayer.getPosition()) <= 3.16 &&
        clearAttempts < 2
      ) {
        ArmadilloStates.currentState = ARMADILLO;
      } else {
        if (config.smartTeleport) {
          SendChat.chat(prefix.prefix + "Route is obstructed! Attempting to tp with smart teleport module!");
          SmartTP.smartTP(nextBlockInList);
        } else {
          SendChat.chat(prefix.prefix + "Route is obstructed!");
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        }
      }
    }
  }
}
