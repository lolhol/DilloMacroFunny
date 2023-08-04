package com.dillo.gui.GUIUtils;

public class Element {

  public boolean isHudModule = true;
  public int width = 0;
  public int height = 0;

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

  public void onClick() {}

  public boolean enabled() {
    return true;
  }

  public boolean isHeld() {
    return false;
  }
}
