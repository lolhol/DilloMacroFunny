package com.dillo.MITGUI;

import static com.dillo.MITGUI.GUIUtils.CurRatesUtils.ItemsPickedUp.started;
import static com.dillo.MITGUI.GUIUtils.DilloRouteUtils.IsInBlockRange.isInCheckRange;
import static com.dillo.MITGUI.GUIUtils.MatchServer.IsChecked.isChecked;
import static com.dillo.data.config.*;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.GetTotalEarned;
import com.dillo.MITGUI.GUIUtils.CurRatesUtils.ItemsPickedUp;
import com.dillo.MITGUI.GUIUtils.CurTimeVein.CurTime;
import com.dillo.MITGUI.GUIUtils.DilloRouteUtils.IsInBlockRange;
import com.dillo.data.config;
import com.dillo.dilloUtils.WorldScan.Utils.StringUtils;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class Overlay {
  public static long curTime = System.currentTimeMillis();
  private static long lastLobbyCheck = System.currentTimeMillis();

  @SubscribeEvent
  public void renderGameOverlay(RenderGameOverlayEvent event) {
    if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
      return;
    }

    if (timeVein) {
      long currTime = CurTime.curTime();

      if (currTime > 0) {
        drawWithColor("Current Time/Vein: " + currTime + "ms", 10, 20, Color.GREEN);
      } else {
        if (Objects.equals(ArmadilloStates.offlineState, "online")) {
          drawWithColor("Current Time/Vein: NONE", 10, 20, Color.GREEN);
        }
      }
    }

    if (started) {
      if (ItemsPickedUp.timePoint + 10000 >= System.currentTimeMillis()) {
        if (GetTotalEarned.totalEarned().totalEarned > 0) {
          GetTotalEarned.TotalEarning earnings = GetTotalEarned.totalEarned();

          String text = "Total Earned: " + earnings.totalEarningString + "$";
          String textPerHour = "Per hour: " + earnings.perHour + "$/hr";
          drawTextInBox(text, textPerHour, config.profitTrackerX, config.profitTrackerY, 30 * config.profitTrackerSize / 2, 20 * config.profitTrackerSize / 2);
        } else {
          started = false;
          GetTotalEarned.clearTotalEarned();
        }
      } else {
        started = false;
        GetTotalEarned.clearTotalEarned();
      }
    }

    if (onRouteCheck) {
      if (IsInBlockRange.isInCheckRange()) {
        if (curTime + 10000 > System.currentTimeMillis()) {
          GlStateManager.pushMatrix();
          GlStateManager.scale(4, 4, 4);
          FontRenderer fontRenderer = ids.mc.fontRendererObj;

          fontRenderer.drawStringWithShadow("Use Path Check!", 49, 35, Color.GREEN.getRGB());
          GlStateManager.popMatrix();
        }
      } else {
        curTime = System.currentTimeMillis();
      }
    }

    if (alrCheckedLobby) {
      if (isChecked()) {
        if (lastLobbyCheck + 5000 > System.currentTimeMillis()) {
          GlStateManager.pushMatrix();
          GlStateManager.scale(3, 3, 3);
          FontRenderer fontRenderer = ids.mc.fontRendererObj;

          fontRenderer.drawStringWithShadow("Alr checked lobby!", 10, 70, Color.red.getRGB());
          GlStateManager.popMatrix();
        }
      } else {
        lastLobbyCheck = System.currentTimeMillis();
      }
    }
  }

  private static void draw(String text, int x, int y) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF);
  }

  private static void drawWithColor(String text, int x, int y, Color color) {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fontRenderer = mc.fontRendererObj;
    fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
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
