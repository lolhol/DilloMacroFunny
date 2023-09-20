package com.dillo.utils;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.item.ItemStack;

public class GetSBItems {

  public static String getSbItemName(ItemStack stack) {
    String skyblockID = getSBAtr.getSkyBlockID(stack);

    if (skyblockID != null) {
      skyblockID = skyblockID.toLowerCase();
    }

    return skyblockID;
  }

  public static int getDrillSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("drill") || skyblockID.contains("gauntlet") || skyblockID.contains("pickon")) {
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

  public static int getBarrelSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("barrel")) {
          return i;
        }
      }
    }

    return -1;
  }

  public static int getPhoneSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("abiphone")) {
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

  public static int getBoomSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("bob") && skyblockID.contains("omb")) {
          return i;
        }
      }
    }

    return -1;
  }

  public static int getFireVeilSlot() {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains("fire") && skyblockID.contains("veil")) {
          return i;
        }
      }
    }

    return -1;
  }

  public static int getCustomSlot(String name) {
    for (int i = 0; i < 8; i++) {
      ItemStack stack = ids.mc.thePlayer.inventory.mainInventory[i];

      String skyblockID = getSBAtr.getSkyBlockID(stack);

      if (skyblockID != null) {
        skyblockID = skyblockID.toLowerCase();

        if (skyblockID.contains(name)) {
          return i;
        }
      }
    }

    return -1;
  }
}
