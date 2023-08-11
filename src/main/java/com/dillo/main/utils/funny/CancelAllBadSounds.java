package com.dillo.main.utils.funny;

import com.dillo.utils.previous.SendChat;
import com.dillo.utils.previous.random.ids;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CancelAllBadSounds {

  public List<String> list = new ArrayList<String>() {
    {
      add("glass");
      add("step");
    }
  };

  @SubscribeEvent
  public void onSound(PlaySoundEvent event) {
    if (ids.mc.theWorld == null) return;

    if (!isContains(list, event.name)) {
      SendChat.chat(event.name);
      //event.setCanceled(true);
      event.setResult(null);
      event.result = null;
    }
  }

  private boolean isContains(List<String> strings, String target) {
    for (String str : strings) {
      if (target.toLowerCase().contains(str.toLowerCase())) {
        return true;
      }
    }

    return false;
  }
}
