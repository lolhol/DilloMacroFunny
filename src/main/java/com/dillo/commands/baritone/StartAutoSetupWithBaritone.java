package com.dillo.commands.baritone;

import static com.dillo.dilloUtils.RouteUtils.AutoSetup.SetupMain.isTurnedOn;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.dilloUtils.RouteUtils.AutoSetup.SetupMain;
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
      isTurnedOn = !isTurnedOn;
      if (!isTurnedOn) main.reset();
    }

    SendChat.chat(isTurnedOn ? "Starting!" : "DONE");
  }
}
