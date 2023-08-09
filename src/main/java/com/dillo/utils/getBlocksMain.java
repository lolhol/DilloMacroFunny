package com.dillo.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class getBlocksMain {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static List<BlockPos> getBlocksMain() {
        List<BlockPos> blocks = new ArrayList<>();
        int radius = 1;
        int x = (int) mc.thePlayer.posX;
        int y = (int) mc.thePlayer.posY;
        int z = (int) mc.thePlayer.posZ;
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - 4; j <= y + 2; j++) {
                for (int k = z - radius; k <= z + radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    IBlockState block = mc.theWorld.getBlockState(pos);

                    if (block.getBlock() == Blocks.stained_glass || block.getBlock() == Blocks.stained_glass_pane) {
                        blocks.add(pos);
                    }
                }
            }
        }
        return blocks;
    }
}
