package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityInfusionCauldron;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSkullNeck;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Random;

public class BlockInfusionCauldron extends BlockCauldron {

	public BlockInfusionCauldron(String name) {
		super();

		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));

		this.setHardness(2f);
		this.setLightLevel(12.0f / 15.0f);
		this.setCreativeTab(InitItem.ITRADERS_TAB);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		TileEntityInfusionCauldron tileEntity = (TileEntityInfusionCauldron) world.getTileEntity(pos);
		if (tileEntity == null)
			return true;

		ItemStack heldStack = player.getHeldItem(hand);
		if (heldStack.getItem() instanceof ItemBucket) {
			FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing);
			return true;
		}

		if (consumeSkull(world, pos, heldStack)) {
			if (!player.isCreative())
				heldStack.shrink(1);
		}

		return true;
	}

	private boolean consumeSkull(World world, BlockPos pos, ItemStack stack) {
		TileEntityInfusionCauldron tileEntity = (TileEntityInfusionCauldron) world.getTileEntity(pos);

		if (tileEntity == null)
			return false;

		int currentWaterLevel = tileEntity.getTank().getFluidAmount();

		if (currentWaterLevel <= 0)
			return false;

		if (stack.getItem() != Items.SKULL)
			return false;

		String name = "";
		NBTTagCompound stackNBT = stack.getTagCompound();

		if(stackNBT == null)
			return false;

		if (stackNBT.hasKey("SkullOwner", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound skullOwnerNBT = stackNBT.getCompoundTag("SkullOwner");

			if (!skullOwnerNBT.hasKey("Name", Constants.NBT.TAG_STRING))
				return false;

			name = skullOwnerNBT.getString("Name");

		} else if (stackNBT.hasKey("SkullOwner", Constants.NBT.TAG_STRING)) {
			name = stackNBT.getString("SkullOwner");

		} else {
			return false;
		}

		// Finally roll the dice!
		if (Math.random() <= InitConfig.CONFIG_SKULL_NECKLACE.NECKLACE_CREATION_RATE) {
			ItemStack necklaceStack = ItemSkullNeck.generateRandom(name);
			this.spawnNecklace((WorldServer) world, pos, necklaceStack);

		} else {
			this.spawnFailParticles((WorldServer) world, pos);
		}

		// ..And drain water from the tank
		int toDrain = currentWaterLevel - 333 < 300 ? tileEntity.getTank().getFluidAmount() : 333;
		tileEntity.getTank().drain(toDrain, true);
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(world.isRemote) return;

		if (entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			ItemStack stack = item.getItem();

			if (consumeSkull(world, pos, stack)) {
				entity.setDead();
			}
		}
	}

	public void spawnNecklace(WorldServer world, BlockPos pos, ItemStack necklaceStack) {
		double itemEntityX = pos.getX() + 0.5d;
		double itemEntityY = pos.getY() + 1.0d;
		double itemEntityZ = pos.getZ() + 0.5d;

		EntityItem itemEntity = new EntityItem(world, itemEntityX, itemEntityY, itemEntityZ, necklaceStack);

		int particleCount = 100;

		world.playSound(null, 
				pos.getX(), pos.getY(), pos.getZ(), 
				SoundEvents.ENTITY_PLAYER_LEVELUP, 
				SoundCategory.MASTER, 1.0f, (float) Math.random());
		world.spawnParticle(EnumParticleTypes.SPELL_WITCH, false, 
				pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, 
				particleCount, 0, 0, 0, Math.PI);
		world.spawnEntity(itemEntity);
	}

	public void spawnFailParticles(WorldServer world, BlockPos pos) {
		int particleCount = 300;

		world.playSound(null, 
				pos.getX(), pos.getY(), pos.getZ(), 
				SoundEvents.ENTITY_ITEM_BREAK, 
				SoundCategory.MASTER, 1.0f, (float) Math.random());
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, 
				pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, 
				particleCount, 0, 0, 0, 0.1d);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityInfusionCauldron();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(InitBlock.ITEM_INFUSION_CAULDRON, 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return InitBlock.ITEM_INFUSION_CAULDRON;
	}

}
