package com.dillo.gui.hud;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ModuleEditorTrigger {

    public static boolean isOpen;

    @SubscribeEvent
    public void onTick(TickEvent event) {
        // Check if the GUI is the main menu and the "P" key is pressed
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            //ids.mc.displayGuiScreen(new ModuleEditor());
        }
    }
}
