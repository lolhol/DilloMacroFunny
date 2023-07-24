package com.dillo.main.route.MobKiller;

import static com.dillo.utils.ScoreboardUtils.GetAllPlayersTab.getAllPlayerNamesList;

import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.swapToSlot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Utils {

  public static List<Entity> getAllNonHumanEntities(AxisAlignedBB bounding) {
    List<Entity> returnList = new ArrayList<>();
    List<Entity> entityList = ids.mc.theWorld.getEntitiesWithinAABB(Entity.class, bounding);
    List<String> playerNames = getAllPlayerNamesList();

    for (Entity entity : entityList) {
      if (!playerNames.contains(entity.getName())) {
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
