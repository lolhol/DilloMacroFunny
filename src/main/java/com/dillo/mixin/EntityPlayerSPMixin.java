package com.dillo.mixin;

import com.dillo.events.PlayerMoveEvent;
import com.dillo.events.ScreenClosedEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class)
public class EntityPlayerSPMixin extends EntityPlayer {

    public EntityPlayerSPMixin(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "closeScreen", at = @At("HEAD"), cancellable = true)
    public void closeScreen(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new ScreenClosedEvent(this.openContainer))) ci.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalking(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Pre())) ci.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onWalking(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Post())) ci.cancel();
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
}
