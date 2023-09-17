package com.dillo.pathfinding.mit.finder.mods.killmobs;

import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;

public class Utils {

  public List<Entity> getMobsAround() {
    return ids.mc.theWorld.loadedEntityList
      .stream()
      .filter(a -> {
        return a instanceof EntityZombie;
      })
      .collect(Collectors.toList());
  }

  public Entity getClosestEntity(List<Entity> all) {
    double closest = 99999999;
    Entity bestE = null;

    for (Entity ent : all) {
      double dist = DistanceFromTo.distanceFromTo(ent.getPositionVector(), ids.mc.thePlayer.getPositionVector());

      if (dist < closest) {
        closest = dist;
        bestE = ent;
      }
    }

    return bestE;
  }

  public void pathfindToEntity() {}

  public void goToEntity() {}
}
