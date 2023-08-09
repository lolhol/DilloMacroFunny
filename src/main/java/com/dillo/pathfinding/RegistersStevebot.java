package com.dillo.pathfinding;

import com.dillo.pathfinding.stevebot.core.player.PlayerUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.dillo.armadillomacro.*;

public class RegistersStevebot {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        playerInput.onEventPlayerTick();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        try {
            renderer.onEventRender(PlayerUtils.getPlayerPosition());
        } catch (NullPointerException e) {
        }
    }

    @SubscribeEvent
    public void onEvent(ConfigChangedEvent.PostConfigChangedEvent event) {
        playerInput.onEventConfigChanged();
    }

    @SubscribeEvent
    public void onEvent(TickEvent.RenderTickEvent event) {
        try {
            playerCamera.onRenderTickEvent(event.phase == TickEvent.Phase.START);
        } catch (NullPointerException e) {
        }
    }

    @SubscribeEvent
    public void onEvent(BlockEvent.PlaceEvent event) {
        blockProvider.getBlockCache().onEventBlockPlace(event.pos.getX(), event.pos.getY(), event.pos.getZ());
    }

    @SubscribeEvent
    public void onEvent(BlockEvent.BreakEvent event) {
        blockProvider.getBlockCache().onEventBlockBreak(event.pos.getX(), event.pos.getY(), event.pos.getZ());
    }
}
