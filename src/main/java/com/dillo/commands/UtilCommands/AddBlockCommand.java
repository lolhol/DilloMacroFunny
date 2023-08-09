package com.dillo.commands.UtilCommands;

import com.dillo.utils.previous.random.ids;
import com.dillo.utils.renderUtils.renderModules.RenderOneBlockMod;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.util.Vec3;

public class AddBlockCommand extends Command {

    public static boolean render = false;

    public AddBlockCommand() {
        super("addBlock");
    }

    @DefaultHandler
    public void handle() {
        render = !render;

        if (render) {
            RenderOneBlockMod.renderOneBlock(
                    new Vec3(ids.mc.thePlayer.posX, ids.mc.thePlayer.posY - 1, ids.mc.thePlayer.posZ),
                    true
            );
        } else {
            RenderOneBlockMod.renderOneBlock(null, true);
        }
    }
}
