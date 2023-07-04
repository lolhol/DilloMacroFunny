package com.dillo.dilloUtils.Teleport;

import static com.dillo.data.config.actuallySwitchAOTV;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class TeleportToNextBlock {

  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  private static final KeyBinding forward = Minecraft.getMinecraft().gameSettings.keyBindForward;

  public static void teleportToNextBlock() {
    if (!Objects.equals(ArmadilloStates.offlineState, "offline")) {
      BlockPos nextBlock = GetNextBlock.getNextBlock();
      nextBlockInList = nextBlock;
      isTeleporting = true;

      if (actuallySwitchAOTV) swapToSlot.swapToSlot(GetSBItems.getAOTVSlot());

      if (nextBlock == null) {
        SendChat.chat(prefix.prefix + "FAILED TO TELEPORT FOR SOME REASON! DM GODBRIGERO!");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = "offline";

        return;
      }

      LookWhileGoingDown.lookUntilState("NextBlockStage2", nextBlock, config.tpHeadMoveSpeed);
      ArmadilloStates.currentState = "startCheckDillo";
    }
  }

  public static void teleportToNextBlockStage2() {
    boolean result = TeleportToBlock.teleportToBlock(nextBlockInList, 200, config.tpWait, "armadillo");

    if (!result) {
      SendChat.chat(prefix.prefix + "Route is obstructed!");
      ArmadilloStates.currentState = null;
      ArmadilloStates.offlineState = "online";
    }
  }
}
