package com.dillo.main.route.Utils;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class IsAbleToMine {

    public static boolean isAbleToMine(BlockPos block) {
        Block blockType = ids.mc.theWorld.getBlockState(block).getBlock();

        if (
                blockType == Blocks.coal_ore ||
                        blockType == Blocks.lapis_ore ||
                        blockType == Blocks.iron_ore ||
                        blockType == Blocks.gold_ore ||
                        blockType == Blocks.redstone_ore ||
                        blockType == Blocks.lit_redstone_ore ||
                        blockType == Blocks.diamond_ore ||
                        blockType == Blocks.emerald_ore ||
                        blockType == Blocks.quartz_ore ||
                        blockType == Blocks.stone ||
                        blockType == Blocks.cobblestone
        ) {
            return true;
        }

        return false;
    }

    public static boolean isBlockInRoute(BlockPos block) {
        return currentRoute.currentRoute.contains(block);
    }
}
