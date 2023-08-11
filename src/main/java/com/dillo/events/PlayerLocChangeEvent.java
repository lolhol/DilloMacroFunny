package com.dillo.events;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerLocChangeEvent extends Event {

    public BlockPos newPos;

    public PlayerLocChangeEvent(BlockPos newBlock) {
        newPos = newBlock;
    }
}
