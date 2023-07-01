package com.dillo.commands.UtilCommands;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.dillo.dilloUtils.RouteChecker.CheckForStruc.isObstructed;

public class RouteChecker extends Command {
    public RouteChecker() {
        super("checkRoute");
    }

    @DefaultHandler
    public void handle() {
        boolean result = isObstructed();
        SendChat.chat(result ? prefix.prefix + "The route appears to be unobstructed!" : prefix.prefix + "There is a high chance that the route is obstructed!");
    }
}
