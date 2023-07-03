package com.dillo.dilloUtils.Teleport;

import static com.dillo.dilloUtils.Utils.GetOnArmadillo.getOnArmadillo;

import com.dillo.ArmadilloMain.ArmadilloMain;
import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.RemoteControl.Movements;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.BlockCols.GetUnobstructedPos;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.GetOffArmadillo;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.StateDillo;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.dilloUtils.TpUtils.WaitThenCall;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
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
