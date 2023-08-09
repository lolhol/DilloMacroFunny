package com.dillo.commands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.files.readwrite.ClearRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class ClearBlockRoute extends Command {

    public ClearBlockRoute() {
        super("clear");
    }

    @DefaultHandler
    public void handle() {
        if (currentRoute.currentRouteSelected != null) {
            ClearRoute.clearBlockRoute(currentRoute.currentRouteFile);
            SendChat.chat(prefix.prefix + "Route cleared successfully!");
        } else {
            SendChat.chat(prefix.prefix + "Please select a route or run /helpMIT!");
        }
    }
}
