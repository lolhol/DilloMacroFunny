package com.dillo.commands.RouteCommands;

import static com.dillo.utils.renderUtils.RenderString.renderStr;

import com.dillo.dilloUtils.BlockUtils.fileUtils.localizedData.currentRoute;
import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.prefix;
import com.dillo.utils.renderUtils.RenderBox;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import java.awt.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StructurePoints extends Command {

  public static boolean render = false;

  public StructurePoints() {
    super("structurePoints");
  }

  @DefaultHandler
  public void handle() {
    if (currentRoute.strucList.size() > 0) {
      render = !render;
      //SendChat.chat(String.valueOf(currentRoute.strucList.size()));
      SendChat.chat(render ? prefix.prefix + "Displaying structures!" : prefix.prefix + "Stopped displaying!");
    } else {
      SendChat.chat(prefix.prefix + "There is nothing to display!");
    }
  }

  @SubscribeEvent
  public void onRenderWorld(RenderWorldLastEvent event) {
    if (render) {
      for (int i = 0; i < currentRoute.strucList.size(); i++) {
        try {
          RenderBox.drawBox(
            currentRoute.strucList.get(i).getX(),
            currentRoute.strucList.get(i).getY(),
            currentRoute.strucList.get(i).getZ(),
            Color.WHITE,
            0.5F,
            event.partialTicks
          );
          renderStr(
            String.valueOf(i + 1),
            currentRoute.strucList.get(i).getX() + 0.5,
            currentRoute.strucList.get(i).getY() - 1,
            currentRoute.strucList.get(i).getZ() + 0.5,
            event.partialTicks,
            false
          );
        } catch (Exception e) {}
      }
    }
  }
}
