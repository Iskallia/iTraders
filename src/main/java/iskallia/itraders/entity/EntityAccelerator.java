package iskallia.itraders.entity;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityAccelerator extends EntityLivingBase implements IEntityAdditionalSpawnData {

	private int timeRemaining;
	private BlockPos target;

	public final SkinProfile skin = new SkinProfile();
	private String previousName = "";

	public EntityAccelerator(World world) {
		super(world);
		this.setSize(0.1f, 0.1f);
		this.noClip = true;
	}

	public EntityAccelerator(World worldIn, String name, BlockPos target) {
		super(worldIn);

		this.target = target;

		this.setSize(0.1f, 0.1f);

		this.setPosition(target.getX() + 0.5D, target.getY() + 1.5D, target.getZ() + 0.5D);

		this.noClip = true;

		this.setCustomNameTag(name);
		this.setAlwaysRenderNameTag(true);
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.world.isRemote) {
			String name = this.getCustomNameTag();
			if (!previousName.equals(name)) {
				this.skin.updateSkin(name);
				this.previousName = name;
			}
		}
		this.timeRemaining -= 1;

		if (this.timeRemaining == 0 && !this.world.isRemote)
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
