package com.dillo.main.utils.funny;

import java.util.Arrays;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CancelAllBadSounds {

  public String[] sounds = new String[] {
    "block.glass.break",
    "block.glass.place",
    "block.glass.hit",
    "block.glass.fall",
    "step",
  };

  @SubscribeEvent
  public void onSound(PlaySoundEvent event) {
    if (
      Arrays
        .stream(sounds)
        .noneMatch(a -> {
          return event.name.toLowerCase().contains(a.toLowerCase());
        })
    ) {
      event.setCanceled(true);
      event.setResult(null);
    }
  }
}
