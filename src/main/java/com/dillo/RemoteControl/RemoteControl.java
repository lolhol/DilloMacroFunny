package com.dillo.RemoteControl;

import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.data.config;
import com.dillo.dilloUtils.FailSafes.RestartMacroFailsafe;
import com.dillo.dilloUtils.Teleport.TeleportToBlock;
import com.dillo.dilloUtils.Teleport.TeleportToNextBlock;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RemoteControl {

  private static boolean actionType = false;
  private static int currentTime = 0;
  private static final KeyBinding jump = Minecraft.getMinecraft().gameSettings.keyBindJump;
  private static int i = 0;

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (/*ArmadilloStates.isOnline() && */config.remoteControl) {
        if (currentTime >= config.timeBetweenExcecutions * 20) {
          //SendChat.chat(String.valueOf(GetRemoteControl.remoteControlActions.size()));
          if (GetRemoteControl.remoteControlActions.size() > i) {
            JsonElement currentAction = GetRemoteControl.remoteControlActions.get(i);
            String currentA = currentAction.getAsString();

            if (!currentA.startsWith("SAY_")) {
              actionType = true;
            } else {
              actionType = false;
            }

            if (actionType) {
              switch (currentA) {
                case "JUMP":
                  jump();
                  break;
                case "PAUSE":
                  PauseMacro.pauseMacro(config.timeForPause, false);
                  break;
                case "FORWARD":
                  Movements.movements(Minecraft.getMinecraft().gameSettings.keyBindForward, config.walkTime * 20);
                  break;
                case "BACK":
                  Movements.movements(Minecraft.getMinecraft().gameSettings.keyBindBack, config.walkTime * 20);
                  break;
                case "RETP":
                  BlockPos nextBlockInList = TeleportToNextBlock.nextBlockInList;
                  TeleportToBlock.teleportToBlock(nextBlockInList, config.tpHeadMoveSpeed, config.tpWait, ARMADILLO);
                  break;
                case "RESTART":
                  RestartMacroFailsafe.restartMacro();
                  break;
                case "STOP":
                  ArmadilloStates.currentState = null;
                  ArmadilloStates.offlineState = KillSwitch.OFFLINE;
                  break;
                case "THROWR":
                  throwRod.throwRodInv();
                  break;
              }
            } else {
              ids.mc.thePlayer.addChatMessage(new ChatComponentText(currentA.replace("SAY_", "")));
            }

            i++;
          } else {
            GetRemoteControl.remoteControlActions = new JsonArray();
            i = 0;
          }

          currentTime = 0;
        } else {
          //KeyBinding.setKeyBindState(jump.getKeyCode(), false);
          currentTime++;
        }
      }
    }
  }

  public static void jump() {
    new Thread(() -> {
      try {
        KeyBinding.setKeyBindState(jump.getKeyCode(), true);
        Thread.sleep(10);
        KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      } catch (InterruptedException e) {
        KeyBinding.setKeyBindState(jump.getKeyCode(), false);
      }
    })
      .start();
  }
}
