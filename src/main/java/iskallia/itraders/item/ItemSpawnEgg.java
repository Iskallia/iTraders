package iskallia.itraders.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.util.profile.UserNameChecker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemSpawnEgg<T> extends Item {

	protected ResourceLocation entityName;

	public ItemSpawnEgg(String name, ResourceLocation entityName, boolean usesDispenser) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.entityName = entityName;
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
		
		if(usesDispenser) {
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem() {
				public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
					EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
					double x = source.getX() + facing.getFrontOffsetX();
					double y = source.getBlockPos().getY() + facing.getFrontOffsetY() + 0.2D;
					double z = source.getZ() + facing.getFrontOffsetZ();
					
					onDispenserSpawn(source.getWorld(), facing, new BlockPos(x, y, z), stack);

					stack.shrink(1);
					return stack;
				}
			});
		}
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		return this.onItemUse(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = world.getBlockState(pos);
			Block block = iblockstate.getBlock();
			/*
			if (block == Blocks.MOB_SPAWNER) {
				TileEntity tileentity = world.getTileEntity(pos);

				if (tileentity instanceof TileEntityMobSpawner) {
					MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
					mobspawnerbaselogic.setEntityId(getNamedIdFrom(stack));
					tileentity.markDirty();
					world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			}
			*/
			BlockPos blockpos = pos.offset(facing);
			double d0 = this.getYOffset(world, blockpos);

			this.onPlayerSpawn(world, player, blockpos, stack);

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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (world.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}

		RayTraceResult raytraceresult = this.rayTrace(world, player, true);

		if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();

			if (!(world.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
				return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
			} else if (world.isBlockModifiable(player, blockpos)
					&& player.canPlayerEdit(blockpos, raytraceresult.sideHit, stack)) {
				this.onPlayerSpawn(world, player, blockpos, stack);

				if (!player.capabilities.isCreativeMode) {
					stack.shrink(1);
				}

				player.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			} else {
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		} else {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
	}

	protected abstract T onPlayerSpawn(World world, EntityPlayer player, BlockPos pos, ItemStack stack);
	protected abstract T onDispenserSpawn(World world, EnumFacing facing, BlockPos pos, ItemStack stack);
	
	@Nullable
	public ResourceLocation getNamedIdFrom(ItemStack stack) {
		return this.entityName;
	}

	@Nullable
	public T spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y,
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

			return (T)entity;
		} else {
			return null;
		}
	}

	public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity) {
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag isAdvanced) {
		tooltip.set(0, UserNameChecker.getTextFormattingFromItem(stack) + tooltip.get(0));
	}

	@Override
	public String getHighlightTip(ItemStack item, String displayName) {
		return UserNameChecker.getTextFormattingFromItem(item) + displayName;
	}

}
