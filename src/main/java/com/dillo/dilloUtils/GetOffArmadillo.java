package com.dillo.dilloUtils;

import akka.io.Udp;
import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.data.config;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.throwRod;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GetOffArmadillo {

  private static String setNewState = null;
  private static double blockYPos = -34;
  private static boolean startOff = false;
  private static int ammountOfCheckTicks = 0;
  private static int currTicks = 0;
  private static boolean sneak = false;
  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;

  public static void getOffArmadillo(String newState, double blockY, int amountOTicks, boolean turnOffSneak) {
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
          if (Objects.equals(ArmadilloStates.offlineState, "online")) {
            if (!ids.mc.thePlayer.isRiding()) {
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
