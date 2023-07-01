package com.dillo.dilloUtils.BlockESP;

import com.dillo.data.config;
import java.awt.*;

public class SelectedColor {

  public static Color getSelectedColor() {
    if (config.blockESPColor == 0) {
      return Color.RED;
    } else if (config.blockESPColor == 1) {
      return Color.GREEN;
    } else if (config.blockESPColor == 2) {
      return Color.BLUE;
    }/*else if(config.blockESPColor == 3) {
            //
        } else if(config.blockESPColor == 4) {
            //
        } else if(config.blockESPColor == 5) {
            //
        }*/

    return Color.MAGENTA;
  }
}
