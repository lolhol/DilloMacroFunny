package com.dillo.utils;

import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class GetEntity {

  public static ArrayList<Entity> getAllEntitiesInRange(String name) {
    ArrayList<Entity> entities = new ArrayList<>();
    for (Entity entity1 : (ids.mc.theWorld.loadedEntityList)) {
      if (entity1 instanceof EntityArmorStand) {
        if (entity1.getName().contains(name)) {
          entities.add(entity1);
        }
      }
    }

    return entities;
  }
}
