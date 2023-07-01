package com.dillo.commands.RouteCommands;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.dillo.dilloUtils.RouteUtils.ViewClearLines.ViewClearLines.clearLines;
import static com.dillo.dilloUtils.RouteUtils.ViewClearLines.ViewClearLines.viewClearLines;

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
