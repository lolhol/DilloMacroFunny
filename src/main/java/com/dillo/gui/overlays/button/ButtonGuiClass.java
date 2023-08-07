package com.dillo.gui.overlays.button;

import java.awt.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;

@Getter
@AllArgsConstructor
public class ButtonGuiClass {

  public GuiButton button;
  public Color buttonColor;
  public Color buttonColorOption1;
  public Color buttonColorOption2;
  public String buttonText;
  public boolean isOn;
  public String buttonTextOn;
  public String buttonTextOff;
  public FunctionCallBack onCall;
  public FunctionCallBack offCall;
}
