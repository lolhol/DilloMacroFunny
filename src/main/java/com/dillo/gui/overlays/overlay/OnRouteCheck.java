package com.dillo.gui.overlays.overlay;

import static com.dillo.config.config.onRouteCheck;
import static com.dillo.gui.hud.ModuleEditor.isMiniGuiOn;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.DilloRouteUtils.IsInBlockRange;
import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.GUIUtils.button_utils.ButtonConfig;
import com.dillo.gui.hud.ModuleEditor;
import com.dillo.gui.overlays.button.ButtonGuiClass;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

public class OnRouteCheck extends Element {

  public static long curTime = System.currentTimeMillis();

  public boolean isHeld;
  public Vec3 drag = new Vec3(0, 0, 0);
  public float size = 1;
  public boolean inited;
  public List<ButtonGuiClass> buttons = new ArrayList<>();

  public OnRouteCheck() {
    width = (int) (80 * getSize());
    height = (int) (10 * getSize());
  }

  public float getSize() {
    return size;
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
  public void buttonActions(boolean buttonState, GuiButton button) {}

  @Override
  public void guiDraw() {
    if (onRouteCheck) {
      if (ids.mc.theWorld != null && ids.mc.thePlayer != null && IsInBlockRange.isInCheckRange()) {
        if (curTime + 10000 > System.currentTimeMillis()) {
          renderBigText(getX(), getY(), getSize());
        }
      } else {
        curTime = System.currentTimeMillis();
      }
    }
    //renderProfitTracker(false, getX(), getY(), config.profitTrackerSize);
  }

  @Override
  public void editorDraw(int x, int y) {
    renderBigText(x, y, getSize());
  }

  public void initiateMiniMenu(ModuleEditor editor) {
    if (!inited) {
      isMiniGuiOn = true;
      buttons.add(
        editor.addButton(
          new ButtonConfig(0, 100, 20, editor.width / 2 - 50, 20, "Master Switch"),
          "Master Switch",
          "Master Switch",
          () -> {
            buttonActions(false, editor.getButtonFromId(0), editor);
          },
          () -> {
            buttonActions(true, editor.getButtonFromId(0), editor);
          }
        )
      );

      int borderSize = 10;
      int rectWidth = editor.width - 2 * borderSize;
      //int rectHeight = editor.height - 2 * borderSize;

      SendChat.chat(String.valueOf(editor.height));

      buttons.add(
        editor.addButton(
          new ButtonConfig(1, 30, 20, editor.width - 40, 10, "Close"),
          "Close",
          "Close",
          () -> {
            buttonActions(false, editor.getButtonFromId(1), editor);
          },
          () -> {
            buttonActions(true, editor.getButtonFromId(1), editor);
          }
        )
      );

      editor.curElement = this;
    } else {
      isMiniGuiOn = false;
      buttons.forEach(b -> {
        editor.removeButton(null, -1, b.button.id);
      });
      editor.curElement = null;
    }

    inited = !inited;
  }

  public void closeMiniGUI(ModuleEditor editor) {
    isMiniGuiOn = false;
    for (ButtonGuiClass button : this.buttons) {
      editor.removeButton(null, -1, button.getButton().id);
    }
    inited = false;
  }

  public void buttonActions(boolean buttonState, ButtonGuiClass button, ModuleEditor editor) {
    switch (button.getButton().id) {
      case 0:
        if (!buttonState) {
          //editor.removeButton(null, -1, button.getButton().id);
        }
        break;
      case 1:
        if (!buttonState) {
          closeMiniGUI(editor);
        }
        break;
    }
  }

  private void renderBigText(int x, int y, float size) {
    GlStateManager.pushMatrix();
    GlStateManager.scale(size, size, size);
    FontRenderer fontRenderer = ids.mc.fontRendererObj;
    fontRenderer.drawStringWithShadow("Use Path Check!", (float) x / size, (float) y / size, Color.GREEN.getRGB());
    GlStateManager.popMatrix();
  }
}
