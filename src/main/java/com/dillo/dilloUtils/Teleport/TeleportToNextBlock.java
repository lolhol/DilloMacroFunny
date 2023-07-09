package com.dillo.dilloUtils.Teleport;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

import java.util.Objects;

import static com.dillo.data.config.actuallySwitchAOTV;

public class TeleportToNextBlock {

  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;
  public static boolean isThrowRod = true;

  public static void teleportToNextBlock() {
    if (!Objects.equals(ArmadilloStates.offlineState, "offline")) {
      BlockPos nextBlock = GetNextBlock.getNextBlock();
      nextBlockInList = nextBlock;
      isTeleporting = true;

      if (nextBlock == null) {
        SendChat.chat(prefix.prefix + "FAILED TO TELEPORT FOR SOME REASON! DM GODBRIGERO!");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = "offline";

        return;
      }

      if (isThrowRod) {
        LookWhileGoingDown.lookUntilState("NextBlockStage2", nextBlock, config.tpHeadMoveSpeed);
        ArmadilloStates.currentState = "startCheckDillo";
      } else {
        ArmadilloStates.currentState = "NextBlockStage2";
      }

      if (actuallySwitchAOTV) swapToSlot.swapToSlot(GetSBItems.getAOTVSlot());
    }
  }

  public static void teleportToNextBlockStage2() {
    boolean result = TeleportToBlock.teleportToBlock(nextBlockInList, 200, config.tpWait, "armadillo");

    if (!result) {
      if (config.smartTeleport) {
        SendChat.chat(prefix.prefix + "Route is obstructed! Attempting to tp with smart teleport module!");
        SmartTP.smartTP(nextBlockInList);
      } else {
        SendChat.chat(prefix.prefix + "Route is obstructed!");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = "offline";
      }
    }
  }
}
