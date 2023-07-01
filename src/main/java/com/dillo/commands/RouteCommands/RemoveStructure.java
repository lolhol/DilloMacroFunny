package com.dillo.commands.RouteCommands;

import com.dillo.dilloUtils.BlockUtils.fileUtils.ReWriteFile;
import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class RemoveStructure extends Command {
    public RemoveStructure() {
        super("removeStructure");
    }

    @DefaultHandler
    public void handle(int point) {
        if (currentRoute.strucList.size() < point + 1) {
            currentRoute.strucList.remove(point + 1);
            ReWriteFile.reWriteFile(currentRoute.currentRouteFile);
            SendChat.chat(prefix.prefix + "Removed structure.");
        } else {
            SendChat.chat(prefix.prefix + "Please provide a valid point.");
            SendChat.chat(prefix.prefix + "If you do not know how to see the points, run /structurePoints.");
        }
    }
}
