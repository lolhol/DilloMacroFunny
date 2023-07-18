package com.dillo.mixin;

import com.dillo.commands.RouteMakerUtils.GemESP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class MixinGemESP {

  @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
  private void renderBlock(
    IBlockState state,
    BlockPos pos,
    IBlockAccess blockAccess,
    WorldRenderer worldRendererIn,
    CallbackInfoReturnable<Boolean> cir
  ) {
    if (GemESP.isRenderGems) {
      if (state.getBlock() != Blocks.stained_glass && state.getBlock() != Blocks.stained_glass_pane) {
        cir.setReturnValue(false);
      }
    }
  }
}
