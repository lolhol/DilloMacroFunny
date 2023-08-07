package com.dillo.gui.overlays.overlay;

import static com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp.started;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.CurRatesUtils.GetTotalEarned;
import com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.hud.ModuleEditor;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.Vec3;

public class ProfitTracker extends Element {

  public boolean isHeld;
  public Vec3 drag = new Vec3(0, 0, 0);

  @Override
  public void initiateMiniMenu(ModuleEditor editor) {}

  public ProfitTracker() {
    width = 160;
    height = 30;
  }

  private static void draw(String text, int x, int y, Color color) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
  }

  @Override
  public Vec3 drag() {
    return drag;
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
  public void buttonActions(boolean buttonState, GuiButton button) {}

  @Override
  public void closeMiniGUI(ModuleEditor editor) {}

  @Override
  public void guiDraw() {
    if (started) {
      if (ItemsPickedUp.timePoint + 10000 >= System.currentTimeMillis()) {
        if (GetTotalEarned.totalEarned().totalEarned > 0) {
          GetTotalEarned.TotalEarning earnings = GetTotalEarned.totalEarned();

          renderProfitTracker(
            false,
            getX(),
            getY(),
            config.profitTrackerSize,
            earnings.totalEarningString,
            earnings.perHour,
            formatTime(earnings.trackTime)
          );
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
    renderProfitTracker(false, x, y, config.profitTrackerSize, "69.420", "69.420", formatTime(123456789));
  }

  public static String formatTime(long milliseconds) {
    long seconds = milliseconds / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;

    seconds %= 60;
    minutes %= 60;

    return String.format("%02dh %02dmin %02dsec", hours, minutes, seconds);
  }

  private void renderProfitTracker(
    boolean isNon,
    int x,
    int y,
    int size,
    String amountOCoinTotal,
    String amountOCoinAnHour,
    String timeTracked
  ) {
    if (isNon) return;

    String text = "Total Earned: " + amountOCoinTotal + "$";
    String textPerHour = "Per hour: " + amountOCoinAnHour + "$/hr";
    String textTimeTracked = "Time Tracked: " + timeTracked;

    draw(textPerHour, x, y + 10, Color.green);
    draw(textTimeTracked, x, y + 20, Color.green);
    draw(text, x, y, Color.green);
    //drawTextInBox(text, textPerHour, x, y, 30 * size / 2, 20 * size / 2);
  }
}
