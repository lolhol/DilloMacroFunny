package com.dillo.main.macro.main;

import static com.dillo.armadillomacro.regJump;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.main.teleport.macro.TeleportToNextBlock;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GetOffArmadillo {

  private static final KeyBinding SNEAK = Minecraft.getMinecraft().gameSettings.keyBindSneak;
  private static double blockYPos = -34;
  private static boolean startOff = false;
  private static int ammountOfCheckTicks = 0;
  private static int currTicks = 0;
  private static boolean sneak = false;
  private final int MIN_DELAY_AMOUNT = 0;
  private final int MAX_DELAY_AMOUNT = 60;
  private final Random random = new Random();
  private static CurrentState nextState = null;

  public static void getOffArmadillo(CurrentState newState, double blockY, int amountOfTicks, boolean turnOffSneak) {
    new Thread(() -> {
      ArmadilloStates.currentState = null;
      nextState = newState;

      int rodSlot = getItemInSlot.getItemSlot(Items.fishing_rod);

      throwRod.throwRodDillo(rodSlot, ids.mc.thePlayer.inventory.currentItem);
      sneak = turnOffSneak;

      regJump.reset();

      blockYPos = blockY;
      startOff = true;
      ammountOfCheckTicks = amountOfTicks;
      KeyBinding.setKeyBindState(SNEAK.getKeyCode(), sneak);
    })
      .start();
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (startOff) {
        if (currTicks <= ammountOfCheckTicks) {
          if (ArmadilloStates.isOnline()) {
            if (Math.abs(blockYPos - ids.mc.thePlayer.posY + 1) < 0.0001) {
              new Thread(() -> {
                sleepRandom();
                regJump.startStop(false);

                if (SNEAK.isPressed()) KeyBinding.setKeyBindState(SNEAK.getKeyCode(), false);

                startOff = false;
                currTicks = 0;

                if (nextState == CurrentState.NEXTBLOCKSTAGE2) {
                  TeleportToNextBlock.teleportToNextBlockStage2();
                  return;
                }

                ArmadilloStates.currentState = nextState;
              })
                .start();
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
      }
    }
  }

  private void sleepRandom() {
    try {
      int delay = MIN_DELAY_AMOUNT + random.nextInt(MAX_DELAY_AMOUNT - MIN_DELAY_AMOUNT + 1);
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
