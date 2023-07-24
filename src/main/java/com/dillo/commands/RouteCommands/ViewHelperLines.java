package com.dillo.commands.RouteCommands;

import static com.dillo.main.route.ViewClearLines.ViewClearLines.clearLines;
import static com.dillo.main.route.ViewClearLines.ViewClearLines.viewClearLines;

import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class ViewHelperLines extends Command {

  public static boolean isRender = false;

  public ViewHelperLines() {
    super("helperLines");
  }

  @DefaultHandler
  public void handle() {
    isRender = !isRender;

    if (isRender) {
      viewClearLines();
    } else {
      clearLines();
    }
  }
}
