package com.dillo.gui.overlays;

import static com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp.started;
import static com.dillo.gui.Overlay.drawWithColor;

import com.dillo.calls.ArmadilloStates;
import com.dillo.config.config;
import com.dillo.gui.GUIUtils.CurRatesUtils.GetTotalEarned;
import com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.gui.GUIUtils.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class ProfitTracker extends Element {

  public boolean isHeld;

  public ProfitTracker() {
    width = 140;
    height = 10;
  }

  @Override
  public int getX() {
    return config.profitTrackerX;
  }

  @Override
  public void setX(int val) {
    config.profitTrackerX = val;
  }

  @Override
  public int getY() {
    return config.profitTrackerY;
  }

  @Override
  public void setY(int val) {
    config.profitTrackerY = val;
  }

  @Override
  public boolean enabled() {
    return true;
  }

  @Override
  public void onClick() {
    isHeld = true;
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY) {
    isHeld = false;
    setX(mouseX);
    setY(mouseY);
  }

  @Override
  public boolean isHeld() {
    return isHeld;
  }

  @Override
  public void guiDraw() {
    if (started) {
      if (ItemsPickedUp.timePoint + 10000 >= System.currentTimeMillis()) {
        if (GetTotalEarned.totalEarned().totalEarned > 0) {
          renderProfitTracker(false, getX(), getY(), config.profitTrackerSize);
        } else {
          started = false;
          GetTotalEarned.clearTotalEarned();
        }
      } else {
        started = false;
        GetTotalEarned.clearTotalEarned();
      }
    }
    //renderProfitTracker(false, getX(), getY(), config.profitTrackerSize);
  }

  @Override
  public void editorDraw(int x, int y) {
    renderProfitTracker(false, x, y, config.profitTrackerSize);
  }

  private void renderProfitTracker(boolean isNon, int x, int y, int size) {
    if (isNon) return;

    GetTotalEarned.TotalEarning earnings = GetTotalEarned.totalEarned();

    String text = "Total Earned: " + earnings.totalEarningString + "$";
    String textPerHour = "Per hour: " + earnings.perHour + "$/hr";
    drawTextInBox(text, textPerHour, x, y, 30 * size / 2, 20 * size / 2);
  }

  private static void draw(String text, int x, int y) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
  }

  private static void drawBoxAround(int x, int y, int width, int height) {
    StringBuilder topText = new StringBuilder();

    for (int i = 0; i < width; i++) {
      topText.append("_");
    }

    draw(topText.toString(), x, y - height * 3 - 5);

    int currDepth = y;

    for (int i = 0; i < height; i++) {
      draw("|", x, currDepth);
      currDepth -= 3;
    }

    draw(topText.toString(), x, y);

    int currDepth1 = y;
    for (int i = 0; i < height; i++) {
      draw("|", x + width * 6, currDepth1);
      currDepth1 -= 3;
    }
  }

  private static void drawTextInBox(String text1, String text2, int x, int y, int width, int height) {
    drawBoxAround(x, y, width, height);
    draw(text1, x + width / 2, y - height / 2 - 10);
    draw(text2, x + width / 2, y - 25 - height / 2);
  }
}
