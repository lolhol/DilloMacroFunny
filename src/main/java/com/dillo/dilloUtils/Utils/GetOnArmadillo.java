package com.dillo.dilloUtils.Utils;

import static com.dillo.utils.keyBindings.rightClick;

import com.dillo.ArmadilloMain.ArmadilloStates;
import com.dillo.utils.previous.random.ids;
import com.dillo.utils.throwRod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetOnArmadillo {
    public static String newState = null;
    public static boolean start = false;
    public static int stage = 0;
    public static int count = 40;
    private static int clickCount = 10;
    private static BlockPos prevBlock = null;

    public static void getOnArmadillo(String newState) {
        if (!isOnDillo()) {
            GetOnArmadillo.newState = newState;
            prevBlock = ids.mc.thePlayer.getPosition();
            prevBlock.add(0, -1, 0);
            start = true;
            stage = 1;
        } else {
            ArmadilloStates.currentState = newState;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Objects.equals(ArmadilloStates.offlineState, "online")) {
            if (start) {
                // Stage 1 checks if the armadillo is summoned.

                if (stage == 1) {
                    if (count >= 40) {
                        count = 0;
                        Entity curEnt = isSummoned();

                        if (curEnt != null) {
                            if (curEnt.posX == ids.mc.thePlayer.posX && curEnt.posZ == ids.mc.thePlayer.posZ) {
                                count = 40;
                                stage = 2;
                            }
                        } else {
                            throwRod.throwRodInv();
                        }
                    } else {
                        count++;
                    }
                }

                // Stage 2 actually clicks right click and gets on.

                if (stage == 2) {
                    if (clickCount >= 10) {
                        clickCount = 0;

                        if (prevBlock.getY() + 0.5 < ids.mc.thePlayer.posY - 1) {
                            ArmadilloStates.currentState = newState;
                            start = false;
                        } else {
                            rightClick();
                        }
                    } else {
                        clickCount++;
                    }
                }
            }
        }
    }

    private static boolean isOnDillo() {
        return ids.mc.thePlayer.posY - Math.floor(ids.mc.thePlayer.posY) > 0;
    }

    public static Entity isSummoned() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        AxisAlignedBB boundingBox = new AxisAlignedBB(
                player.posX - 3, player.posY - 3, player.posZ - 3,
                player.posX + 3, player.posY + 3, player.posZ + 3
        );

        List<Entity> entityList = mc.theWorld.getEntitiesWithinAABB(Entity.class, boundingBox);
        for (Entity entity : entityList) {
            if (!(entity instanceof EntityPlayer)) {
                if (entity.getName().contains(player.getName())) {
                    return entity;
                }
            }
        }

        return null;
    }
}
