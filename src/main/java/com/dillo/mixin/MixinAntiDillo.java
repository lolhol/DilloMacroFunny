package com.dillo.mixin;

import com.dillo.config.config;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
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
        if (config.armadilloDefeatr || config.armadildo) {
            cir.setReturnValue(false);
        }
    }
}
