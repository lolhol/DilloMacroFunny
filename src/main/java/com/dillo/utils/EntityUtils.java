package com.dillo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class EntityUtils {

    public static Entity isSummoned() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        AxisAlignedBB boundingBox = new AxisAlignedBB(
                player.posX - 3,
                player.posY - 3,
                player.posZ - 3,
                player.posX + 3,
                player.posY + 3,
                player.posZ + 3
        );

        List<Entity> entityList = mc.theWorld.getEntitiesWithinAABB(Entity.class, boundingBox);
        for (Entity entity : entityList) {
            if (!(entity instanceof EntityPlayer)) {
                if (entity.getName().contains(player.getName())) {
                    return entity;
                }
            }
        }

        return null;
    }
}
