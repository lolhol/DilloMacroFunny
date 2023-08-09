package com.dillo.main.teleport.TeleportMovePlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@Getter
@AllArgsConstructor
public class VertexGetterConfig {

    public Vec3 playerPosition;
    public BlockPos nextBlock;
    public float playerHeight;
}
