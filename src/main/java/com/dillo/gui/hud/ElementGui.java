package com.dillo.gui.hud;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class ElementGui extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
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
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
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
}
