package kaptainwutax.itraders.block.entity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityGraveStone extends TileEntity {

	private String name = null;
	private int months = -1;
	private long time = -1;
	
	public TileEntityGraveStone() {
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("Name", this.name);
		compound.setInteger("Months", this.months);
		compound.setLong("Time", this.time);
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
		
		if(compound.hasKey("Time", Constants.NBT.TAG_LONG)) {
			this.time = compound.getLong("Time");
		}
		
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMonths() {
		return this.months;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public void setName(String name) {
		this.name = name;
		this.markDirty();
	}
	
	public void setMonths(int months) {
		this.months = months;
		this.markDirty();
	}
	
	public void setTime(long time) {
		this.time = time;
		this.markDirty();
	}
	
	public static class Renderer extends TileEntitySpecialRenderer<TileEntityGraveStone> {
		
		@Override
		public void render(TileEntityGraveStone te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
			super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		}
		
	}
	
}
