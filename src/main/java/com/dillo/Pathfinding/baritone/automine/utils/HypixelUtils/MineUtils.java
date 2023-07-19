package com.dillo.Pathfinding.baritone.automine.utils.HypixelUtils;

import com.dillo.Pathfinding.baritone.automine.utils.BlockUtils.BlockData;
import java.util.ArrayList;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import org.fusesource.jansi.Ansi;

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
