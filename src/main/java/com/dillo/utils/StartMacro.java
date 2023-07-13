package com.dillo.utils;

import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;
import static com.dillo.ArmadilloMain.KillSwitch.OFFLINE;
import static com.dillo.ArmadilloMain.KillSwitch.ONLINE;
import static com.dillo.dilloUtils.StateDillo.stateDilloNoGettingOn;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.dilloUtils.BlockUtils.CheckIfOnBlock;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
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
          String str = ArmadilloStates.offlineState == OFFLINE
            ? prefix.prefix + "Starting macro!"
            : prefix.prefix + "Macro stopped!";
          ArmadilloStates.offlineState = ArmadilloStates.offlineState == OFFLINE ? ONLINE : OFFLINE;
          ArmadilloStates.currentState = ArmadilloStates.offlineState == ONLINE ? ARMADILLO : null;

          SendChat.chat(str);
        }
      } else {
        SendChat.chat(prefix.prefix + "Cant start macro! (Some items are not in inventory.)");
      }
    } else {
      SendChat.chat(
        ArmadilloStates.offlineState == OFFLINE ? prefix.prefix + "Please stand on a block on route!" : "Macro stopped!"
      );
      ArmadilloStates.offlineState = OFFLINE;
      ArmadilloStates.currentState = null;
    }
  }
}
