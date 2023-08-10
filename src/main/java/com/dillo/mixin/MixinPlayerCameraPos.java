package com.dillo.mixin;

import static com.dillo.main.esp.player.FreeLook.isAdd;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderPlayer.class)
public class MixinPlayerCameraPos {

  @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
  private void onDoRender(
    EntityLivingBase entity,
    double x,
    double y,
    double z,
    float entityYaw,
    float partialTicks,
    CallbackInfoReturnable<Boolean> cir
  ) {
    if (isAdd) {
      //cir.setReturnValue(true);
      //entity.setPosition(x += 1, y += 1, z += 1);
    }
  }
}
