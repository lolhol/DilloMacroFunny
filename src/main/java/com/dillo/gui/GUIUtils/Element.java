package com.dillo.gui.GUIUtils;

import com.dillo.gui.hud.ModuleEditor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.Vec3;

public abstract class Element {

    public int width = 0;
    public int height = 0;

    public abstract Vec3 drag();

    public abstract int getX();

    public abstract void setX(int pos);

    public abstract int getY();

    public abstract void setY(int pos);

    public abstract void editorDraw(int x, int y);

    public abstract void guiDraw();

    public abstract void mouseReleased(int mouseX, int mouseY);

    public abstract void onClick(int mouseX, int mouseY);

    public boolean enabled() {
        return true;
    }

    public abstract void initiateMiniMenu(ModuleEditor editor);

    public boolean isHeld() {
        return false;
    }

    public abstract void buttonActions(boolean buttonState, GuiButton button);

    public abstract void closeMiniGUI(ModuleEditor editor);
}
