package com.dillo.utils;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.item.ItemStack;

public class GetSBItems {

  public static int getDrillSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("drill") || skyblockID.contains("gauntlet") || skyblockID.contains("picko")) {
          return i;
        }
      }
    }

    return -1;
  }

  public static int getAOTVSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("void")) {
          return i;
        }
      }
    }

    return -1;
  }

  public static int getSack() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);
      String skyblock = getSBAtr.getSBData(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("sack")) {
          SendChat.chat("!!!");
          getSBAtr.getSBData(stack);
          //SendChat.chat(skyblock);
          return i;
        }
      }
    }

    return -1;
  }
}
