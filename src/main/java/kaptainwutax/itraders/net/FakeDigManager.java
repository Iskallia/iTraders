package kaptainwutax.itraders.net;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class FakeDigManager {

	private EntityPlayerMP player;
	
    private BlockPos currentBlock = new BlockPos(-1, -1, -1);
    private ItemStack currentItemHittingBlock = ItemStack.EMPTY;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private GameType currentGameType = GameType.SURVIVAL;
    private int currentPlayerItem;
	
	public FakeDigManager(EntityPlayerMP player) {
		this.player = player;
	}
	
    public boolean clickBlock(BlockPos loc, EnumFacing face) {
    	if(!this.isHittingBlock || !this.isHittingPosition(loc)) {
            if(this.isHittingBlock) {
            	this.player.interactionManager.cancelDestroyingBlock();
            }

            IBlockState iblockstate = this.player.world.getBlockState(loc);
            this.player.interactionManager.onBlockClicked(loc, face);
            
            boolean flag = iblockstate.getMaterial() != Material.AIR;

            if(flag && this.curBlockDamageMP == 0.0F) {
                iblockstate.getBlock().onBlockClicked(this.player.world, loc, this.player);
            }

            if(flag && iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, loc) >= 1.0F) {
            	this.onPlayerDestroyBlock(loc);
            	this.player.interactionManager.blockRemoving(loc);
            } else {
                this.isHittingBlock = true;
                this.currentBlock = loc;
                this.currentItemHittingBlock = this.player.getHeldItemMainhand();
                this.curBlockDamageMP = 0.0F;
                this.stepSoundTickCounter = 0.0F;
                this.player.world.sendBlockBreakProgress(this.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
            }
        }

        return true;
    }
    
    public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing) {
        if(this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        } else if(this.isHittingPosition(posBlock)) {
            IBlockState iblockstate = this.player.world.getBlockState(posBlock);
            Block block = iblockstate.getBlock();

            if(iblockstate.getMaterial() == Material.AIR) {
                this.isHittingBlock = false;
                return false;
            } else {
                float increment = ForgeHooks.blockStrength(iblockstate, this.player, this.player.world, posBlock);
                this.curBlockDamageMP += 4 * increment;
                
                if(this.stepSoundTickCounter % 4.0F == 0.0F) {
                    SoundType soundtype = block.getSoundType(iblockstate, this.player.world, posBlock, this.player);
                    this.player.world.playSound(null, posBlock, soundtype.getHitSound(), SoundCategory.NEUTRAL, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F);
                }

                ++this.stepSoundTickCounter;

                if(this.curBlockDamageMP >= 1.0F) {
                    this.isHittingBlock = false;
                    this.onPlayerDestroyBlock(posBlock);
                    this.player.interactionManager.blockRemoving(posBlock);
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.blockHitDelay = 5;
                }

                this.player.world.sendBlockBreakProgress(this.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
                return true;
            }
        } else {
            return this.clickBlock(posBlock, directionFacing);
        }
    }
    
    private boolean isHittingPosition(BlockPos pos) {
        ItemStack itemstack = this.player.getHeldItemMainhand();
        boolean flag = this.currentItemHittingBlock.isEmpty() && itemstack.isEmpty();

        if(!this.currentItemHittingBlock.isEmpty() && !itemstack.isEmpty()) {
            flag = !ForgeHooksClient.shouldCauseBlockBreakReset(this.currentItemHittingBlock, itemstack);
        }

        return pos.equals(this.currentBlock) && flag;
    }
    
    public boolean onPlayerDestroyBlock(BlockPos pos) {
        ItemStack stack = this.player.getHeldItemMainhand();
        
        if(!stack.isEmpty() && stack.getItem().onBlockStartBreak(stack, pos, this.player)) {
            return false;
        }

        if(this.currentGameType.isCreative() && !stack.isEmpty() && !stack.getItem().canDestroyBlockInCreative(this.player.world, pos, stack, this.player)) {
            return false;
        } else {
            World world = this.player.world;
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.player.canUseCommandBlock()) {
                return false;
            } else if(iblockstate.getMaterial() == Material.AIR) {
                return false;
            } else {
                world.playEvent(2001, pos, Block.getStateId(iblockstate));

                this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());

                if(!this.currentGameType.isCreative()) {
                    ItemStack itemstack1 = this.player.getHeldItemMainhand();
                    ItemStack copyBeforeUse = itemstack1.copy();

                    if(!itemstack1.isEmpty()) {
                        itemstack1.onBlockDestroyed(world, iblockstate, pos, this.player);

                        if(itemstack1.isEmpty()) {
                            ForgeEventFactory.onPlayerDestroyItem(this.player, copyBeforeUse, EnumHand.MAIN_HAND);
                            this.player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    }
                }          
             
                return this.player.interactionManager.tryHarvestBlock(pos);
            }
        }
    }

}
