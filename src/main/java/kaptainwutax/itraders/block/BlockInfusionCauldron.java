package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSkullNeck;
import kaptainwutax.itraders.tileentity.TileEntityInfusionCauldron;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidUtil;

public class BlockInfusionCauldron extends BlockCauldron {

	public static final double NECKLACE_CREATION_RATE = 0.01d;

	public BlockInfusionCauldron(String name) {
		super();

		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));

		this.setCreativeTab(InitItem.ITRADERS_TAB);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		TileEntityInfusionCauldron te = (TileEntityInfusionCauldron) world.getTileEntity(pos);
		if (te == null)
			return true;

		ItemStack heldStack = player.getHeldItem(hand);
		if (heldStack.getItem() instanceof ItemBucket) {
			FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing);
			return true;
		}

		if (handleSkull(world, pos, heldStack)) {
			if (!player.isCreative())
				heldStack.shrink(1);
		}

		return true;
	}

	private boolean handleSkull(World world, BlockPos pos, ItemStack stack) {

		TileEntityInfusionCauldron te = (TileEntityInfusionCauldron) world.getTileEntity(pos);
		if (te == null)
			return false;

		int currentWaterLevel = te.getTank().getFluidAmount();

		if (currentWaterLevel <= 0)
			return false;

		if (stack.getItem() != Items.SKULL)
			return false;

		NBTTagCompound stackNBT = stack.getTagCompound();

		if (stackNBT == null || !stackNBT.hasKey("SkullOwner", Constants.NBT.TAG_COMPOUND))
			return false;

		NBTTagCompound skullOwnerNBT = stackNBT.getCompoundTag("SkullOwner");

		if (!skullOwnerNBT.hasKey("Name", Constants.NBT.TAG_STRING))
			return false;

		if (Math.random() <= NECKLACE_CREATION_RATE) {
			String ghostName = skullOwnerNBT.getString("Name");
			ItemStack necklaceStack = ItemSkullNeck.generateRandom(ghostName);
			this.spawnNecklace((WorldServer) world, pos, necklaceStack);
		} else {
			this.spawnFailParticles((WorldServer) world, pos);
		}

		int toDrain = currentWaterLevel - 333 < 300 ? te.getTank().getFluidAmount() : 333;
		te.getTank().drain(toDrain, true);	

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(world.isRemote) return; 
		if (entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			ItemStack stack = item.getItem();
			if (handleSkull(world, pos, stack)) {
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

}
