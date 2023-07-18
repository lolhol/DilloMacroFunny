package com.dillo.dilloUtils.RouteUtils.Nuker;

import static com.dillo.dilloUtils.LookAt.reset;
import static com.dillo.dilloUtils.RouteUtils.Nuker.NukerMain.isStartLook;
import static com.dillo.keybinds.Keybinds.isNuking;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.Utils.GetBlocksForNuker;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;

public class StartNuker {

  public static void startNuker() {
    if (currentRoute.currentRoute.size() > 1) {
      isStartLook = false;
      reset();
      SendChat.chat(prefix.prefix + "Started Nuker!");
      GetBlocksForNuker.getBlocks(currentRoute.currentRoute, "nuker");
    } else {
      isNuking = false;
      SendChat.chat(prefix.prefix + "No route selected! :L ");
    }
  }

  public static void stopNuker() {
    NukerMain.startNuking = false;
    NukerMain.broken.clear();
    isStartLook = false;
    reset();
    isNuking = false;
    NukerMain.nuking.clear();
    SendChat.chat(prefix.prefix + "Stopped nuker!");
  }
}
