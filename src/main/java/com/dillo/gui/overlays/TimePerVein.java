package com.dillo.gui.overlays;

import static com.dillo.gui.Overlay.drawWithColor;

import com.dillo.calls.ArmadilloStates;
import com.dillo.config.config;
import com.dillo.gui.GUIUtils.CurTimeVein.CurTime;
import com.dillo.gui.GUIUtils.Element;
import java.awt.*;
import net.minecraft.util.Vec3;

public class TimePerVein extends Element {

  public boolean isHeld;
  public Vec3 drag = new Vec3(0, 0, 0);

  @Override
  public Vec3 drag() {
    return drag;
  }

  public TimePerVein() {
    width = 140;
    height = 10;
  }

  @Override
  public int getX() {
    return config.timeVeinX;
  }

  @Override
  public void setX(int val) {
    config.timeVeinX = val;
  }

  @Override
  public int getY() {
    return config.timeVeinY;
  }

  @Override
  public void setY(int val) {
    config.timeVeinY = val;
  }

  @Override
  public boolean enabled() {
    return config.timeVein;
  }

  @Override
  public void onClick(int mouseX, int mouseY) {
    isHeld = true;
    drag = new Vec3(mouseX, mouseY, 0);
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY) {
    isHeld = false;
    setX(mouseX);
    setY(mouseY);
    drag = new Vec3(0, 0, 0);
  }

  @Override
  public boolean isHeld() {
    return isHeld;
  }

  @Override
  public void guiDraw() {
    if (!config.timeVein) return;

    long currTime = CurTime.curTime();

    if (currTime > 0) {
      renderTimeVein(currTime, false, getX(), getY());
    } else {
      if (ArmadilloStates.isOnline()) {
        renderTimeVein(currTime, true, getX(), getY());
      }
    }
  }

  @Override
  public void editorDraw(int x, int y) {
    renderTimeVein(1337, false, x, y);
  }

  private void renderTimeVein(long time, boolean isNon, int x, int y) {
    if (isNon) {
      drawWithColor("Current Time/Vein: NONE", x, y, Color.GREEN);
      return;
    }
    drawWithColor("Current Time/Vein: " + time + "ms", x, y, Color.GREEN);
  }
}
