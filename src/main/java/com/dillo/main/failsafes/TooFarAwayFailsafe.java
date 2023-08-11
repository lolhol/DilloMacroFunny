package com.dillo.main.failsafes;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.DistanceFromTo;
import com.dillo.utils.previous.random.ids;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.config.config.withinBlockRadiusChecks;

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
                } catch (NullPointerException e) {
                }
            }
        }
    }
}
