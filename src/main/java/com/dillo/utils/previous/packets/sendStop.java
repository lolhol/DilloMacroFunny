package com.dillo.utils.previous.packets;

import com.dillo.utils.previous.random.ids;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class sendStop {

    public static void sendStopPacket(BlockPos block, EnumFacing enumFacing) {
        ids.mc.thePlayer.sendQueue.addToSendQueue(
                new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, enumFacing)
        );
    }
}
