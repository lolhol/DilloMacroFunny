package com.dillo.pathfinding.baritone.automine.utils.HypixelUtils;

import com.dillo.pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

import java.util.ArrayList;

public class MineUtils {

    public static EnumDyeColor[] gemPriority = {
            null,
            EnumDyeColor.RED,
            EnumDyeColor.PURPLE,
            EnumDyeColor.LIME,
            EnumDyeColor.LIGHT_BLUE,
            EnumDyeColor.ORANGE,
            EnumDyeColor.YELLOW,
            EnumDyeColor.MAGENTA,
    };

    public static ArrayList<BlockData<?>> getMithrilColorBasedOnPriority(int priority) {
        switch (priority) {
            case 0:
                return new ArrayList<BlockData<?>>() {
                    {
                        add(new BlockData<>(Blocks.stained_hardened_clay, null));
                        add(new BlockData<>(Blocks.wool, EnumDyeColor.GRAY));
                    }
                };
            case 1:
                return new ArrayList<BlockData<?>>() {
                    {
                        add(new BlockData<>(Blocks.prismarine, null));
                    }
                };
            case 2:
                return new ArrayList<BlockData<?>>() {
                    {
                        add(new BlockData<>(Blocks.wool, EnumDyeColor.LIGHT_BLUE));
                    }
                };
            case 3:
                return new ArrayList<BlockData<?>>() {
                    {
                        add(
                                new BlockData<>(
                                        Blocks.stone
                                                .getDefaultState()
                                                .withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH)
                                                .getBlock(),
                                        BlockStone.EnumType.DIORITE_SMOOTH
                                )
                        );
                    }
                };
            default:
                return null;
        }
    }
}
