package com.dillo.dilloUtils.WorldScan.Utils.objects;

import java.awt.*;
import net.minecraft.entity.Entity;

public class HighlightEntity {

  private final String name;
  private final int color;

  public HighlightEntity(String name, int color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public int getColor() {
    return color;
  }
}
