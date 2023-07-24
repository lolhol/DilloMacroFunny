package com.dillo.adapter;

import com.dillo.pathfinding.stevebot.core.data.blockpos.BaseBlockPos;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockUtils;
import com.dillo.pathfinding.stevebot.core.data.blocks.BlockWrapper;
import com.dillo.pathfinding.stevebot.core.data.items.ItemUtils;
import com.dillo.pathfinding.stevebot.core.data.items.wrapper.ItemBlockWrapper;
import com.dillo.pathfinding.stevebot.core.data.items.wrapper.ItemStackWrapper;
import com.dillo.pathfinding.stevebot.core.data.items.wrapper.ItemWrapper;
import com.dillo.pathfinding.stevebot.core.math.vectors.vec2.Vector2d;
import com.dillo.pathfinding.stevebot.core.math.vectors.vec3.Vector3d;
import com.dillo.pathfinding.stevebot.core.minecraft.InputBinding;
import com.dillo.pathfinding.stevebot.core.minecraft.MinecraftAdapter;
import com.dillo.pathfinding.stevebot.core.minecraft.MouseChangeInterceptor;
import com.dillo.pathfinding.stevebot.core.pathfinding.actions.ActionCosts;
import com.dillo.pathfinding.stevebot.core.player.PlayerInputConfig;
import com.dillo.utils.previous.SendChat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

public class MinecraftAdapterImpl implements MinecraftAdapter {

  private MouseChangeInterceptor mouseChangeInterceptor = null;
  private final Map<String, Item> items = new HashMap<>();
  private final Map<String, Block> blocks = new HashMap<>();

  public MinecraftAdapterImpl() {
    getMinecraft().mouseHelper =
      new MouseHelper() {
        @Override
        public void mouseXYChange() {
          if (mouseChangeInterceptor == null || mouseChangeInterceptor.onChange()) {
            super.mouseXYChange();
          }
        }
      };
  }

  private Minecraft getMinecraft() {
    return Minecraft.getMinecraft();
  }

  private World getWorld() {
    return getMinecraft().theWorld;
  }

  private EntityPlayerSP getPlayer() {
    return getMinecraft().thePlayer;
  }

  @Override
  public boolean hasPlayer() {
    return getPlayer() != null;
  }

  @Override
  public boolean isPlayerCreativeMode() {
    return false;
  }

  @Override
  public Vector3d getPlayerHeadPosition() {
    final Vec3 posEyes = getPlayer().getPositionEyes(1.0F);
    return new Vector3d(posEyes.xCoord, posEyes.yCoord, posEyes.zCoord);
  }

  @Override
  public Vector2d getPlayerHeadPositionXZ() {
    final Vec3 posEyes = getPlayer().getPositionEyes(1.0F);
    return new Vector2d(posEyes.xCoord, posEyes.zCoord);
  }

