package com.dillo.dilloUtils.ReFuelDrill;

import static com.dillo.ArmadilloMain.CurrentState.ARMADILLO;
import static com.dillo.ArmadilloMain.CurrentState.REFUELING;
import static com.dillo.utils.GetSBItems.*;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import com.dillo.utils.throwRod;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReFuelDrill {

  private static boolean isOpen = false;
  private static boolean start = false;
  private static boolean start1 = false;
  public static boolean isStart = false;

  public static void reFuelDrill() {
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

      swapToSlot.swapToSlot(slot);

      try {
        Thread.sleep(100);
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

      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        SendChat.chat("!!!");
      }

      start = true;

      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        SendChat.chat("!!!");
      }

      if (!isOpen) {
        start = false;
      }
    })
      .start();
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

      clickSlotShift(89 - getBarrelSlot(), 0);

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

      if (ThrowAtEnd.isThrow) {
        int slot = getItemInSlot.getItemSlot(Items.fishing_rod);

        try {
          Thread.sleep(100);
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
