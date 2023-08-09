package com.dillo.main.failsafes.RouteFailsafes;

import com.dillo.calls.ArmadilloStates;
import com.dillo.calls.KillSwitch;
import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RemoveBlockFailsafe {

    int curTicks = 0;

    public boolean isBlockLoaded(BlockPos block) {
        Chunk chunk = new Chunk(ids.mc.theWorld, block.getX(), block.getZ());
        return chunk.isLoaded();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!ArmadilloStates.isOnline() || curTicks < 20) {
            curTicks++;
            return;
        }

        for (BlockPos block : currentRoute.currentRoute) {
            if (!isBlockLoaded(block)) continue;

            if (ids.mc.theWorld.getBlockState(block).getBlock() == Blocks.air) {
                ArmadilloStates.offlineState = KillSwitch.OFFLINE;
                SendChat.chat(prefix.prefix + "Detected cobble missing on route! Stopping!");
                return;
            }
        }

        curTicks = 0;
    }
}
