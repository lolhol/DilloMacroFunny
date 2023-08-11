package com.dillo.utils.previous.random;

import net.minecraft.util.BlockPos;

public class IsSameBlock {

    public static boolean isSameBlock(BlockPos defBlock, BlockPos newBlock) {
        return (
                Math.abs(defBlock.getX() - newBlock.getX()) < 0.0001 &&
                        Math.abs(defBlock.getY() - newBlock.getY()) < 0.0001 &&
                        Math.abs(defBlock.getZ() - newBlock.getZ()) < 0.00001
        );
    }
}
