package iskallia.itraders.block.entity;

import java.util.Random;

import iskallia.itraders.block.BlockFacing;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitConfig;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityJar extends TileEntity implements ITickable {

	protected ItemStack donator = ItemStack.EMPTY;
	protected ItemStack food = ItemStack.EMPTY;
	protected ItemStack poop = ItemStack.EMPTY;
	
	public TileEntityJar() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Donator", this.donator.serializeNBT());
		compound.setTag("Food", this.food.serializeNBT());
		compound.setTag("Poop", this.poop.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.donator = new ItemStack(compound.getCompoundTag("Donator"));
		this.food = new ItemStack(compound.getCompoundTag("Food"));
		this.poop = new ItemStack(compound.getCompoundTag("Poop"));
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
		
		this.food.setCount(1);
		this.poop.setCount(1);
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
	
	public void poop() {
		EnumFacing facing = this.getFacing();
		if(facing == null)return;
		facing = facing.getOpposite();
		
		Random rand = this.world.rand;
        double d0 = this.pos.getY() + 0.2F;
        BlockPos pos = this.pos.offset(facing);
        EntityItem item = new EntityItem(this.world, pos.getX() + 0.5f, d0, pos.getZ() + 0.5f, this.poop.copy());
        item.setPickupDelay(40);
        this.world.spawnEntity(item);
	}

	@Override
	public void update() {	
		
	}
	
}
