package iskallia.itraders.entity;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityAccelerator extends EntityLivingBase implements IEntityAdditionalSpawnData {

	private int timeRemaining;
	private BlockPos target;

	// private static final DataParameter<Integer> TIME_REMAINING =
	// EntityDataManager.<Integer>createKey(EntityAccelerator.class,
	// DataSerializers.VARINT);

	public final SkinProfile skin = new SkinProfile();
	private String previousName = "";
	private String subName;

	public EntityAccelerator(World world) {
		super(world);
		this.setSize(0.25f, 0.25f);
		this.noClip = true;
		// this.dataManager.register(TIME_REMAINING, 0);
	}

	public EntityAccelerator(World worldIn, String name, BlockPos target) {
		super(worldIn);

		this.subName = name;
		this.target = target;

		this.setSize(0.1f, 0.1f);

		this.setPosition(target.getX() + 0.5D, target.getY() + 1.0D, target.getZ() + 0.5D);

		IBlockState state = worldIn.getBlockState(target);

		if (state.getProperties().containsKey(BlockHorizontal.FACING)) {
			EnumFacing facing = (EnumFacing) state.getProperties().get(BlockHorizontal.FACING);
			this.setRotation(facing.getHorizontalAngle(), 0.0f);
			this.setRotationYawHead(facing.getHorizontalAngle());

		}

		this.noClip = true;

		this.setCustomNameTag(name);
		this.setAlwaysRenderNameTag(false);
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
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
		}

		this.timeRemaining -= 1;

		if (this.timeRemaining == 0)
			this.setDead();
	}

	@Override
	protected void entityInit() {
		super.entityInit();

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO Auto-generated method stub

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
		return false; // Disable saving of mini ghosts
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

}
