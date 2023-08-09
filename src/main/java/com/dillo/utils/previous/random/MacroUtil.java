package com.dillo.utils.previous.random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MacroUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Double playerPosX;
    public static Double playerPosY;
    public static Double playerPosZ;
    public static int intPosX;
    public static int intPosY;
    public static int intPosZ;

    //Old pizza rotation function (when it was open source), creds to them and let me know if you want me to remove it!
    public static void snapTo(final Vec3 target) {
        final double diffX = target.xCoord - mc.thePlayer.posX;
        final double diffY = target.yCoord - (mc.thePlayer.posY + PlayerUtil.fastEyeHeight());
        final double diffZ = target.zCoord - mc.thePlayer.posZ;
        final EntityPlayerSP thePlayer = mc.thePlayer;
        thePlayer.rotationYaw +=
                (float) MathHelper.wrapAngleTo180_double(
                        MathHelper.atan2(diffZ, diffX) * 57.29577951308232 - 90 - mc.thePlayer.rotationYaw
                );
        final EntityPlayerSP thePlayer2 = mc.thePlayer;
        thePlayer2.rotationPitch +=
                (float) MathHelper.wrapAngleTo180_double(
                        -(MathHelper.atan2(diffY, (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ)) * 57.29577951308232) -
                                mc.thePlayer.rotationPitch
                );
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            playerPosX = mc.thePlayer.posX;
            playerPosY = mc.thePlayer.posY;
            playerPosZ = mc.thePlayer.posZ;
            intPosX = playerPosX.intValue();
            intPosY = playerPosY.intValue();
            intPosZ = playerPosZ.intValue();
        }
    }
}
