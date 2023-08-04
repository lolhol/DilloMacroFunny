package com.dillo.main.route.MobKiller;

import static com.dillo.main.route.MobKiller.MobKillerMain.mobNames;
import static com.dillo.utils.ScoreboardUtils.GetAllPlayersTab.getAllPlayerNamesList;

import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Utils {

  public static String getWatchdogName() {
    AtomicReference<String> returnName = new AtomicReference<>("");
    ids.mc.theWorld
      .getLoadedEntityList()
      .forEach(a -> {
        if (
          !Objects.equals(a.getName(), ids.mc.thePlayer.getName()) &&
          DistanceFromTo.distanceFromTo(a.getPosition(), ids.mc.thePlayer.getPosition()) < 0.2
        ) {
          returnName.set(a.getName());
        }
      });

    return String.valueOf(returnName);
  }

  public static List<Entity> getAllNonHumanEntities(AxisAlignedBB bounding) {
    List<Entity> returnList = new ArrayList<>();
    List<Entity> entityList = ids.mc.theWorld.getEntitiesWithinAABB(Entity.class, bounding);
    List<String> playerNames = getAllPlayerNamesList();

    for (Entity entity : entityList) {
      if (
        !playerNames.contains(entity.getName()) &&
        Arrays
          .stream(mobNames)
          .anyMatch(a -> {
            return a.toLowerCase().contains(entity.getName().toLowerCase());
          }) &&
        DistanceFromTo.distanceFromTo(entity.getPosition(), ids.mc.thePlayer.getPosition()) > 0.5 &&
        !Objects.equals(getWatchdogName(), entity.getName())
      ) {
        returnList.add(entity);
      }
    }

    return returnList;
  }

  public static void clickSlot(long delay, int slot) {
    int currentItem = ids.mc.thePlayer.inventory.currentItem;
    swapToSlot.swapToSlot(slot);

    try {
      Thread.sleep(delay);
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

    swapToSlot.swapToSlot(currentItem);
  }

  public static Tools getConfigTool(int type) {
    switch (type) {
      case 0:
        return Tools.AURORA_STAFF;
      case 1:
        return Tools.FIREVEIL;
      case 2:
        return Tools.SHORTBOW;
      case 3:
        return Tools.HYPERION;
    }

    return Tools.CUSTOM;
  }
}
