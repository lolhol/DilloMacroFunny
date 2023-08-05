package com.dillo.gui.overlays;

import static com.dillo.config.config.onRouteCheck;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.DilloRouteUtils.IsInBlockRange;
import com.dillo.gui.GUIUtils.Element;
import com.dillo.utils.previous.random.ids;
import java.awt.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

public class OnRouteCheck extends Element {

  public static long curTime = System.currentTimeMillis();

  public boolean isHeld;
  public Vec3 drag = new Vec3(0, 0, 0);

  public OnRouteCheck() {
    width = 110;
    height = 10;
  }

  @Override
  public Vec3 drag() {
    return drag;
  }

  @Override
  public int getX() {
    return config.routeCheckX;
  }

  @Override
  public void setX(int val) {
    config.routeCheckX = val;
  }

  @Override
  public int getY() {
    return config.routeCheckY;
  }

  @Override
  public void setY(int val) {
    config.routeCheckY = val;
  }

  @Override
  public boolean enabled() {
    return true;
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
    if (onRouteCheck) {
      if (ids.mc.theWorld != null && ids.mc.thePlayer != null && IsInBlockRange.isInCheckRange()) {
        if (curTime + 10000 > System.currentTimeMillis()) {
          renderBigText(false, getX() / 2, getY() / 2, 2);
        }
      } else {
        curTime = System.currentTimeMillis();
      }
    }
    //renderProfitTracker(false, getX(), getY(), config.profitTrackerSize);
  }

  @Override
  public void editorDraw(int x, int y) {
    renderBigText(false, x / 2, y / 2, 2);
  }

  private void renderBigText(boolean isNon, int x, int y, int size) {
    if (isNon) return;

    GlStateManager.pushMatrix();
    GlStateManager.scale(size, size, size);
    FontRenderer fontRenderer = ids.mc.fontRendererObj;
    fontRenderer.drawStringWithShadow("Use Path Check!", x, y, Color.GREEN.getRGB());
    GlStateManager.popMatrix();
  }
}