  @Override
  public BaseBlockPos getPlayerBlockPosition() {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      return BlockUtils.toBaseBlockPos(player.posX, player.posY, player.posZ);
    } else {
      return null;
    }
  }

  @Override
  public Vector3d getPlayerPosition() {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      return new Vector3d(player.posX, player.posY, player.posZ);
    } else {
      return null;
    }
  }

  @Override
  public Vector3d getPlayerMotion() {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      return new Vector3d(player.motionX, player.motionY, player.motionZ);
    } else {
      return null;
    }
  }

  @Override
  public float getPlayerRotationYaw() {
    return getPlayer().rotationYaw;
  }

  @Override
  public float getPlayerRotationPitch() {
    return getPlayer().rotationPitch;
  }

  @Override
  public void setPlayerRotation(float yaw, float pitch) {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      player.rotationPitch = pitch;
      player.rotationYaw = yaw;
    }
  }

  @Override
  public void setCameraRotation(float yaw, float pitch) {
    Entity camera = getMinecraft().getRenderViewEntity();
    if (camera != null) {
      camera.rotationYaw = yaw;
      camera.rotationPitch = pitch;
      camera.prevRotationYaw = yaw;
      camera.prevRotationPitch = pitch;
    }
  }

  @Override
  public Vector3d getLookDir() {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      Vec3 lookDir = player.getLook(0f);
      return new Vector3d(lookDir.xCoord, lookDir.yCoord, lookDir.zCoord);
    } else {
      return null;
    }
  }

  @Override
  public void setMouseChangeInterceptor(MouseChangeInterceptor interceptor) {
    this.mouseChangeInterceptor = interceptor;
  }

  @Override
  public float getMouseSensitivity() {
    return getMinecraft().gameSettings.mouseSensitivity;
  }

  @Override
  public double getMouseDX() {
    return Mouse.getDX();
  }

  @Override
  public double getMouseDY() {
    return Mouse.getDY();
  }

  @Override
  public void setInput(final int keyCode, final boolean down) {
    KeyBinding.setKeyBindState(keyCode, down);
    if (down) {
      KeyBinding.onTick(keyCode);
    }
  }

  @Override
  public void setPlayerSprinting(final boolean sprint) {
    final EntityPlayerSP player = getPlayer();
    if (player != null) {
      player.setSprinting(sprint);
    }
  }

  @Override
  public InputBinding getKeyBinding(final PlayerInputConfig.InputType inputType) {
    GameSettings settings = getMinecraft().gameSettings;
    switch (inputType) {
      case WALK_FORWARD:
        return new McInputBinding(settings.keyBindForward);
      case WALK_BACKWARD:
        return new McInputBinding(settings.keyBindBack);
      case WALK_LEFT:
        return new McInputBinding(settings.keyBindLeft);
      case WALK_RIGHT:
        return new McInputBinding(settings.keyBindRight);
      case SPRINT:
        return new McInputBinding(settings.keyBindSprint);
      case SNEAK:
        return new McInputBinding(settings.keyBindSneak);
      case JUMP:
        return new McInputBinding(settings.keyBindJump);
      case PLACE_BLOCK:
        return new McInputBinding(settings.keyBindUseItem);
      case BREAK_BLOCK:
        return new McInputBinding(settings.keyBindAttack);
      case INTERACT:
        return new McInputBinding(settings.keyBindUseItem);
    }
    return null;
  }

  @Override
  public boolean isPlayerOnGround() {
    final EntityPlayerSP player = getPlayer();
    return player.onGround;
  }

  @Override
  public float getPlayerHealth() {
    final EntityPlayerSP player = getPlayer();
    return player.getHealth();
  }

  @Override
  public void sendMessage(final String msg) {
    SendChat.chat(msg);
  }

  @Override
  public List<ItemStackWrapper> getHotbarItems() {
    final List<ItemStackWrapper> items = new ArrayList<>();
    final InventoryPlayer inventory = getPlayer().inventory;
    for (int i = 0; i < 9; i++) {
      final ItemStack itemStack = inventory.getStackInSlot(i);
      if (itemStack != null) {
        final int itemId = Item.getIdFromItem(itemStack.getItem());
        items.add(new ItemStackWrapper(ItemUtils.getItemLibrary().getItemById(itemId), itemStack.stackSize, i));
      }
    }
    return items;
  }

  @Override
  public void selectHotbarSlot(int slot) {
    final InventoryPlayer inventory = getPlayer().inventory;
    inventory.currentItem = slot;
  }

  @Override
  public List<BlockWrapper> getBlocks() {
    this.blocks.clear();
    final List<BlockWrapper> blocks = new ArrayList<>();
    for (Block block : Block.blockRegistry) {
      final String name = Block.blockRegistry.getNameForObject(block).toString();
      final int id = Block.blockRegistry.getIDForObject(block);
      final boolean isNormalCube = block.getDefaultState().getBlock().isNormalCube();
      blocks.add(new BlockWrapper(id, name, isNormalCube));
      this.blocks.put(name, block);
    }
    return blocks;
  }

  @Override
  public List<ItemWrapper> getItems() {
    this.items.clear();
    final List<ItemWrapper> items = new ArrayList<>();
    for (Item item : Item.itemRegistry) {
      String name = Item.itemRegistry.getNameForObject(item).toString();
      int id = Item.itemRegistry.getIDForObject(item);
      items.add(new ItemWrapper(id, name));
      this.items.put(name, item);
    }
    return items;
  }

  @Override
  public int getBlockId(final BaseBlockPos pos) {
    final Block block = getWorld().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
    return Block.blockRegistry.getIDForObject(block);
  }

  @Override
  public boolean isChunkLoaded(int chunkX, int chunkZ) {
    return getWorld().getChunkFromChunkCoords(chunkX, chunkZ).isLoaded();
  }

  @Override
  public int getItemIdFromBlock(final BlockWrapper block) {
    final Item itemFromBlock = Item.getItemFromBlock(blocks.get(block.getName()));
    return Item.itemRegistry.getIDForObject(itemFromBlock);
  }

  @Override
  public int getBlockIdFromItem(final ItemBlockWrapper item) {
    final ItemBlock itemBlock = (ItemBlock) items.get(item.getName());
    return Block.blockRegistry.getIDForObject(itemBlock.getBlock());
  }

  @Override
  public String getBlockFacing(BaseBlockPos position) {
    return null;
  }

  /*@Override
  public String getBlockFacing(final BaseBlockPos position) {
    final IBlockState blockState = getWorld()
      .getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ()));
    final EnumFacing.Axis facing = blockState.getValue(blockState).getAxis();
    return facing.getName();
  }*/

  @Override
  public boolean isDoorOpen(final BaseBlockPos position) {
    final IBlockState blockState = getWorld()
      .getBlockState(new BlockPos(position.getX(), position.getY(), position.getZ()));
    return blockState.getValue(BlockDoor.OPEN);
  }

  @Override
  public boolean isBlockPassable(final BlockWrapper block, final BaseBlockPos pos) {
    return blocks.get(block.getName()).isPassable(getWorld(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
  }

  @Override
  public float getBreakDuration(ItemWrapper item, BlockWrapper block) {
    final Block mcBlock = blocks.get(block.getName());
    if (item == null) {
      return getBreakDuration(null, mcBlock.getDefaultState());
    } else {
      final Item mcItem = items.get(item.getName());
      return getBreakDuration(new ItemStack(mcItem), mcBlock.getDefaultState());
    }
  }

  /**
   * @param itemStack the used item
   * @param state     the block to break
   * @return the time (in ticks) it takes to break the given block with the given item
   */
  private static float getBreakDuration(ItemStack itemStack, IBlockState state) {
    // sources:
    // https://greyminecraftcoder.blogspot.com/2015/01/calculating-rate-of-damage-when-mining.html
    // https://minecraft.gamepedia.com/Breaking
    final float blockHardness = state.getBlock().getBlockHardness(null, null);
    if (blockHardness < 0) {
      return (float) ActionCosts.get().COST_INFINITE;
    }
    final float playerBreakSpeed = getDigSpeed(itemStack, state);
    final int canHarvestMod = (itemStack != null && itemStack.canHarvestBlock(state.getBlock())) ? 30 : 100;
    final float dmgPerTick = ((playerBreakSpeed / blockHardness) * (1f / canHarvestMod));
    return 1f / dmgPerTick;
  }

  private static float getDigSpeed(ItemStack itemStack, IBlockState state) {
    return getDigSpeed(itemStack, state, false, 0, false, 0, 0, false, false, true);
  }

  /**
   * @param itemStack the used tool or null for hand
   *                  see {//@link net.minecraft.entity.player.EntityPlayer#getDigSpeed(IBlockState, BlockPos)}
   */
  private static float getDigSpeed(
    ItemStack itemStack,
    IBlockState state,
    boolean hasEffectHaste,
    int effectHasteAmplifier,
    boolean hasEffectMiningFatigue,
    int effectMiningFatigueAmplifier,
    int efficiencyModifier,
    boolean aquaAffinityModifier,
    boolean isInsideWater,
    boolean isOnGround
  ) {
    float f = getDestroySpeed(itemStack, state);

    if (f > 1.0F) {
      int i = efficiencyModifier;
      if (i > 0 && (itemStack != null)) {
        f += (float) (i * i + 1);
      }
    }

    if (hasEffectHaste) {
      f *= 1.0F + (float) (effectHasteAmplifier + 1) * 0.2F;
    }

    if (hasEffectMiningFatigue) {
      float f1;
      switch (effectMiningFatigueAmplifier) {
        case 0:
          {
            f1 = 0.3F;
            break;
          }
        case 1:
          {
            f1 = 0.09F;
            break;
          }
        case 2:
          {
            f1 = 0.0027F;
            break;
          }
        case 3:
        default:
          {
            f1 = 8.1E-4F;
          }
      }
      f *= f1;
    }

    if (isInsideWater && !aquaAffinityModifier) {
      f /= 5.0F;
    }

    if (!isOnGround) {
      f /= 5.0F;
    }

    return (f < 0 ? 0 : f);
  }

  /**
   * @param itemStack the used tool or null for hand
   * @param state     the block
   */
  private static float getDestroySpeed(ItemStack itemStack, IBlockState state) {
    float f = 1.0F;
    /*if (itemStack != null) {
      f *= itemStack.getDestroySpeed(state);
    }*/
    return f;
  }
}
