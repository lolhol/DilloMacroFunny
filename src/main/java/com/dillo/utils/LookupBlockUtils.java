package com.dillo.utils;
import com.dillo.utils.previous.random.ids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * @author Gabagooooooooooool
 * @version 1.2
 * Util for verifying stacked blocks integrity.
 */
public class LookupBlockUtils {
    public static boolean blocksAbove(BlockPos baseBlock, Block[] blockList, BlockStone.EnumType[] stoneParameters) {
        int x = baseBlock.getX();
        int y = baseBlock.getY();
        int z = baseBlock.getZ();
        for (int i = 0; i < blockList.length; i++) {
            if (ids.mc.theWorld.getBlockState(new BlockPos(x, y + i, z)).getBlock() == blockList[i]) {
                if (stoneParameters[i] != null && (ids.mc.theWorld.getBlockState(new BlockPos(x, y + i, z)).getBlock() == Blocks.stone)) {
                    if (ids.mc.theWorld.getBlockState(new BlockPos(x, y + i, z)).getValue(BlockStone.VARIANT) != stoneParameters[i])
                        return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}