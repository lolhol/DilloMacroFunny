package com.dillo.gui.GUIUtils;

import net.minecraft.util.Vec3;

public class Element {

  public boolean isHudModule = true;
  public int width = 0;
  public int height = 0;
  public Vec3 drag = null;

  public Vec3 drag() {
    return null;
  }

  public int getX() {
    return 0;
  }

  public void setX(int pos) {}

  public int getY() {
    return 0;
  }

  public void setY(int pos) {}

  public void editorDraw(int x, int y) {}

  public void guiDraw() {}

  public void mouseReleased(int mouseX, int mouseY) {}

  public void onClick(int mouseX, int mouseY) {}

  public boolean enabled() {
    return true;
  }

  public boolean isHeld() {
    return false;
  }
}
