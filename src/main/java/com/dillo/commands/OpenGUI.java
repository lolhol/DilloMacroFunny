package com.dillo.commands;

import com.dillo.config.config;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;

import java.util.Objects;

public class OpenGUI extends Command {

    public OpenGUI() {
        super("MITmenu");
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(Objects.requireNonNull(config.INSTANCE.gui()));
    }
}
