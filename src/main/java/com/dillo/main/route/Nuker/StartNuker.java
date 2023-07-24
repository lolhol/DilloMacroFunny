package com.dillo.main.route.Nuker;

import static com.dillo.keybinds.Keybinds.isNuking;
import static com.dillo.main.route.Nuker.NukerMain.isStartLook;
import static com.dillo.main.utils.looks.LookAt.reset;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.route.Utils.GetBlocksForNuker;
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
