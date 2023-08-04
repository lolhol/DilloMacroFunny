package com.dillo.gui.hud;

import static com.dillo.armadillomacro.allOverlays;

import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.overlays.ProfitTracker;
import com.dillo.utils.previous.SendChat;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

/**
 * @author Papa-Stalin Gabagooooooooooool ChatGPT
 * @version 1.1
 * @credit ClientAPI (Papa-Stalin)
 * @brief Module Editor
 */

public class ModuleEditor extends GuiScreen {

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);

    drawDefaultBackground();

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    for (Element element : allOverlays) {
      if (!element.isHeld()) {
        element.editorDraw(element.getX(), element.getY());

        drawRectWithOutline(
          element.getX() - 2,
          element.getY() - 2,
          element.getX() + element.width,
          element.getY() + element.height,
          0xFFFFFFFF
        );
        continue;
      }

      int x = (int) (mouseX - (element.drag().xCoord - element.getX()));
      int y = (int) (mouseY - (element.drag().yCoord - element.getY()));

      element.editorDraw(x, y);

      drawRectWithOutline(x - 2, y - 2, x + element.width, y + element.height, 0xFFFFFFFF);
    }
    //drawRect(mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2, 0xFFFFFFFF);
  }

  @Override
  public void initGui() {
    super.initGui();
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int dWheel = Mouse.getEventDWheel();
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    for (Element element : allOverlays) {
      if (isHover(element.getX(), element.getY(), element.width, element.height, mouseX, mouseY)) {
        SendChat.chat("!!!");
        element.onClick(mouseX, mouseY);
      }
    }
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY, int state) {
    for (Element element : allOverlays) {
      if (element.isHeld()) {
        element.mouseReleased(
          (int) (mouseX - (element.drag().xCoord - element.getX())),
          (int) (mouseY - (element.drag().yCoord - element.getY()))
        );
      }
    }
  }

  @Override
  public boolean doesGuiPauseGame() {
    return true;
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
  }

  private boolean isHover(int x, int y, int w, int h, int mX, int mY) {
    return mX >= x && mX <= x + w && mY >= y && mY <= y + h;
  }

  private void drawRectWithOutline(int x1, int y1, int x2, int y2, int outlineColor) {
    // Draw the outline (top and bottom lines)
    drawHorizontalLine(x1, x2, y1, outlineColor);
    drawHorizontalLine(x1, x2, y2, outlineColor);

    // Draw the outline (left and right lines)
    drawVerticalLine(x1, y1, y2, outlineColor);
    drawVerticalLine(x2, y1, y2, outlineColor);
  }
  /*
  private void drawHorizontalLine(int startX, int endX, int y, int color) {
    if (startX < endX) {
      int temp = startX;
      startX = endX;
      endX = temp;
    }
    drawRect(endX, y, startX + 1, y + 1, color);
  }

  private void drawVerticalLine(int x, int startY, int endY, int color) {
    if (startY < endY) {
      int temp = startY;
      startY = endY;
      endY = temp;
    }
    drawRect(x, endY + 1, x + 1, startY, color);
  }

 */
}
