package com.dillo.main.route.Nuker;

import static com.dillo.config.config.nukerRange;
import static com.dillo.config.config.nukerServerRotations;
import static com.dillo.keybinds.Keybinds.isNuking;
import static com.dillo.main.route.AutoSetup.SetupMain.isAutoSetupOn;
import static com.dillo.main.route.Utils.GetBlocksForNuker.Blockss;
import static com.dillo.main.route.Utils.IsAbleToMine.isAbleToMine;
import static com.dillo.main.route.Utils.IsAbleToMine.isBlockInRoute;
import static com.dillo.main.utils.GetMostOptimalPath.centerBlock;
import static com.dillo.main.utils.GetMostOptimalPath.getYawNeededVec;
import static com.dillo.main.utils.looks.LookAt.reset;
import static com.dillo.main.utils.looks.LookAt.updateServerLook;
import static com.dillo.main.utils.looks.LookYaw.curRotation;
import static com.dillo.utils.EntityUtils.isSummoned;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.config.config;
import com.dillo.events.PlayerMoveEvent;
import com.dillo.events.SecondEvent;
import com.dillo.main.utils.looks.LookAt;
import com.dillo.utils.BlockUtils;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.packets.sendStart;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.RenderBox;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class NukerMain {

  public static List<BlockPos> nuking = new ArrayList<BlockPos>();
  public static List<BlockPos> broken = new ArrayList<>();
  public static boolean startNuking = false;
  public static BlockPos curBlock = null;
  private static int currTicks = 0;
  public static boolean isStartLook = false;
  private static boolean isAlrLooked = false;
  private static long lastTime = System.currentTimeMillis();
  private static boolean isAutoSetup = false;
  private static int nukesPerSecond = 0;
  public static int prev = 100;
  boolean earlyCheck = false;
  int secondCount = 0;

  public static void nukeBlocks(List<BlockPos> blocksToNuke, boolean nuke) {
    nuking = blocksToNuke;
    startNuking = nuke;
    lastTime = System.currentTimeMillis();
  }

  public static void startAutoSetupNuker(List<BlockPos> blocksToNuke, boolean nuke) {
    nuking = blocksToNuke;
    startNuking = nuke;
    lastTime = System.currentTimeMillis();
    isAutoSetup = true;
  }

  public static void nukerStart() {
    nukeBlocks(Blockss, true);
  }

  public static void pauseNuker() {
    startNuking = false;
  }

  public static void unpauseNuker() {
    startNuking = true;
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (startNuking) {
      if (currTicks >= 30) {
        while (broken.size() > 0) {
          BlockPos block = broken.get(0);

          if (isAbleToMine(block) && !isBlockInRoute(block)) {
            nuking.add(0, block);
          }

          broken.remove(0);
        }

        currTicks = 0;
      } else {
        currTicks++;
      }
    }
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (startNuking) {
      if (nuking.size() > 0) {
        if (System.currentTimeMillis() >= lastTime + (1000 / config.nukerBPS)) {
          lastTime = System.currentTimeMillis();

          BlockPos block = nuking.get(0);
          curBlock = block;

          if (!isAbleToMine(block) || isBlockInRoute(block)) {
            nuking.remove(block);
            broken.add(block);
          } else {
            if (
              isInFOV(block, config.nukerFOV) && isHoldingDrill() && isOnGround() && isAbleToMine(block) && isInDillo()
            ) {
              if (DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), block) > nukerRange) {
                earlyCheck = true;
                return;
              }

              if (config.nukerUnObstructedChecks && !canBeBroken(block)) {
                return;
              }

              if (isAlrLooked) {
                isAlrLooked = false;
                reset();
              }

              if (nukerServerRotations) {
                isAlrLooked = true;
                startLook(block);
              }

              sendStart.sendStartPacket(block, EnumFacing.fromAngle(ids.mc.thePlayer.rotationYaw));
              nuking.remove(block);
              broken.add(block);

              nukesPerSecond++;
            }
          }
        }
      } else {
        startNuking = false;
        NukerMain.broken.clear();
        isNuking = false;
        NukerMain.nuking.clear();
        SendChat.chat(prefix.prefix + "The route is clear!");
      }

      if (curBlock != null) {
        RenderBox.drawBox(curBlock.getX(), curBlock.getY(), curBlock.getZ(), Color.red, 0.2F, event.partialTicks, true);
      }

      if (nuking.size() > 1) {
        if (!nuking.get(1).equals(curBlock)) {
          RenderBox.drawBox(
            nuking.get(1).getX(),
            nuking.get(1).getY(),
            nuking.get(1).getZ(),
            Color.white,
            0.2F,
            event.partialTicks,
            false
          );
        }
      }
    }
  }

  @SubscribeEvent
  public void onSecond(SecondEvent event) {
    if (!isAutoSetup || !startNuking) {
      return;
    }

    if (secondCount <= 2) {
      secondCount++;
      return;
    }

    prev = nukesPerSecond;
    nukesPerSecond = 0;
    secondCount = 0;
  }

  public int getNukesPerSecond() {
    return prev;
  }

  public boolean isDone() {
    return (
      DistanceFromTo.distanceFromTo(nuking.get(0), ids.mc.thePlayer.getPosition()) > nukerRange && broken.size() < 1
    );
  }

  public static void startLook(BlockPos block) {
    LookAt.serverSmoothLook(LookAt.getRotation(centerBlock(block)), (long) (1000 / config.nukerBPS) - 10);
    isStartLook = true;
  }

  public static boolean isInDillo() {
    Entity dillo = isSummoned();
    return dillo == null || DistanceFromTo.distanceFromTo(dillo.getPosition(), ids.mc.thePlayer.getPosition()) > 2;
  }

  public static boolean isOnGround() {
    return Math.abs(Math.round(ids.mc.thePlayer.posY) - ids.mc.thePlayer.posY) < 0.00001;
  }

  public static boolean isHoldingDrill() {
    ItemStack name = ids.mc.thePlayer.inventory.getCurrentItem();

    if (name != null) {
      String stringName = name.getDisplayName();

      if (name != null) {
        if (
          stringName.toLowerCase().contains("drill") ||
          stringName.toLowerCase().contains("pickaxe") ||
          stringName.toLowerCase().contains("gauntlet")
        ) {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean isInFOV(BlockPos blockPos, int fov) {
    Float fovBlock = Math.abs(
      getYawNeededVec(BlockUtils.fromBlockPosToVec3(blockPos).addVector(0.5, 0.5, 0.5), curRotation())
    );

    if (!isAutoSetupOn) {
      return fovBlock < fov;
    } else {
      return true;
    }
  }

  public static boolean canBeBroken(BlockPos block) {
    Vec3 blockHit = adjustLook(
      ids.mc.thePlayer.getPositionVector(),
      block,
      new net.minecraft.block.Block[] { Blocks.air },
      false
    );

    if (!isAutoSetupOn) {
      return blockHit != null;
    } else {
      return true;
    }
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isStartLook || !startNuking) return;
    updateServerLook();
  }
}
