package com.dillo.utils;

import com.dillo.utils.previous.random.SwapToSlot;
import com.dillo.utils.previous.random.checkIfItemInInv;
import com.dillo.utils.previous.random.getItemInSlot;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.random.ThreadUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class throwRod {

  public static Minecraft mc = Minecraft.getMinecraft();

  public static void throwRodInv() {
    Item rod = Items.fishing_rod;
    //swapToSlot.swapToSlot(rod);

    if (checkIfItemInInv.checkIfItemInInv(rod)) {
      int currentItem = mc.thePlayer.inventory.currentItem;
      int rodSlot = getItemInSlot.getItemSlot(Items.fishing_rod);
      //swapToSlot.swapToSlot(Items.fishing_rod);

      mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(rodSlot));

      mc.thePlayer.sendQueue.addToSendQueue(
        new C08PacketPlayerBlockPlacement(
          new BlockPos(-1, -1, -1),
          255,
          mc.thePlayer.inventory.getStackInSlot(rodSlot),
          0,
          0,
          0
        )
      );

      mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentItem));
      //swapToSlot.swapToSlot(currentItem);
    }
  }

  public static void throwRodDillo(int rodSlot, int curSlot) {
    SwapToSlot.swapToSlot(rodSlot);

    ThreadUtils.threadSleepRandom(30);

    ids.mc.thePlayer.sendQueue.addToSendQueue(
      new C08PacketPlayerBlockPlacement(
        new BlockPos(-1, -1, -1),
        255,
        ids.mc.thePlayer.inventory.getStackInSlot(rodSlot),
        0,
        0,
        0
      )
    );

    ThreadUtils.threadSleepRandom(100);

    SwapToSlot.swapToSlot(curSlot);
  }
}
