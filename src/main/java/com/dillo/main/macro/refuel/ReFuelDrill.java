package com.dillo.main.macro.refuel;

import static com.dillo.calls.CurrentState.ARMADILLO;
import static com.dillo.calls.CurrentState.REFUELING;
import static com.dillo.utils.GetSBItems.*;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import com.dillo.utils.throwRod;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;

public class ReFuelDrill {

  private static boolean isOpen = false;
  private static boolean start = false;
  private static boolean start1 = false;
  public static boolean isStart = false;

   private final int MIN_DELAY_AMOUNT = 100;
    private final int MAX_DELAY_AMOUNT = 300;
    private final Random random = new Random();

     public void reFuelDrill() {
        isStart = true;

        throwRod.throwRodInv();

        ArmadilloStates.currentState = REFUELING;
        ArmadilloStates.offlineState = KillSwitch.OFFLINE;

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            start = true;

            int slot = getPhoneSlot();

            if (slot == -1) {
                return;
            }

            swapWithRandomDelay(slot);

            ids.mc.thePlayer.sendQueue.addToSendQueue(
                new C08PacketPlayerBlockPlacement(
                    new BlockPos(-1, -1, -1),
                    255,
                    ids.mc.thePlayer.inventory.getStackInSlot(slot),
                    0,
                    0,
                    0
                )
            );

            swapWithRandomDelay(250);

            start = true;

            swapWithRandomDelay(10000);

            if (!isOpen) {
                start = false;
            }
        })
        .start();
    }

    public void stage3() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            clickSlotShift(81 + getDrillSlot(), 0);

            swapWithRandomDelay(500);

            clickSlotShift(81 + getBarrelSlot(), 0);

            swapWithRandomDelay(500);

            clickSlot(22, 0);

            swapWithRandomDelay(500);

            clickSlot(22, 0);

            swapWithRandomDelay(500);

            ids.mc.thePlayer.closeScreen();

            swapWithRandomDelay(500);

            swapWithRandomDelay(500);

            if (!Objects.equals(getInventoryName(ids.mc.currentScreen), "")) {
                ids.mc.thePlayer.closeScreen();

                swapWithRandomDelay(500);
            }

            if (ThrowAtEnd.isThrow) {
                int slot = getItemInSlot.getItemSlot(Items.fishing_rod);

                swapToSlot.swapToSlot(slot);

                swapWithRandomDelay(200);

                ids.mc.thePlayer.sendQueue.addToSendQueue(
                    new C08PacketPlayerBlockPlacement(
                        new BlockPos(-1, -1, -1),
                        255,
                        ids.mc.thePlayer.inventory.getStackInSlot(slot),
                        0,
                        0,
                        0
                    )
                );
            }

            if (ArmadilloStates.currentState == REFUELING) {
                ArmadilloStates.offlineState = KillSwitch.ONLINE;
                ArmadilloStates.currentState = ARMADILLO;
            }
        })
        .start();
    }

    private void swapWithRandomDelay(int delayTime) {
        try {
            int delay = MIN_DELAY_AMOUNT + random.nextInt(MAX_DELAY_AMOUNT - MIN_DELAY_AMOUNT + 1);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void swapWithRandomDelay(int slot) {
        swapToSlot.swapToSlot(slot);
        swapWithRandomDelay(100);
    }

  public static void stage2() {
    new Thread(() -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      start = false;
      List<Slot> chestInventory = ((GuiChest) ids.mc.currentScreen).inventorySlots.inventorySlots;

      for (int i = 0; i < chestInventory.size(); i++) {
        Slot curSlot = chestInventory.get(i);

        String lowerString = curSlot.getStack().getDisplayName().toLowerCase();

        if (lowerString.contains("jotraeline")) {
          clickSlot(i, 0);
          break;
        }
      }

      start1 = true;

      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      start1 = false;
    })
      .start();
  }

  @SubscribeEvent
  public void onBackgroundDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
    if (ArmadilloStates.currentState == REFUELING) {
      if (start) {
        if (getInventoryName(event.gui).toLowerCase().contains("abiphone")) {
          isOpen = true;
          stage2();
        }
      }

      if (start1) {
        if (getInventoryName(event.gui).toLowerCase().contains("anvil")) {
          start1 = false;
          stage3();
        }
      }
    }
  }

  public void stage3() {
    new Thread(() -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      clickSlotShift(81 + getDrillSlot(), 0);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      clickSlotShift(81 + getBarrelSlot(), 0);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      clickSlot(22, 0);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      clickSlot(22, 0);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      ids.mc.thePlayer.closeScreen();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (!Objects.equals(getInventoryName(ids.mc.currentScreen), "")) {
        ids.mc.thePlayer.closeScreen();

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      if (ThrowAtEnd.isThrow) {
        int slot = getItemInSlot.getItemSlot(Items.fishing_rod);

        swapToSlot.swapToSlot(slot);

        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        ids.mc.thePlayer.sendQueue.addToSendQueue(
          new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1),
            255,
            ids.mc.thePlayer.inventory.getStackInSlot(slot),
            0,
            0,
            0
          )
        );
      }

      if (ArmadilloStates.currentState == REFUELING) {
        ArmadilloStates.offlineState = KillSwitch.ONLINE;
        ArmadilloStates.currentState = ARMADILLO;
      }
    })
      .start();
  }

  public static void clickSlot(int slot, int windowAdd) {
    ids.mc.playerController.windowClick(
      ids.mc.thePlayer.openContainer.windowId + windowAdd,
      slot,
      0,
      0,
      ids.mc.thePlayer
    );
  }

  public static void clickSlotShift(int slot, int windowAdd) {
    ids.mc.playerController.windowClick(
      ids.mc.thePlayer.openContainer.windowId + windowAdd,
      slot,
      0,
      1,
      ids.mc.thePlayer
    );
  }

  public static String getInventoryName(GuiScreen gui) {
    if (gui instanceof GuiChest) {
      return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory()
        .getDisplayName()
        .getUnformattedText();
    } else return "";
  }
}
