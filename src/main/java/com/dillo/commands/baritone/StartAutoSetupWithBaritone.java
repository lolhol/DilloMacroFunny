package com.dillo.commands.baritone;

import com.dillo.main.files.localizedData.currentRoute;
import com.dillo.main.route.AutoSetup.SetupMain;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class StartAutoSetupWithBaritone extends Command {

  public static SetupMain main = new SetupMain();

  public StartAutoSetupWithBaritone() {
    super("autoSetup");
  }

  @DefaultHandler
  public void handle() {
    if (
      ids.mc.thePlayer.getCurrentEquippedItem().getDisplayName().toLowerCase().contains("drill") &&
      currentRoute.currentRoute.size() > 0
    ) {
      main.isTurnedOn = !main.isTurnedOn;
      if (!main.isTurnedOn) main.reset();
    }

    SendChat.chat(main.isTurnedOn ? "Starting!" : "DONE");
  }
}
