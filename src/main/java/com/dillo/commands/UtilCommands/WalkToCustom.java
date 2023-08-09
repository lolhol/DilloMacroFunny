package com.dillo.commands.UtilCommands;

import com.dillo.pathfinding.stevebot.core.StevebotApi;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class WalkToCustom extends Command {

    public static boolean startRender = false;
    StevebotApi api;

    public WalkToCustom(StevebotApi api) {
        super("walkToCustom");
        this.api = api;
    }

    @DefaultHandler
    public void handle() {
        ids.mc.thePlayer.inventory.currentItem = 1;
    }
}
