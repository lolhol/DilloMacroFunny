package com.dillo.main.route.MobKiller;

import static com.dillo.main.route.MobKiller.Utils.clickSlot;
import static com.dillo.main.route.MobKiller.Utils.getAllNonHumanEntities;
import static com.dillo.main.utils.looks.LookAt.*;
import static com.dillo.utils.GetSBItems.getCustomSlot;
import static com.dillo.utils.GetSBItems.getFireVeilSlot;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.CurrentState;
import com.dillo.config.config;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderMultipleBlocksMod;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobKillerMain {

  public static String[] mobNames = new String[] {
    "sludge",
    "jungle key guardian",
    "butterfly",
    "Yog",
    "Blaze",
    "Bal",
    "Scatha Worm",
    "Automaton",
    "Grunt",
    "Executive Sebastian",
    "Executive Wendy",
    "Executive Viper",
    "Boss Corleone",
    "Thyst",
    "Sneaky Creeper",
    "Lapis Zombie",
    "Redstone Pigman",
    "Emerald Slime",
    "Miner Zombie",
    "Miner Skeleton",
    "Goblin",
    "Water Worm",
    "Poisoned Water Worm",
    "Flaming Worm",
    "Zombie Miner",
    "Lava Blaze",
    "Lava Pigman",
    "Weakling",
    "goblin",
    "Treasurite",
  };

  // Will add more mobs in the future, for now this is all I have (hypixel wiki) if u want me to add more dm me all the mob names :O :D
  boolean start;

  public void killMobsAround(float range, CurrentState newState) {
    start = true;

    //d

    Vec3 playerVec = ids.mc.thePlayer.getPositionVector();

    List<Entity> allEntities = getAllNonHumanEntities(
      new AxisAlignedBB(
        playerVec.xCoord - range,
        playerVec.yCoord - range,
        playerVec.zCoord - range,
        playerVec.xCoord + range,
        playerVec.yCoord + range,
        playerVec.zCoord + range
      )
    );

    if (allEntities.size() < 1) {
      ArmadilloStates.currentState = newState;
      return;
    }

    Tools tool = Utils.getConfigTool(config.killerWeapon);

    new Thread(() -> {
      switch (tool) {
        case CUSTOM:
          start = true;

          for (int i = 0; i < allEntities.size(); i++) {
            if (!ArmadilloStates.isOnline()) {
              ArmadilloStates.currentState = newState;
              return;
            }

            serverSmoothLook(LookAt.getRotation(allEntities.get(i).getPosition().add(0, 1, 0)), 200);

            sleep(200);

            int slot = getCustomSlot(config.customWeapon);

            if (slot != -1) {
              clickSlot(100, slot);

              sleep(100);
            } else {
              break;
            }

            reset();
          }

          ArmadilloStates.currentState = newState;
          return;
        case SHORTBOW:
        case AURORA_STAFF:
          start = true;

          RenderMultipleBlocksMod.renderMultipleBlocks(null, false);

          for (Entity allEntity : allEntities) {
            if (!ArmadilloStates.isOnline()) {
              ArmadilloStates.currentState = newState;
              return;
            }

            RenderMultipleBlocksMod.renderMultipleBlocks(allEntity.getPositionVector(), true);

            serverSmoothLook(LookAt.getRotation(allEntity.getPosition().add(0, 1, 0)), 200);

            sleep(200);

            int slot = getCustomSlot(tool == Tools.AURORA_STAFF ? "runic_staff" : "shortbow");

            if (slot != -1) {
              clickSlot(100, slot);
              sleep(100);
            }

            reset();

            if (tool == Tools.AURORA_STAFF) {
              sleep(2000);
            } else {
              sleep(200);
            }
          }

          ArmadilloStates.currentState = newState;
          return;
        case FIREVEIL:
          int slot = getFireVeilSlot();

          if (slot != -1) {
            clickSlot(100, slot);

            sleep(500);

            clickSlot(100, slot);

            sleep(100);
          }

          ArmadilloStates.currentState = newState;
          return;
        case HYPERION:
          slot = getCustomSlot("hype");

          if (slot != -1) {
            float prev = ids.mc.thePlayer.cameraPitch;

            LookAt.smoothLook(new Rotation(0, ids.mc.thePlayer.cameraYaw), 200);

            sleep(200);

            clickSlot(100, getCustomSlot("hype"));

            sleep(100);

            LookAt.smoothLook(new Rotation(prev, ids.mc.thePlayer.cameraYaw), 200);

            sleep(200);
          }

          ArmadilloStates.currentState = newState;
          return;
      }
    })
      .start();
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!start) return;
    updateServerLook();
  }

  private void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
