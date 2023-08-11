package com.dillo.utils;

import com.dillo.utils.previous.SendChat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class getSBAtr {

    public static String getSkyBlockID(ItemStack item) {
        if (item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if (extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }

        return null;
    }

    public static String getSBData(ItemStack item) {
        if (item != null) {
            //NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            NBTTagCompound itemNBT = item.getTagCompound();

            if (itemNBT != null && itemNBT.hasKey("ExtraAttributes", 10)) {
                NBTTagCompound extraAttributes = itemNBT.getCompoundTag("ExtraAttributes");

                if (extraAttributes.hasKey("id", 8) && extraAttributes.getString("id").equals("SACK")) {
                    NBTTagList items = extraAttributes.getTagList("items", 10);
                    //SendChat.chat("!!!!!!!");

                    for (int i = 0; i < items.tagCount(); i++) {
                        NBTTagCompound item1 = items.getCompoundTagAt(i);
                        String itemId = item1.getString("id");
                        int count = item1.getInteger("Count");

                        SendChat.chat("Item ID: " + itemId + ", Count: " + count);
                    }
                }
            }
        }

        return null;
    }
}
