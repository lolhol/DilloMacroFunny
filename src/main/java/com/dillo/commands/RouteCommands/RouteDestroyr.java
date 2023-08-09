package com.dillo.commands.RouteCommands;

import com.dillo.main.route.RouteDeletr.RouteDeletrConfig;
import com.dillo.main.route.RouteDeletr.RouteDeletrMain;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class RouteDestroyr extends Command {

    boolean routeDestroy = false;
    RouteDeletrMain deletr;

    public RouteDestroyr(RouteDeletrMain destroyer) {
        super("destroyRoute");
        deletr = destroyer;
    }

    @DefaultHandler
    public void destroyRoute() {
        routeDestroy = !routeDestroy;

        String str = routeDestroy ? "Started Destroying!" : "Stopped Destroying";
        SendChat.chat(prefix.prefix + str);

        RouteDeletrConfig config = new RouteDeletrConfig(
                true,
                true,
                true,
                ids.mc.playerController.getBlockReachDistance(),
                20,
                30,
                100
        );

        deletr.startStop(config, routeDestroy);
    }
}
