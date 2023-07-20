package com.dillo.dilloUtils.Teleport.TeleportMovePlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class VertexGetterConfig {

  public BlockPos playerPosition;
  public BlockPos nextBlock;
  public float playerHeight;
}
