package com.dillo.commands.baritone;

import com.dillo.pathfinding.baritone.automine.config.BaritoneConfig;
import com.dillo.pathfinding.baritone.automine.config.MiningType;
import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.player.PlayerUtils;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.List;

import static com.dillo.armadillomacro.pathHandler;

public class WalkToBlockWithBaritone extends Command {

    final List<Block> blocksAllowedToMine = new ArrayList<Block>() {
        {
            add(Blocks.stone);
            add(Blocks.gold_ore);
            add(Blocks.emerald_ore);
            add(Blocks.redstone_ore);
            add(Blocks.iron_ore);
            add(Blocks.coal_ore);
            add(Blocks.stained_glass_pane);
            add(Blocks.stained_glass);
            add(Blocks.air);
        }
    };
    StevebotApi api = new StevebotApi(pathHandler);

    public WalkToBlockWithBaritone() {
        super("walkBaritone");
    }

    @DefaultHandler
    public void handle(int x, int y, int z) {
    /*SendChat.chat(prefix.prefix + "Walking!");
    BlockPos blockToWalk = new BlockPos(x, y, z);
    autoMineBaritone.goTo(blockToWalk);*/

        api.path(new BaseBlockPos(PlayerUtils.getPlayerBlockPos()), new BaseBlockPos(x, y, z), true, false);
    }

    private BaritoneConfig getMineBehaviour() {
        return new BaritoneConfig(MiningType.DYNAMIC, false, true, false, 200, 8, null, blocksAllowedToMine, 256, 256);
    }
}
