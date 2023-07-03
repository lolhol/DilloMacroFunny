package com.dillo.commands.UtilCommands;

import com.dillo.dilloUtils.RouteUtils.LegitRouteClear.LegitRouteClear;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class StartClearLegit extends Command {

  public static boolean isClearing = false;

  public StartClearLegit() {
    super("clearLegit");
  }

  @DefaultHandler
  public void handle() {
    isClearing = !isClearing;
    if (isClearing) {
      LegitRouteClear.clearRouteLegit();
    } else {
      LegitRouteClear.stopClearLegit();
    }
  }
}
