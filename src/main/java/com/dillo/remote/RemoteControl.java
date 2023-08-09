package com.dillo.remote;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.config.config;
import com.dillo.main.failsafes.RestartMacroFailsafe;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.main.teleport.utils.TeleportToBlock;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.calls.CurrentState.ARMADILLO;
import static com.dillo.main.utils.keybinds.AllKeybinds.JUMP;

public class RemoteControl {

    private static boolean actionType = false;
    private static int currentTime = 0;
    private static int i = 0;

    public static void jump() {
        new Thread(() -> {
            try {
                KeyBinding.setKeyBindState(JUMP.getKeyCode(), true);
                Thread.sleep(10);
                KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
            } catch (InterruptedException e) {
                KeyBinding.setKeyBindState(JUMP.getKeyCode(), false);
            }
        })
                .start();
    }

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
}
