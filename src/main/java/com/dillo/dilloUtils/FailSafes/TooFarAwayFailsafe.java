package com.dillo.dilloUtils.FailSafes;

import static com.dillo.data.config.withinBlockRadiusChecks;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.ArmadilloMain.KillSwitch;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TooFarAwayFailsafe {

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (currentRoute.currentRoute.size() > 0 && withinBlockRadiusChecks) {
        double distance = 100000000;

        try {
          for (BlockPos point : currentRoute.currentRoute) {
            double dist = DistanceFromTo.distanceFromTo(point, ids.mc.thePlayer.getPosition());

            if (dist < distance) {
              distance = dist;
            }
          }

          if (distance > 240) {
            ArmadilloStates.currentState = null;
            ArmadilloStates.offlineState = KillSwitch.OFFLINE;
          }
        } catch (NullPointerException e) {}
      }
    }
  }
}
