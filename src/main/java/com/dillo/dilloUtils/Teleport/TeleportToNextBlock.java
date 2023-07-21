package com.dillo.dilloUtils.Teleport;

import static com.dillo.ArmadilloMain.CurrentState.*;
import static com.dillo.ArmadilloMain.KillSwitch.ONLINE;
import static com.dillo.armadillomacro.vertexMover;
import static com.dillo.data.config.actuallySwitchAOTV;
import static com.dillo.dilloUtils.MoreLegitSpinDrive.makeNewBlock;
import static com.dillo.dilloUtils.NewSpinDrive.isFirst;
import static com.dillo.dilloUtils.StateDillo.canDillo;
import static com.dillo.dilloUtils.Teleport.TeleportToBlock.SNEAK;
import static com.dillo.dilloUtils.TpUtils.LookWhileGoingDown.stopLook;
import static com.dillo.dilloUtils.Utils.GetMostOptimalPath.isClear;
import static com.dillo.dilloUtils.Utils.LookYaw.curRotation;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.LookAt;
import com.dillo.dilloUtils.Teleport.TeleportMovePlayer.MoveToVertex;
import com.dillo.dilloUtils.Teleport.TeleportMovePlayer.VertexGetter;
import com.dillo.dilloUtils.Teleport.TeleportMovePlayer.VertexGetterConfig;
import com.dillo.dilloUtils.TpUtils.LookWhileGoingDown;
import com.dillo.utils.GetSBItems;
import com.dillo.utils.RandomisationUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.previous.random.swapToSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class TeleportToNextBlock {

  public static BlockPos nextBlockInList = null;
  public static boolean isTeleporting = false;
  public static boolean isThrowRod = true;
  public static int clearAttempts = 0;
  public static final KeyBinding SNEEK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  public static boolean isClearing = false;
  public static boolean alrMoved = false;
  public static boolean attemptedToSmartTP = false;

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
        /*LookWhileGoingDown.lookUntilState(
          NEXTBLOCKSTAGE2,
          nextBlock,
          config.tpHeadMoveSpeed + RandomisationUtils.getRandomAdd(config.tpHeadMoveSpeed)
        );*/
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
      config.tpHeadMoveSpeed + RandomisationUtils.getRandomAdd(config.tpHeadMoveSpeed),
      config.tpWait + RandomisationUtils.getRandomAdd(config.tpWait),
      ARMADILLO
    );

    if (!result) {
      stopLook();

      VertexGetter getVertex = new VertexGetter();
      VertexGetterConfig vertConfig = new VertexGetterConfig(
        ids.mc.thePlayer.getPositionVector(),
        nextBlockInList,
        1.54F
      );
      VertexGetter.VertexGetterClass vertex = getVertex.getVertex(vertConfig);

      if (vertex != null && !alrMoved) {
        vertexMover.moveToVertex(vertex, TPSTAGEWALK, true);
        alrMoved = true;
        return;
      }

      if (!attemptedToSmartTP) {
        if (config.smartTeleport) {
          SendChat.chat(prefix.prefix + "Route is obstructed! Attempting other method of tp!");
          SmartTP.smartTP(nextBlockInList, false);
        }
      }

      if (canDillo() && clearAttempts < 3) {
        attemptedToSmartTP = false;
        KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
        isClear = true;
        ArmadilloStates.currentState = ARMADILLO;
        clearAttempts++;
        LookAt.smoothLook(new LookAt.Rotation(20, curRotation()), 20);
        isClearing = true;
      } else {
        SendChat.chat(prefix.prefix + "Route is obstructed!");
        ArmadilloStates.currentState = null;
        ArmadilloStates.offlineState = KillSwitch.OFFLINE;
      }
    }
  }
}
