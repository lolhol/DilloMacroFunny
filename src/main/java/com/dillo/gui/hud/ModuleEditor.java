package com.dillo.gui.hud;

import static com.dillo.armadillomacro.allOverlays;
import static com.dillo.config.AutoSaveConfig.isOverride;

import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.GUIUtils.button_utils.ButtonConfig;
import com.dillo.gui.overlays.button.ButtonGuiClass;
import com.dillo.gui.overlays.button.FunctionCallBack;
import com.dillo.gui.overlays.overlay.OnRouteCheck;
import com.dillo.utils.previous.random.ids;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author brigeroman
 * @version 1.1
 * @credit ClientAPI (Papa-Stalin), Papa-Stalin, Gabagooooooooooool
 * @brief Module Editor
 */

public class ModuleEditor extends GuiScreen {

  public static boolean isMiniGuiOn;
  public Element curElement;
  public static List<ButtonGuiClass> buttons = new ArrayList<>();
  public Color selectedColor = null;

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);

    drawDefaultBackground();

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    if (!isMiniGuiOn) {
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
    } else {
      int borderSize = 10;
      int rectWidth = width - 2 * borderSize;
      int rectHeight = height - 2 * borderSize;
      drawRect(borderSize, borderSize, borderSize + rectWidth, borderSize + rectHeight, Color.WHITE.getRGB());
    }

    for (ButtonGuiClass button : buttons) {
      renderButton(button);
    }
    //drawRect(mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2, 0xFFFFFFFF);
  }

  public void changeButtonColor(GuiButton curButton, Color newColor) {
    ButtonGuiClass buttonGui = getButtonClicked(curButton);

    if (buttonGui != null) {
      buttonGui.buttonColor = newColor;
    }
  }

  public ButtonGuiClass getButtonFromId(int id) {
    for (ButtonGuiClass button : buttons) {
      if (button.button.id == id) {
        return button;
      }
    }

    return null;
  }

  public void renderButton(ButtonGuiClass button) {
    FontRenderer fontRenderer = ids.mc.fontRendererObj;
    GuiButton buttonGUI = button.button;

    drawRect(
      buttonGUI.xPosition,
      buttonGUI.yPosition,
      buttonGUI.xPosition + buttonGUI.width,
      buttonGUI.yPosition + buttonGUI.height,
      button.buttonColor.getRGB()
    );

    String label = button.buttonText;
    drawCenteredString(
      fontRenderer,
      label,
      buttonGUI.xPosition + buttonGUI.width / 2,
      buttonGUI.yPosition + buttonGUI.height / 2 - 4,
      0xFFFFFFFF
    );
  }

  public ButtonGuiClass addButton(
    ButtonConfig config,
    String option1,
    String option2,
    FunctionCallBack offCall,
    FunctionCallBack onCall
  ) {
    GuiButton button = new GuiButton(
      config.BUTTON_ID,
      config.BUTTON_X,
      config.BUTTON_Y,
      config.BUTTON_WIDTH,
      config.BUTTON_HEIGHT,
      config.BUTTON_TEXT
    );

    buttonList.add(button);
ButtonGuiClass newClass = new ButtonGuiClass(
  button,
  Color.GREEN,
  Color.GREEN,
  Color.RED,
  option1,
  true,
  option1,
  option2,
  onCall,
  offCall
);
    buttons.add(newClass);
    return newClass;
  }

  public void removeButton(ButtonGuiClass button, int position, int id) {
    if (position != -1) {
      buttons.remove(position);
    } else if (button != null) {
      buttons.remove(button);
    } else if (id != -1) {
      for (ButtonGuiClass curButton : buttons) {
        if (curButton.button.id == id) {
          buttonList.remove(curButton.button);
          buttons.remove(curButton);
          return;
        }
      }
    }
  }

  public ButtonGuiClass getButtonClicked(GuiButton button) {
    for (ButtonGuiClass curButton : buttons) {
      if (curButton.button.equals(button)) {
        return curButton;
      }
    }

    return null;
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    ButtonGuiClass clickedButtonClass = getButtonClicked(button);

    if (clickedButtonClass != null) {
      clickedButtonClass.isOn = !clickedButtonClass.isOn;
      clickedButtonClass.buttonColor = clickedButtonClass.isOn ? Color.GREEN : Color.RED;
      clickedButtonClass.buttonText =
        clickedButtonClass.isOn ? clickedButtonClass.buttonTextOn : clickedButtonClass.buttonTextOff;

      if (
        !clickedButtonClass.isOn
      ) clickedButtonClass.offCall.callFunction(); else clickedButtonClass.onCall.callFunction();

      if (curElement != null) {
        curElement.buttonActions(clickedButtonClass.isOn, clickedButtonClass.button);
      }
    }
    /*button.displayString = Objects.equals(button.displayString, "On") ? "Off" : "On";
    button.enabled = true;*/
  }

  @Override
  public void initGui() {
    super.initGui();
  }

  /*@Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int dWheel = Mouse.getEventDWheel();
  }*/

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    if (mouseButton == 0) {
      mouseButtonLeft(mouseX, mouseY);
    } else {
      mouseButtonRight(mouseX, mouseY);
    }
  }

  public void mouseButtonLeft(int mouseX, int mouseY) {
    Element element = getHoverElement(mouseX, mouseY);

    if (element != null) {
      element.onClick(mouseX, mouseY);
    }
  }

  public void mouseButtonRight(int mouseX, int mouseY) {
    Element element = getHoverElement(mouseX, mouseY);
    if (element != null) element.initiateMiniMenu(this);
  }

  public Element getHoverElement(int mouseX, int mouseY) {
    for (Element element : allOverlays) {
      if (isHover(element.getX(), element.getY(), element.width, element.height, mouseX, mouseY)) {
        return element;
      }
    }

    return null;
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

  public void setSelectedColor(Color color) {
    this.selectedColor = color;
  }

  @Override
  public boolean doesGuiPauseGame() {
    return true;
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    reset();
    isOverride = true;
  }

  public void reset() {
    for (Element element : allOverlays) {
      if (element instanceof OnRouteCheck) {
        element.closeMiniGUI(this);
      }
    }
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
