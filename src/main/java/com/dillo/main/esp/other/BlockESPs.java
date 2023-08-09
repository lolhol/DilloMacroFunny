package com.dillo.main.esp.other;

import com.dillo.config.config;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.DistFromXPlayer;
import com.dillo.utils.renderUtils.RenderBox;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockESPs {

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (config.renderRoute) {
            for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
                if (
                        DistFromXPlayer.distFromXPlayer(
                                currentRoute.currentRoute.get(i).getX(),
                                currentRoute.currentRoute.get(i).getY(),
                                currentRoute.currentRoute.get(i).getZ()
                        ) <
                                config.routeDist
                ) {
                    RenderBox.drawBox(
                            currentRoute.currentRoute.get(i).getX(),
                            currentRoute.currentRoute.get(i).getY(),
                            currentRoute.currentRoute.get(i).getZ(),
                            Color.BLUE,
                            0.5f,
                            event.partialTicks,
                            false
                    );
                }
            }
        }
    }
}
