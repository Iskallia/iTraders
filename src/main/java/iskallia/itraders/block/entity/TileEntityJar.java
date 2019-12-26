package iskallia.itraders.block.entity;

import java.util.Random;

import iskallia.itraders.block.BlockFacing;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityJar extends TileEntity implements ITickable {

	protected ItemStack donator = ItemStack.EMPTY;
	protected ItemStack food = ItemStack.EMPTY;
	protected ItemStack poop = ItemStack.EMPTY;
	
	protected ItemStackHandler input = new ItemStackHandler(1) {
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(!this.isItemValid(slot, stack)) {
				return stack;
			}
			
			return super.insertItem(slot, stack, simulate);
		};
		
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		};
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem() == TileEntityJar.this.getFood().getItem();
		}
	};
	
	public int foodCount = 0;
	
	protected SkinProfile skin = null;
	
	public TileEntityJar() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Donator", this.donator.serializeNBT());
		compound.setTag("Food", this.food.serializeNBT());
		compound.setTag("Poop", this.poop.serializeNBT());
		compound.setInteger("FoodCount", this.foodCount);
		compound.setTag("Input", this.input.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.donator = new ItemStack(compound.getCompoundTag("Donator"));
		this.food = new ItemStack(compound.getCompoundTag("Food"));
		this.poop = new ItemStack(compound.getCompoundTag("Poop"));
		this.foodCount = compound.getInteger("FoodCount");
		this.input.deserializeNBT(compound.getCompoundTag("Input"));
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {	  
	    return new SPacketUpdateTileEntity(this.getPos(), 1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
	    this.handleUpdateTag(packet.getNbtCompound());
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? true : super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.input);
        }
            
        return super.getCapability(capability, facing);
    }
	
	public SkinProfile getSkin() {
		if(this.skin == null && this.world.isRemote) {
			this.skin = new SkinProfile();		
		}
		
		return this.skin;
	}
	
	public ItemStack getDonator() {
		return this.donator;
	}
	
	public void setDonator(ItemStack donator) {
		this.donator = donator;
		
		this.food = InitConfig.CONFIG_JAR.FOODS.get(
					this.world.rand.nextInt(InitConfig.CONFIG_JAR.FOODS.size())
				).toStack();	
		
		this.poop = InitConfig.CONFIG_JAR.POOPS.get(
					this.world.rand.nextInt(InitConfig.CONFIG_JAR.POOPS.size())
				).toStack();
		
		this.notifyClients();
	}
	
	public void notifyClients() {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
		this.markDirty();
	}

	public ItemStack getFood() {
		return this.food;
	}
	
	public ItemStack getPoop() {
		return this.poop;
	}
	
	public EnumFacing getFacing() {
		IBlockState state = this.getWorld().getBlockState(this.getPos());
		if(state.getBlock() != InitBlock.JAR)return null;
		EnumFacing facing = state.getValue(BlockFacing.FACING);
		return facing;
	}
	
	public boolean feed(ItemStack stack) {
		if(stack.isEmpty() || stack.getItem() != this.getFood().getItem())return false;
		
		stack.shrink(1);
		this.foodCount++;
		this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1.0f, 0.8f);
		this.notifyClients();
		return true;
	}
	
	public void poop() {
		if(this.foodCount < this.getFood().getCount())return;
		TileEntity te = this.world.getTileEntity(this.pos.down());
		if(te == null || !te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))return;
		
		IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		ItemStack poop = this.getPoop().copy();
		
		for(int i = 0; i < inv.getSlots() && !poop.isEmpty(); i++) {
			poop = inv.insertItem(i, poop, false);
		}
		
		if(poop.getCount() < this.getPoop().getCount()) {
			this.foodCount -= this.getFood().getCount();
			this.notifyClients();
			this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0f, 0.8f);
		}
	}

	@Override
	public void update() {	
		if(this.world.isRemote && !this.donator.isEmpty()) {	
			this.getSkin().updateSkin(this.donator.getDisplayName());
		}	
		
		if(!this.world.isRemote) {
			this.foodCount += this.input.getStackInSlot(0).getCount();
			this.input.setStackInSlot(0, ItemStack.EMPTY);
			this.poop();
		}
	}
	
}
