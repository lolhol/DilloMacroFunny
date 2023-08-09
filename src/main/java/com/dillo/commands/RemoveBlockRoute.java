package com.dillo.commands;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.files.readwrite.ReWriteFile;
import com.dillo.utils.previous.chatUtils.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class RemoveBlockRoute extends Command {

    public RemoveBlockRoute() {
        super("removeBlock");
    }

    @DefaultHandler
    public void handle(int number) {
        if (number - 1 < currentRoute.currentRoute.size()) {
            currentRoute.currentRoute.remove(number - 1);
            ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
            SendChat.chat(prefix.prefix + "Block removed!");
        } else {
            SendChat.chat(prefix.prefix + "Please provide a valid number on route.");
        }
    }
}
