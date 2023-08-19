package com.dillo.main.teleport.macro;

import static com.dillo.armadillomacro.vertexMover;
import static com.dillo.calls.CurrentState.*;
import static com.dillo.calls.KillSwitch.ONLINE;
import static com.dillo.config.config.actuallySwitchAOTV;
import static com.dillo.config.config.earlyLook;
import static com.dillo.main.macro.main.StateDillo.canDillo;
import static com.dillo.main.teleport.utils.LookWhileGoingDown.stopLook;
import static com.dillo.main.utils.GetMostOptimalPath.isClear;
import static com.dillo.main.utils.keybinds.AllKeybinds.SNEAK;
import static com.dillo.main.utils.looks.LookYaw.curRotation;
import static com.dillo.utils.BlockUtils.getNextBlock;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetter;
import com.dillo.main.teleport.TeleportMovePlayer.VertexGetterConfig;
import com.dillo.main.teleport.utils.LookWhileGoingDown;
import com.dillo.main.teleport.utils.TeleportToBlock;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.RandomisationUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class TeleportToNextBlock {

  public static final KeyBinding SNEEK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  public static boolean isThrowRod = true;
  public static int clearAttempts = 0;
  public static boolean isClearing = false;
  public static boolean alrMoved = false;
  public static boolean attemptedToSmartTP = false;

  public static void teleportToNextBlock() {
    new Thread(() -> {
      if (ArmadilloStates.isOnline()) {
        BlockPos nextBlock = getNextBlock();
        nextBlockInList = nextBlock;
        isTeleporting = true;

        if (nextBlock == null) {
          SendChat.chat(prefix.prefix + "FAILED TO TELEPORT FOR SOME REASON! DM GODBRIGERO!");
          ArmadilloStates.currentState = null;
          ArmadilloStates.offlineState = ONLINE;
          return;
        }

        int aotv = GetSBItems.getAOTVSlot();

        if (aotv == -1) {
          SendChat.chat(prefix.prefix + "Failed to find aotv in hotbar :/");
          return;
        }

        if (actuallySwitchAOTV) SwapToSlot.swapToSlot(aotv);

        if (isThrowRod) {
          if (earlyLook) LookWhileGoingDown.lookUntilState(
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
    })
      .start();
  }

  public static void teleportToNextBlockStage2() {
    stopLook();
    boolean result = TeleportToBlock.teleportToBlock(
      nextBlockInList,
      config.tpHeadMoveSpeed + RandomisationUtils.getRandomAdd(config.tpHeadMoveSpeed),
      config.tpWait + RandomisationUtils.getRandomAdd(config.tpWait),
      ARMADILLO
    );

    if (!result) {
      isThrowRod = false;
      stopLook();

      if (!attemptedToSmartTP) {
        if (config.smartTeleport) {
          SendChat.chat(prefix.prefix + "Route is obstructed! Attempting other method of tp!");
          SmartTP.smartTP(nextBlockInList, false);
          attemptedToSmartTP = true;
          return;
        }
      }

      VertexGetter getVertex = new VertexGetter();
      VertexGetterConfig vertConfig = new VertexGetterConfig(
        ids.mc.thePlayer.getPositionVector(),
        nextBlockInList,
        1.54F
      );
      VertexGetter.VertexGetterClass vertex = getVertex.getVertex(vertConfig);

      if (vertex != null && !alrMoved) {
        vertexMover.moveToVertex(vertex, TPSTAGEWALK, true, 60);
        alrMoved = true;
        return;
      }

      if (canDillo() && clearAttempts < 5) {
        KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
        isClear = true;
        ArmadilloStates.currentState = ARMADILLO;
        clearAttempts++;
        LookAt.smoothLook(new LookAt.Rotation(20, curRotation()), 20);
        isClearing = true;
        return;
      }

      SendChat.chat(prefix.prefix + "Route is obstructed!");
      ArmadilloStates.currentState = null;
      ArmadilloStates.offlineState = KillSwitch.OFFLINE;
    }
  }
}
