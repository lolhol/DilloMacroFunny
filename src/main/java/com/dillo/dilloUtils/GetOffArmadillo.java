package com.dillo.dilloUtils;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.CurrentState;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GetOffArmadillo {

  private static CurrentState setNewState = null;
  private static double blockYPos = -34;
  private static boolean startOff = false;
  private static int ammountOfCheckTicks = 0;
  private static int currTicks = 0;
  private static boolean sneak = false;
  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;

  public static void getOffArmadillo(CurrentState newState, double blockY, int amountOTicks, boolean turnOffSneak) {
    throwRod.throwRodInv();
    ArmadilloStates.currentState = null;
    sneak = turnOffSneak;

    setNewState = newState;
    blockYPos = blockY;
    startOff = true;
    ammountOfCheckTicks = amountOTicks;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startOff) {
        if (currTicks <= ammountOfCheckTicks) {
          if (ArmadilloStates.isOnline()) {
            if (Math.abs(blockYPos - ids.mc.thePlayer.posY + 1) < 0.0001) {
              if (sneak) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
              startOff = false;
              currTicks = 0;
              ArmadilloStates.currentState = setNewState;
            }
          } else {
            startOff = false;
            currTicks = 0;
            blockYPos = -34;
          }
        } else {
          SendChat.chat("Failed to get off the dillo.");
          startOff = false;
          currTicks = 0;
          ArmadilloStates.currentState = null;
        }

        currTicks++;
      }
    }
  }
}
