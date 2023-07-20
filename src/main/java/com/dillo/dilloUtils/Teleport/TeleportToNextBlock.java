package com.dillo.dilloUtils.Teleport;

import static com.dillo.ArmadilloMain.CurrentState.*;
import static com.dillo.ArmadilloMain.KillSwitch.ONLINE;
import static com.dillo.data.config.actuallySwitchAOTV;
import static com.dillo.dilloUtils.StateDillo.canDillo;
import static com.dillo.dilloUtils.Teleport.TeleportToBlock.SNEAK;
import static com.dillo.dilloUtils.TpUtils.LookWhileGoingDown.stopLook;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.isClear;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.RandomisationUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class TeleportToNextBlock {

  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  public static boolean isThrowRod = true;
  public static int clearAttempts = 0;
  public static final KeyBinding SNEEK = Minecraft.getMinecraft().gameSettings.keyBindSneak;

  public static void teleportToNextBlock() {
    if (ArmadilloStates.offlineState == ONLINE) {
      KeyBinding.setKeyBindState(SNEEK.getKeyCode(), false);

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
        LookWhileGoingDown.lookUntilState(
          NEXTBLOCKSTAGE2,
          nextBlock,
          config.tpHeadMoveSpeed + RandomisationUtils.getRandomAdd(config.tpHeadMoveSpeed)
        );
        ArmadilloStates.currentState = STARTCHECKDILLO;
      } else {
        ArmadilloStates.currentState = NEXTBLOCKSTAGE2;
        isThrowRod = true;
      }
    }
  }

  public static void teleportToNextBlockStage2() {
    stopLook();
    boolean result = TeleportToBlock.teleportToBlock(
      nextBlockInList,
      20 + RandomisationUtils.getRandomAdd(50),
      config.tpWait + RandomisationUtils.getRandomAdd(config.tpWait),
      ARMADILLO
    );

    if (!result) {
      stopLook();
      if (canDillo() && clearAttempts < 2) {
        KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
        isClear = true;
        ArmadilloStates.currentState = ARMADILLO;
        clearAttempts++;
      } else {
        if (config.smartTeleport) {
          SendChat.chat(prefix.prefix + "Route is obstructed! Attempting other method of tp!");
          SmartTP.smartTP(nextBlockInList, false);
        } else {
          SendChat.chat(prefix.prefix + "Route is obstructed!");
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = KillSwitch.OFFLINE;
        }
      }
    }
  }
}
