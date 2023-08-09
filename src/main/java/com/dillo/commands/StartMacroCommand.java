package com.dillo.commands;

import com.dillo.utils.StartMacro;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class StartMacroCommand extends Command {

    public StartMacroCommand() {
        super("testS");
    }

    @DefaultHandler
    public void handle() {
        StartMacro.startMacro();
    }
}
