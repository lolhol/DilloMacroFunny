package com.dillo.utils.previous.packets;

import com.dillo.utils.previous.random.ids;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class sendStart {

    public static void sendStartPacket(BlockPos block, EnumFacing enumFacing) {
        ids.mc.thePlayer.sendQueue.addToSendQueue(
                new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, enumFacing)
        );
        ids.mc.thePlayer.swingItem();
    }
}
