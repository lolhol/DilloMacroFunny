package com.dillo.dilloUtils.RouteUtils.PlaceBlocks;

import static com.dillo.data.config.isNukerPlaceCobble;
import static com.dillo.dilloUtils.FailSafes.UsePathfinderInstead.getAdj;
import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.*;
import static com.dillo.dilloUtils.StateDillo.isDilloSummoned;

import com.dillo.Events.MillisecondEvent;
import com.dillo.Events.PlayerMoveEvent;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.LookAt;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderPoints;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlaceCobbleModule {

  boolean isStart = false;
  int msCount = 0;
  Vec3 grain = null;
  boolean isPlaced = false;
  int tickCount = 0;
  List<BlockPos> placed = new ArrayList<>();

  @SubscribeEvent
  public void onMillisecond(MillisecondEvent event) {
    if (!isNukerPlaceCobble || currentRoute.currentRoute.size() < 1/*|| !startNuking*/) {
      return;
    }

    if (msCount < 50) {
      msCount++;
      return;
    } else {
      msCount = 0;
    }

    BlockPos closestBlock = closestBlockRoute();

    if (
      DistanceFromTo.distanceFromTo(closestBlock, ids.mc.thePlayer.getPosition()) < 5 &&
      ids.mc.theWorld.getBlockState(closestBlock).getBlock() == Blocks.air
    ) {
      if (startNuking) {
        pauseNuker();
      }

      RenderPoints.renderPoint(null, 0.1, false);
      grain = getAdj(closestBlock);

      if (grain != null) {
        isStart = true;

        if (!placed.contains(closestBlock) && !isDilloSummoned()) {
          placed.add(closestBlock);

          new Thread(() -> {
            try {
              Thread.sleep(200);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }

            ids.mc.playerController.onPlayerRightClick(
              ids.mc.thePlayer,
              ids.mc.theWorld,
              ids.mc.thePlayer.inventory.getCurrentItem(),
              closestBlock,
              EnumFacing.UP,
              grain
            );

            unpauseNuker();
          })
            .start();
        }
      }
    } else {
      isStart = false;
    }
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onUpdatePre(PlayerMoveEvent.Pre pre) {
    if (!isNukerPlaceCobble || !isStart) return;
    if (grain != null) {
      LookAt.look(LookAt.getRotation(grain));
    }
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (isNukerPlaceCobble) {
      if (tickCount < 20) {
        tickCount++;
        return;
      } else {
        tickCount = 0;
      }

      placed.removeIf(a -> {
        return ids.mc.theWorld.getBlockState(a).getBlock() == Blocks.air;
      });
    }
  }

  private BlockPos closestBlockRoute() {
    BlockPos curClose = currentRoute.currentRoute.get(0);
    double curSmall = DistanceFromTo.distanceFromTo(ids.mc.thePlayer.getPosition(), currentRoute.currentRoute.get(0));

    for (BlockPos block : currentRoute.currentRoute) {
      double cur = DistanceFromTo.distanceFromTo(block, ids.mc.thePlayer.getPosition());

      if (curSmall > cur) {
        curSmall = cur;
        curClose = block;
      }
    }

    return curClose;
  }
}
