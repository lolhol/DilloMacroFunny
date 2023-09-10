package com.dillo.pathfinding.mit.finder.mobs;

import com.dillo.pathfinding.mit.finder.walker.event.DoneWalking;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KillMobs {

  //TODO: soon (TM);
  public boolean isEnabled = false;
  public int count = 0;
  public Entity entityType = null;
  public HashSet<Entity> cantPathFindTo = new HashSet<>();
  int total = 20;
  boolean isWalking = false;

  final Utils Utils = new Utils();

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isEnabled) return;
    if (count <= total) {
      count++;
      return;
    } else {
      count = 0;
    }

    if (isWalking) {
      return;
    }

    List<Entity> list = Utils.getMobsAround();
    if (!list.isEmpty()) {
      Entity ent = Utils.getClosestEntity(list);
      total = 20;
    } else {
      total = 100;
    }
  }

  @SubscribeEvent
  public void onDoneWalking(DoneWalking event) {
    isWalking = !isWalking;
  }
}
