package com.dillo.mixin;

import com.dillo.commands.RouteMakerUtils.GemESP;
import com.dillo.config.config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinAntiDillo {

  @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
  private void renderBlock(
    Entity entityIn,
    ICamera camera,
    double camX,
    double camY,
    double camZ,
    CallbackInfoReturnable<Boolean> cir
  ) {
    if (config.armadilloDefeatr) {
      cir.setReturnValue(false);
    }
  }
}
