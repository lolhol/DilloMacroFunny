package com.dillo.commands.RouteCommands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class DeleteRoute extends Command {

    public DeleteRoute() {
        super("deleteRoute");
    }

    @DefaultHandler
    public void handle() {
        if (currentRoute.currentRouteFile != null) {
            currentRoute.currentRouteFile.delete();
            currentRoute.currentRoute.clear();
            currentRoute.strucList.clear();
            currentRoute.currentRouteFile = null;
            currentRoute.currentRouteSelected = null;
            currentRoute.currentBlock = null;

            SendChat.chat(prefix.prefix + "Route deleted.");
        } else {
            SendChat.chat(prefix.prefix + "No route selected :/. Select it then run command again.");
        }
    }
}
