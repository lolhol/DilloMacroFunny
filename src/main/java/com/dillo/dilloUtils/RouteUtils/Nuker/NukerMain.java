package com.dillo.dilloUtils.RouteUtils.Nuker;

import static com.dillo.data.config.nukerRange;
import static com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker.Blockss;
import static com.dillo.dilloUtils.RouteUtils.Utils.IsAbleToMine.isAbleToMine;
import static com.dillo.dilloUtils.Utils.GetOnArmadillo.isSummoned;
import static com.dillo.utils.RayTracingUtils.adjustLook;

import com.dillo.data.config;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class NukerMain {

  public static List<BlockPos> nuking = new ArrayList<BlockPos>();
  public static List<BlockPos> broken = new ArrayList<>();
  public static boolean startNuking = false;
  public static BlockPos curBlock = null;
  private static int currTicks = 0;
  private static long lastTime = System.currentTimeMillis();

  public static void nukeBlocks(List<BlockPos> blocksToNuke, boolean nuke) {
    nuking = blocksToNuke;
    startNuking = nuke;
    lastTime = System.currentTimeMillis();
  }

  public static void nukerStart() {
    nukeBlocks(Blockss, true);
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (startNuking) {
      if (currTicks >= 30) {
        while (broken.size() > 0) {
          BlockPos block = broken.get(0);

          if (isAbleToMine(block)) {
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

          if (!isAbleToMine(block)) {
            nuking.remove(block);
            broken.add(block);
          } else {
            if (
              isInFOV(block, config.nukerFOV) &&
              isHoldingDrill() &&
              DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), block) < nukerRange + 0.1 &&
              isOnGround() &&
              isAbleToMine(block) &&
              isInDillo()
            ) {
              if (config.nukerUnObstructedChecks && !canBeBroken(block)) {
                return;
              }

              sendStart.sendStartPacket(block, EnumFacing.fromAngle(ids.mc.thePlayer.rotationYaw));
              nuking.remove(block);
              broken.add(block);
            }
          }
        }
      } else {
        startNuking = false;
        SendChat.chat(prefix.prefix + "The route is clear!");
      }

      if (curBlock != null) {
        RenderBox.drawBox(curBlock.getX(), curBlock.getY(), curBlock.getZ(), Color.red, 0.2F, event.partialTicks);
      }
    }
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
    Float result = getYawBlock(blockPos);
    return result < fov / 2 || result > 360 - fov / 2;
  }

  public static Float getYawBlock(BlockPos block) {
    double dX = block.getX() + 0.5 - ids.mc.thePlayer.posX;
    double dZ = block.getZ() + 0.5 - ids.mc.thePlayer.posZ;

    double angle = Math.atan2(dZ, dX);
    float rotationYaw = (float) Math.toDegrees(angle) - 90.0f;

    if (rotationYaw < 0.0f) {
      rotationYaw += 360.0f;
    }

    float playerYaw = ids.mc.thePlayer.rotationYaw;

    if (ids.mc.thePlayer.rotationYaw > 360) {
      playerYaw =
        (float) (((ids.mc.thePlayer.rotationYaw / 360) - (Math.floor(ids.mc.thePlayer.rotationYaw / 360))) * 360);
    }

    rotationYaw = Math.abs(playerYaw - rotationYaw);

    if (rotationYaw > 360) {
      rotationYaw = (float) (((playerYaw / 360) - (Math.floor(playerYaw / 360))) * 360);
    }

    return Math.abs(rotationYaw);
  }

  public static boolean canBeBroken(BlockPos block) {
    Vec3 blockHit = adjustLook(
      ids.mc.thePlayer.getPosition(),
      block,
      new net.minecraft.block.Block[] { Blocks.air },
      false
    );
    return blockHit != null;
  }

  public static Float getYawBlockAround(BlockPos block, float curRotationYaw) {
    double dX = block.getX() + 0.5 - ids.mc.thePlayer.posX;
    double dZ = block.getZ() + 0.5 - ids.mc.thePlayer.posZ;

    double angle = Math.atan2(dZ, dX);
    float rotationYaw = (float) Math.toDegrees(angle) - 90.0f;

    if (rotationYaw < 0.0f) {
      rotationYaw += 360.0f;
    }

    rotationYaw = Math.abs(curRotationYaw - rotationYaw);

    if (rotationYaw > 360) {
      rotationYaw = (float) (((curRotationYaw / 360) - (Math.floor(curRotationYaw / 360))) * 360);
    }

    return Math.abs(rotationYaw);
  }
}
