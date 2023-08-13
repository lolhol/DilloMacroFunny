package com.dillo.gui.overlays.overlay;

import static com.dillo.config.config.*;
import static com.dillo.gui.GUIUtils.MatchServer.IsChecked.isChecked;
import static com.dillo.utils.sound.SoundUtils.playSound;

import com.dillo.config.config;
import com.dillo.gui.GUIUtils.Element;
import com.dillo.gui.hud.ModuleEditor;
import com.dillo.utils.previous.random.ids;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

public class AlrCheckedLobby extends Element {

  private static long lastLobbyCheck = System.currentTimeMillis();
  private static boolean isFirst = true;
  public boolean isHeld;
  public float size = 1;
  public Vec3 drag = new Vec3(0, 0, 0);

  public AlrCheckedLobby() {
    width = 90;
    height = 10;
  }

  private static void draw(String text, int x, int y, Color color) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
  }

  @Override
  public void initiateMiniMenu(ModuleEditor editor) {}

  @Override
  public Vec3 drag() {
    return drag;
  }

  @Override
  public int getX() {
    return config.alrCheckedLobbyX;
  }

  @Override
  public void setX(int val) {
    config.alrCheckedLobbyX = val;
  }

  @Override
  public int getY() {
    return config.alrCheckedLobbyY;
  }

  @Override
  public void setY(int val) {
    config.alrCheckedLobbyY = val;
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
    if (alrCheckedLobby && !ids.mc.isSingleplayer()) {
      if (isChecked()) {
        if (lastLobbyCheck + 5000 > System.currentTimeMillis()) {
          if (alrCheckedLobbySound) {
            playSound(0.5f, 0.5f, "random.orb");
          }

          if (alrCheckedLobbyWarpOut && isFirst) {
            ids.mc.thePlayer.sendChatMessage("/is");
          }

          renderAlrCheckedLobby(false, getX(), getY());
        }
      } else {
        isFirst = true;
        lastLobbyCheck = System.currentTimeMillis();
      }
    }
  }

  @Override
  public void editorDraw(int x, int y) {
    renderAlrCheckedLobby(false, x, y);
  }

  private void renderAlrCheckedLobby(boolean isNon, int x, int y) {
    if (isNon) return;

    GlStateManager.pushMatrix();
    GlStateManager.scale(size, size, size);
    FontRenderer fontRenderer = ids.mc.fontRendererObj;
    isFirst = false;
    fontRenderer.drawStringWithShadow("Alr checked lobby!", x, y, Color.red.getRGB());
    GlStateManager.popMatrix();
    //drawTextInBox(text, textPerHour, x, y, 30 * size / 2, 20 * size / 2);
  }
}
