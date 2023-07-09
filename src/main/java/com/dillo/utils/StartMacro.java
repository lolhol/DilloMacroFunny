package com.dillo.utils;

import static com.dillo.ArmadilloMain.ArmadilloStates.armadillo;
import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;
import static com.dillo.ArmadilloMain.KillSwitch.OFFLINE;
import static com.dillo.ArmadilloMain.KillSwitch.ONLINE;
import static com.dillo.dilloUtils.StateDillo.stateDilloNoGettingOn;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.CheckIfOnBlock;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.util.BlockPos;

public class StartMacro {

  public static void startMacro() {
    BlockPos blockUnder = CheckIfOnBlock.checkIfOnBlock();

    if (blockUnder != null) {
      currentRoute.currentBlock = blockUnder;
      if (InInvItemsCheck.checkItems()) {
        if (ids.mc.thePlayer.isRiding()) {
          stateDilloNoGettingOn();
        } else {
          armadillo = !armadillo;

          String str = armadillo ? "Starting macro..." : "Macro stopped!";
          ArmadilloStates.offlineState = armadillo ? ONLINE : OFFLINE;
          ArmadilloStates.currentState = armadillo ? ARMADILLO : null;

          SendChat.chat("§l§4[MIT+]§r " + str);
        }
      } else {
        SendChat.chat("§l§4[MIT+]§r Cant start macro! (Some items are not in inventory.)");
        armadillo = false;
      }
    } else {
      SendChat.chat(prefix.prefix + "Please stand on a block on route!");
      ArmadilloStates.offlineState = OFFLINE;
      ArmadilloStates.currentState = null;
    }
  }
}
