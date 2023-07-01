package com.dillo.dilloUtils.RouteUtils.Nuker;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker;
import com.dillo.keybinds.Keybinds;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import net.minecraft.util.BlockPos;

import java.util.List;

public class StartNuker {
    public static void startNuker() {
        if (currentRoute.currentRoute.size() > 1) {
            SendChat.chat(prefix.prefix + "Started Nuker!");
            GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "nuker");
        } else {
            Keybinds.isNuking = false;
            SendChat.chat(prefix.prefix + "No route selected! :L ");
        }
    }

    public static void stopNuker() {
        NukerMain.startNuking = false;
        NukerMain.broken.clear();
        NukerMain.nuking.clear();
        SendChat.chat(prefix.prefix + "Stopped nuker!");
    }
}
