package com.dillo.commands;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.dillo.main.route.Utils.AddBlockRoute.addBlockRoute;

public class AddBlockRouteCommand extends Command {

    public AddBlockRouteCommand() {
        super("block");
    }

    @DefaultHandler
    public void handle() {
        addBlockRoute();
    }
}
