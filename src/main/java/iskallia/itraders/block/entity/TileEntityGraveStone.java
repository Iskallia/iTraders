package iskallia.itraders.block.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityGraveStone extends TileEntity {

	private String name = null;
	private int months = -1;
	private int blocksMined = -1;
	
	public TileEntityGraveStone() {
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(this.name != null) {
			compound.setString("Name", this.name);
		}
		
		if(this.months != -1) {
			compound.setInteger("Months", this.months);
		}
		
		if(this.blocksMined != -1) {
			compound.setInteger("BlocksMined", this.blocksMined);
		}
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("Name", Constants.NBT.TAG_STRING)) {
			this.name = compound.getString("Name");
		}
		
		if(compound.hasKey("Months", Constants.NBT.TAG_INT)) {
			this.months = compound.getInteger("Months");
		}
		
		if(compound.hasKey("BlocksMined", Constants.NBT.TAG_INT)) {
			this.blocksMined = compound.getInteger("BlocksMined");
		}
		
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		
		if(this.name != null) {
			nbt.setString("Name", this.name);
		}
		
		if(this.months != -1) {
			nbt.setInteger("Months", this.months);
		}
		
		if(this.blocksMined != -1) {
			nbt.setInteger("BlocksMined", this.blocksMined);
		}
		
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		if(tag.hasKey("Name", Constants.NBT.TAG_STRING)) {
			this.name = tag.getString("Name");
		}
		
		if(tag.hasKey("Months", Constants.NBT.TAG_INT)) {
			this.months = tag.getInteger("Months");
		}
		
		if(tag.hasKey("BlocksMined", Constants.NBT.TAG_INT)) {
			this.blocksMined = tag.getInteger("BlocksMined");
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){	    
	    return new SPacketUpdateTileEntity(getPos(), 1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
	    this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMonths() {
		return this.months;
	}
	
	public int getBlocksMined() {
		return this.blocksMined;
	}
	
	public void setName(String name) {
		this.name = name;
		this.markDirty();
	}
	
	public void setMonths(int months) {
		this.months = months;
		this.markDirty();
	}
	
	public void setBlocksMined(int blockMined) {
		this.blocksMined = blockMined;
		this.markDirty();
	}
	
}
