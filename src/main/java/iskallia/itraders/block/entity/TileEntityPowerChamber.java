package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockDoublePartDirectional;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityPowerChamber extends TileInventoryBase implements IEnergyStorage {
	/*
	## "Power Chamber"
	
	A block that looks like a glass chamber and generates RF / T actively by using the Trader eggs
	
	Right clicking brings up a UI that shows current RF Generation ($Actor is Generating: X RF/T), 
	an off/on status and a fuel slot.
	
	This machine takes a Trader, reads the $donationAmount and returns an 
	RF / T generation like so: rand($donationAmount, ($donationAmount*10)). 
	
	To insert a trader, right click the egg on to the block.
	A trader inserted can not be extracted.
	
	Fuel is required for the machine to run, there for the TOP SIDE of the block should allow 
	itraders:bit_100 as input, and put it in the fuel slot. 
	Every bit_100 burns for 1000 ticks and while the machine
	has fuel it will generate RF as described above.
	
	All sides BUT the top can be used to take a power output.
	
	A redstone signal to the block disables the power generation, and stops burning fuel.
	
	The block should generate the skin of the trader, just like the cryoChamber does.
	*/
	
	@Nonnull
	private SkinProfile skin = new SkinProfile();
	private String nickname;
	
	public TileEntityPowerChamber() {
		super(1);
	}
	
	public ItemStack getContent() {
		return getInventoryHandler().getStackInSlot(0);
	}
	
	public boolean isOccupied() {
		return getContent() != ItemStack.EMPTY;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	@Nonnull
	public SkinProfile getSkin() {
		return skin;
	}
	
	public boolean insertEgg(ItemStack eggStack) {
		if (eggStack.getItem() != InitItem.SPAWN_EGG_TRADER)
			return false;
		
		if (isOccupied())
			return false;
		
		getInventoryHandler().insertItem(0, eggStack, false);
		return true;
    }
	
	@Override
	public void update() {
		if (!world.isRemote) {
			
		}
		
		if (world.isRemote) {
			if (nickname != null) {
				String previousNickname = skin.getLatestNickname();
				
				if (previousNickname == null || !previousNickname.equals(nickname)) {
					skin.updateSkin(nickname);
				}
			}
		}
	}
	
	@Override
	protected ItemHandlerTile createNewItemHandler() {
		return new ItemHandlerTileFiltered(this) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				ItemStack remainingStack = super.insertItem(slot, stack, simulate);
				
				if (remainingStack == ItemStack.EMPTY) {
					nickname = stack.getDisplayName();
				}
				
				return remainingStack;
			}
		};
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound compound) {
		super.readCustomNBT(compound);
		
		this.nickname = compound.getString("Nickname");
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound compound) {
		super.writeCustomNBT(compound);
		
		compound.setString("Nickname", nickname != null ? nickname : "");
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (getBlockState().getValue(BlockDoublePartDirectional.PART) == BlockDoublePartDirectional.EnumPartType.TOP) {
			TileEntity master = getWorld().getTileEntity(pos.down());
			
			if (master != null) return master.hasCapability(capability, facing);
		}
		else
		{
			if (capability != null && capability.getName() == "net.minecraftforge.energy.IEnergyStorage")
			{
				return true;
			}
		}
		
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (getBlockState().getValue(BlockDoublePartDirectional.PART) == BlockDoublePartDirectional.EnumPartType.TOP) {
			TileEntity master = getWorld().getTileEntity(pos.down());
			
			if (master != null) return master.getCapability(capability, facing);
		}
		else
		{
			if (capability != null && capability.getName() == "net.minecraftforge.energy.IEnergyStorage") {
				return (T) this;
			}
		}
		
		return super.getCapability(capability, facing);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}
	
	@Override
	public int getEnergyStored() {
		return 0;
	}
	
	@Override
	public int getMaxEnergyStored() {
		return 0;
	}
	
	@Override
	public boolean canExtract() {
		return false;
	}
	
	@Override
	public boolean canReceive() {
		return false;
	}
}
