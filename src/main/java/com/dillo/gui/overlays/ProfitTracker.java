package com.dillo.gui.overlays;

import static com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp.started;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.CurRatesUtils.GetTotalEarned;
import com.dillo.gui.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.gui.GUIUtils.Element;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.vector.Vector;

public class ProfitTracker extends Element {

  public boolean isHeld;
  public Vec3 drag = new Vec3(0, 0, 0);

  public ProfitTracker() {
    width = 110;
    height = 20;
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
            earnings.perHour
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
    renderProfitTracker(false, x, y, config.profitTrackerSize, "69.420", "69.420");
  }

  private void renderProfitTracker(
    boolean isNon,
    int x,
    int y,
    int size,
    String amountOCoinTotal,
    String amountOCoinAnHour
  ) {
    if (isNon) return;

    String text = "Total Earned: " + amountOCoinTotal + "$";
    String textPerHour = "Per hour: " + amountOCoinAnHour + "$/hr";

    draw(textPerHour, x, y + 10, Color.green);
    draw(text, x, y, Color.green);
    //drawTextInBox(text, textPerHour, x, y, 30 * size / 2, 20 * size / 2);
  }

  private static void draw(String text, int x, int y, Color color) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
  }
}
