package com.dillo.main.failsafes.RestartMacroUtils;

import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.HashSet;
import java.util.List;

public class BestBlocks {

    public static List<BlockPos> bestBlocks(List<BlockPos> initialBlocks) {
        BlockPos playerPos = new BlockPos(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY, ids.mc.thePlayer.posZ);

        initialBlocks.sort((a, b) -> {
            return getPercentages(playerPos, a) > getPercentages(playerPos, b) ? -1 : 1;
        });

        return initialBlocks;
    }

    public static double getPercentages(BlockPos block1, BlockPos block2) {
        HashSet<BlockPos> alrChecked = new HashSet<BlockPos>();
        int notAir = 0;
        int allBlocks = 0;

        double dx = block1.getX() - block2.getX();
        double dy = block1.getY() - block2.getY();
        double dz = block1.getZ() - block2.getZ();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        double stepX = dx / length;
        double stepY = dy / length;
        double stepZ = dz / length;

        for (int i = 0; i < length; i++) {
            double newX = stepX * i + block2.getX();
            double newY = stepY * i + block2.getY();
            double newZ = stepZ * i + block2.getZ();

            for (int m = -1; m <= 1; m++) {
                for (int k = -1; k <= 1; k++) {
                    for (int j = -1; j <= 1; j++) {
                        BlockPos block = new BlockPos(newX + m, newY + j, newZ + k);
                        Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

                        if (!alrChecked.contains(block)) {
                            if (blockType != Blocks.air) {
                                notAir++;
                                alrChecked.add(block);
                            }

                            allBlocks++;
                        }
                    }
                }
            }
        }

        return getPercent(allBlocks, notAir);
    }

    public static double getPercent(double x, double y) {
        return (100 / x) * y;
    }
}
