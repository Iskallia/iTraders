package kaptainwutax.itraders.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class ItemSpawnEgg extends Item {

	protected ResourceLocation entityName;

	public ItemSpawnEgg(String name, ResourceLocation entityName) {
		this.setTranslationKey(name);
		this.setRegistryName(Traders.getResource(name));
		this.entityName = entityName;
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		return this.onItemUse(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (block == Blocks.MOB_SPAWNER) {
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (tileentity instanceof TileEntityMobSpawner) {
					MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
					mobspawnerbaselogic.setEntityId(getNamedIdFrom(stack));
					tileentity.markDirty();
					worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			}

			BlockPos blockpos = pos.offset(facing);
			double d0 = this.getYOffset(worldIn, blockpos);

			this.doSpawning(worldIn, blockpos, stack);

			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		}
	}

	protected double getYOffset(World world, BlockPos pos) {
		AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expand(0.0D, -1.0D, 0.0D);
		List<AxisAlignedBB> list = world.getCollisionBoxes((Entity) null, axisalignedbb);

		if (list.isEmpty()) {
			return 0.0D;
		} else {
			double d0 = axisalignedbb.minY;

			for (AxisAlignedBB axisalignedbb1 : list) {
				d0 = Math.max(axisalignedbb1.maxY, d0);
			}

			return d0 - (double) pos.getY();
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}

		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

		if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();

			if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
				return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
			} else if (worldIn.isBlockModifiable(playerIn, blockpos)
					&& playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, stack)) {
				this.doSpawning(worldIn, blockpos, stack);

				if (!playerIn.capabilities.isCreativeMode) {
					stack.shrink(1);
				}

				playerIn.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			} else {
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		} else {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
	}

	protected abstract void doSpawning(World worldIn, BlockPos blockpos, ItemStack stack);

	@Nullable
	public ResourceLocation getNamedIdFrom(ItemStack stack) {
		return this.entityName;
	}

	@Nullable
	public static Entity spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y,
			double z) {
		if (entityID != null) {
			Entity entity = null;
			entity = EntityList.createEntityByIDFromName(entityID, worldIn);

			if (entity instanceof EntityLiving) {
				EntityLiving entityliving = (EntityLiving) entity;
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)),
						(IEntityLivingData) null);
				worldIn.spawnEntity(entity);
				entityliving.playLivingSound();
			}

			return entity;
		} else {
			return null;
		}
	}

	public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack,
			@Nullable Entity targetEntity) {
		MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

		if (minecraftserver != null && targetEntity != null) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
				if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null
						|| !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile()))) {
					return;
				}

				NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
				UUID uuid = targetEntity.getUniqueID();
				nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
				targetEntity.setUniqueId(uuid);
				targetEntity.readFromNBT(nbttagcompound1);
			}
		}
	}

}
