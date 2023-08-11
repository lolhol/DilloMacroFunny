package com.dillo.utils;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;

import java.util.Objects;

public class CheckCurrRoute {

    public static boolean checkCurrRoute(String name) {
        if (!Objects.equals(currentRoute.currentRouteSelected, name)) {
            SendChat.chat(prefix.prefix + "Please select the route you want or run /helpMIT for help.");
            return false;
        } else {
            return true;
        }
    }
}
