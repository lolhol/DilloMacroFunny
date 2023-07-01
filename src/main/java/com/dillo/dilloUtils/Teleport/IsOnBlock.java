package com.dillo.dilloUtils.Teleport;

import static com.dillo.data.config.reTeleport;
import static com.dillo.dilloUtils.Teleport.TeleportToNextBlock.isTeleporting;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.FailSafes.RestartMacroFailsafe;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class IsOnBlock {

  private static boolean startCheck = false;
  private static int checkTime = 0;
  private static BlockPos blockPos = null;
  private static String nextState = null;
  private static int curTicks = 0;
  private static int curReTps = 0;
  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;

  public static void isOnBlock(int checkTimeTicks, BlockPos nextBlock, String newString) {
    blockPos = nextBlock;
    nextState = newString;
    checkTime = checkTimeTicks;
    startCheck = true;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startCheck) {
        if (curTicks <= checkTime) {
          if (blockPos != null) {
            if (Math.abs(ids.mc.thePlayer.posX - blockPos.getX() - 0.5) < 0.0001 && Math.abs(ids.mc.thePlayer.posZ - blockPos.getZ() - 0.5) < 0.0001) {
              startCheck = false;

              SendChat.chat(prefix.prefix + "Teleported successfully!");
              ids.mc.thePlayer.rotationPitch = 5;
              KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

              if (RestartMacroFailsafe.isRestart) {
                ArmadilloStates.offlineState = "online";
                RestartMacroFailsafe.isRestart = false;
              }

              isTeleporting = true;

              currentRoute.currentBlock = blockPos;
              ArmadilloStates.currentState = nextState;
            }
          }
        } else {
          SendChat.chat(prefix.prefix + "Failed to teleport! " + (reTeleport ? "Re-Teleporting!" : ""));

          curTicks = 0;
          startCheck = false;

          if (reTeleport && curReTps <= config.reTpTimes) {
            curReTps++;
            ArmadilloStates.currentState = "Re-Teleporting";
            TeleportToNextBlock.teleportToNextBlock();
          }
        }
      }

      if (startCheck)
        curTicks++;
    }
  }
}
