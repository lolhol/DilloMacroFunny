package com.dillo.commands.RouteMakerUtils;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import static com.dillo.utils.RayTracingUtils.adjustLook;

public class CheckIfCanTpToEvery extends Command {

    public CheckIfCanTpToEvery() {
        super("canTp");
    }

    @DefaultHandler
    public void handle() {
        if (currentRoute.currentRoute.size() < 1) {
            SendChat.chat(prefix.prefix + "No route selected!");
            return;
        }

        boolean send = true;

        for (int i = 0; i < currentRoute.currentRoute.size(); i++) {
            int second = i + 1;

            if (second >= currentRoute.currentRoute.size()) {
                second = 0;
            }

            BlockPos block1 = currentRoute.currentRoute.get(i);
            BlockPos block2 = currentRoute.currentRoute.get(second);

            Vec3 nextBlockPos = adjustLook(
                    new Vec3(block1.getX(), block1.getY() + 1, block1.getZ()),
                    block2,
                    new net.minecraft.block.Block[]{
                            Blocks.stone,
                            Blocks.coal_ore,
                            Blocks.emerald_ore,
                            Blocks.diamond_ore,
                            Blocks.gold_ore,
                            Blocks.iron_ore,
                            Blocks.lapis_ore,
                            Blocks.lit_redstone_ore,
                            Blocks.redstone_ore,
                            Blocks.air,
                    },
                    true
            );

            if (nextBlockPos == null) {
                send = false;
                SendChat.chat(prefix.prefix + "Can't tp to one or more blocks! (" + (int) (i + 1) + ")");
            }
        }

        if (send) SendChat.chat(prefix.prefix + "It appears that you can tp to every block!");
    }
}
