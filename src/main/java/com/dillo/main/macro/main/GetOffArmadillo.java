package com.dillo.main.macro.main;

import static com.dillo.armadillomacro.regJump;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Random;

public class GetOffArmadillo {

    private static CurrentState setNewState = null;
    private static double blockYPos = -34;
    private static boolean startOff = false;
    private static int ammountOfCheckTicks = 0;
    private static int currTicks = 0;
    private static boolean sneak = false;
    private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
    
    private final int MIN_DELAY_AMOUNT = 150;
    private final int MAX_DELAY_AMOUNT = 400;
    private final Random random = new Random();
  
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

    regJump.reset();

    setNewState = newState;
    blockYPos = blockY;
    startOff = true;
    ammountOfCheckTicks = amountOTicks;

    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), true);
  }

  //TODO: Cactus gets off armadillo and doesnt look until like 1/2 the getting off is done

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (startOff) {
                if (currTicks <= ammountOfCheckTicks) {
                    if (ArmadilloStates.isOnline()) {
                        if (Math.abs(blockYPos - ids.mc.thePlayer.posY + 1) < 0.0001) {
                            regJump.startStop(false);
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
                    KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);
                    startOff = false;
                    currTicks = 0;
                    ArmadilloStates.currentState = null;
                }

                currTicks++;

                swapWithRandomDelay(); // Add the random delay here
            }
        }
    }

    private void swapWithRandomDelay() {
        try {
            int delay = MIN_DELAY_AMOUNT + random.nextInt(MAX_DELAY_AMOUNT - MIN_DELAY_AMOUNT + 1);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
