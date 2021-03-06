package iskallia.itraders.entity;

import javax.annotation.Nonnull;

import iskallia.itraders.init.InitConfig;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAccelerator extends EntityLivingBase {

	private int timeRemaining;
	private BlockPos target;

	public final SkinProfile skin = new SkinProfile();
	private String previousName = "";
	private EnumFacing facing;

	public EntityAccelerator(World world) {
		super(world);
		this.setSize(0.1f, 0.1f);
		this.noClip = true;
	}

	public EntityAccelerator(World worldIn, String name, BlockPos target) {
		super(worldIn);

		this.target = target;

		this.setCustomNameTag(name);
		this.setAlwaysRenderNameTag(false);
	}

	public EnumFacing getFacing() {
		return facing;
	}

	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (previousName.isEmpty() && this.world.isRemote) {
			String name = this.getCustomNameTag();
			previousName = name;
			this.skin.updateSkin(name);
		}

		if (this.world.isRemote)
			return;

		TileEntity te = this.world.getTileEntity(this.target);
		if (te != null && te instanceof ITickable) {
			for (int i = 0; i < InitConfig.CONFIG_ACCELERATION_BOTTLE.SPEED_MULTIPLIER; i++) {
				((ITickable) te).update();
			}
		} else {
			this.setDead();
		}

		this.timeRemaining -= 1;

		if (this.timeRemaining == 0)
			this.setDead();
	}

	@Override
	public void onUpdate() {
		
		float rotation = 0f;
		float headRotation = 0f;

		super.onUpdate();
		if (this.world.isRemote)
			return;

		if(this.facing != null) {
			headRotation = this.facing.getHorizontalAngle();
			rotation = headRotation + 40f;
		}

		if (this.rotationYaw < rotation)
			this.rotationYaw += 20f;
		if (this.rotationYawHead < headRotation)
			this.rotationYawHead += 20f;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.timeRemaining = compound.getInteger("TimeRemaining");
		this.target = NBTUtil.getPosFromTag(compound.getCompoundTag("Target"));
		this.previousName = "";
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("TimeRemaining", this.timeRemaining);
		compound.setTag("Target", NBTUtil.createPosTag(this.target));
		super.writeEntityToNBT(compound);
	}

	public int getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public BlockPos getTarget() {
		return target;
	}

	public void setTarget(BlockPos target) {
		this.target = target;
	}

	@Nonnull
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
	}

	@Nonnull
	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		return super.writeToNBTOptional(compound);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public boolean attackable() {
		return false;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
	}

	@Override
	protected void collideWithNearbyEntities() {
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	public float getFacingAngle(EnumFacing facing) {
		switch (facing) {
		case NORTH:
			return 0;
		case SOUTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
		default:
			return -90;
		}
	}

}
