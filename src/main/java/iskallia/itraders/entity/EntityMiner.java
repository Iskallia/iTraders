package iskallia.itraders.entity;

import java.util.List;

import com.mojang.authlib.GameProfile;

import iskallia.itraders.block.entity.TileEntityGraveStone;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.net.FakeDigManager;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityMiner extends EntityCreature {

	public SkinProfile skin;	
	private String lastName = "Miner";
	
	public FakeUser fakePlayer;
	public FakeDigManager digManager;
	private EnumFacing miningDirection;
	
	private ItemStackHandler minerInventory = new ItemStackHandler(3 * 9 - 2);
	private BlockPos startMiningPosition;
	private int miningDistance;
	private int months;
	private int blocksMined;
	
	public EntityMiner(World world) {
		super(world);
		this.setCustomNameTag(this.lastName);		
		this.setCanPickUpLoot(true);
		
		if(!this.world.isRemote) {
			this.fakePlayer = new FakeUser((WorldServer)this.world, new GameProfile(null, this.lastName));		
			this.digManager = new FakeDigManager(this.fakePlayer);
		} else {
			this.skin = new SkinProfile();
		}
	}
	
	@Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        if(world.isRemote)return;
        ItemStack itemstack = itemEntity.getItem();

        for(int i = 0; i < this.minerInventory.getSlots() && !itemstack.isEmpty(); i++) {
        	itemstack = this.minerInventory.insertItem(i, itemstack, false);
        }
        
        if(itemEntity.getItem().getCount() == itemstack.getCount())return;
        
        if(itemstack.isEmpty()) {
            itemEntity.setDead();
        } else {
        	itemEntity.getItem().setCount(itemstack.getCount());
        }
        
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 1.4F + 2.0F);
    }
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.startMiningPosition = this.getPosition();
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();		
		
		if(this.world.isRemote) {
			String name = this.getCustomNameTag();
	
			if(!lastName.equals(name)) {			
				this.skin.updateSkin(name);			
				this.lastName = name;
			}
		}
		
		if(!world.isRemote) {    
			this.handleMining();
		}	
		
		this.updateArmSwingProgress();		
		
        List<Entity> xpOrbs = this.world.getEntitiesInAABBexcluding(
        		this, this.getEntityBoundingBox().grow(1.2f, 1.0f, 1.2f), 
        		entity -> {return entity instanceof EntityXPOrb;}
        	);

        for(int i = 0; i < xpOrbs.size(); ++i) {
            Entity entity = xpOrbs.get(i);

            if(!entity.isDead) {
            	entity.setDead();
            }
        }
	}
	
	private void handleMining() {
		double distanceSq = this.getPosition().distanceSq(this.startMiningPosition);
		
		if(distanceSq > this.miningDistance * this.miningDistance) {
			this.attackEntityFrom(DamageSource.FLY_INTO_WALL, Float.MAX_VALUE);
		}
		
		if(this.blocksMined >= this.miningDistance * 2) {
			this.attackEntityFrom(DamageSource.FLY_INTO_WALL, Float.MAX_VALUE);
		}
		
		if(this.ticksExisted >= 20 * 60 * 5) {
			this.attackEntityFrom(DamageSource.STARVE, Float.MAX_VALUE);
		}
		
		if(this.dead)return;
		
		this.rotationYaw = this.miningDirection.getHorizontalAngle();
		this.rotationYawHead = this.miningDirection.getHorizontalAngle();	
		this.fakePlayer.rotationYaw = this.miningDirection.getHorizontalAngle();
		this.fakePlayer.rotationYawHead = this.miningDirection.getHorizontalAngle();	
		this.fakePlayer.posX = this.posX;
		this.fakePlayer.posY = this.posY;
		this.fakePlayer.posZ = this.posZ;
		
		if(this.miningDirection == null) {
			this.miningDirection = EnumFacing.Plane.HORIZONTAL.random(this.rand);
		}
		
		boolean mining = false;
		
		for(int i = 1; i >= 0; i--) {
			BlockPos pos = this.getPosition().up(i).offset(this.miningDirection);
			
			if(!this.inAirOrLiquid(pos)) {
				this.digManager.onPlayerDamageBlock(pos, EnumFacing.NORTH, () -> {
					this.blocksMined++;
				});			
				this.swingArm(EnumHand.MAIN_HAND);
				mining = true;
				break;
			}
		}	
		
		if(!mining) {
			BlockPos target = this.getPosition().offset(this.miningDirection, 1);
			this.navigator.setPath(this.navigator.getPathToPos(target), 0.5f);
			
			this.placeTorch(this.getPosition());
			
			if(this.inAirOrLiquid(target.down())) {
		        for(int i = 0; i < this.minerInventory.getSlots(); i++) {
		        	ItemStack stack = this.minerInventory.getStackInSlot(i).copy();
		        	
		        	if(!stack.isEmpty()) {
		        		if(stack.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
		        			this.world.setBlockState(target.down(), Blocks.COBBLESTONE.getDefaultState());
		        		} if(stack.getItem() == Item.getItemFromBlock(Blocks.STONE)) {
		        			this.world.setBlockState(target.down(), Blocks.STONE.getDefaultState());
		        		} else {
		        			continue;
		        		}
		        		
		        		this.world.playSound(null, target.down(), SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, SoundType.STONE.getVolume(), SoundType.STONE.getPitch());
		        		stack.setCount(stack.getCount() - 1);
		        		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack.copy());
		        		this.swingArm(EnumHand.OFF_HAND);
		        		this.minerInventory.setStackInSlot(i, stack);
		        		break;
		        	}
		        }
			}
		}			
	}

	private void placeTorch(BlockPos pos) {
		int lightLevel = this.world.getLightFor(EnumSkyBlock.BLOCK, pos);

		if(lightLevel <= 7 && this.world.isAirBlock(pos) && ((BlockTorch)Blocks.TORCH).canPlaceBlockAt(this.world, pos)) {
    		this.world.setBlockState(pos, Blocks.TORCH.getDefaultState());
    		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Item.getItemFromBlock(Blocks.TORCH)));
    		this.swingArm(EnumHand.OFF_HAND);
    		this.world.playSound(null, pos, SoundType.WOOD.getPlaceSound(), SoundCategory.BLOCKS, SoundType.WOOD.getVolume(), SoundType.WOOD.getPitch());
		}
	}

	public boolean inAirOrLiquid(BlockPos pos) {
		IBlockState state = this.world.getBlockState(pos);
		return state.getMaterial() == Material.AIR 
				|| state.getMaterial() == Material.WATER
				|| state.getMaterial() == Material.LAVA;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
		if(this.miningDirection != null) {
			compound.setInteger("MiningDirection", this.miningDirection.ordinal());
		}
		
		compound.setTag("MinerInventory", this.minerInventory.serializeNBT());		
		compound.setLong("StartMiningPosition", this.startMiningPosition.toLong());
		compound.setInteger("MiningDistance", this.miningDistance);
		compound.setInteger("TicksExisted", this.ticksExisted);
		compound.setInteger("BlocksMined", this.blocksMined);
		
		NBTTagCompound subData = new NBTTagCompound();
		subData.setInteger("Months", this.months);
		compound.setTag("SubData", subData);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		if(compound.hasKey("MiningDirection")) {
			this.miningDirection = EnumFacing.VALUES[compound.getInteger("MiningDirection")];
		}
		
		this.minerInventory.deserializeNBT(compound.getCompoundTag("MinerInventory"));
		this.startMiningPosition = BlockPos.fromLong(compound.getLong("StartMiningPosition"));
		this.miningDistance = compound.getInteger("MiningDistance");
		this.ticksExisted = compound.getInteger("TicksExisted");
		this.blocksMined = compound.getInteger("BlocksMined");
		this.months = compound.getCompoundTag("SubData").getInteger("Months");
		
		this.fakePlayer.setHeldItem(EnumHand.MAIN_HAND, this.getHeldItemMainhand());	
	}
	
	@Override
	public void addPotionEffect(PotionEffect potioneffect) {
		super.addPotionEffect(potioneffect);
		this.fakePlayer.addPotionEffect(potioneffect);
	}

	public void setMiningDirection(EnumFacing facing) {
		if(facing == null || facing == EnumFacing.UP || facing == EnumFacing.DOWN)return;
		this.miningDirection = facing;	
	}

	@Override
	public void onDeath(DamageSource cause) {	
		if(!this.world.isRemote) {
			this.digManager.clickBlock(BlockPos.ORIGIN, EnumFacing.DOWN);
			
			this.placeGraveStone();
			
			this.world.setBlockState(this.getPosition(), Blocks.CHEST.getDefaultState().withProperty(Blocks.CHEST.FACING, this.miningDirection.getOpposite()));
			
			TileEntity tileEntity = world.getTileEntity(this.getPosition());
			
			if(tileEntity instanceof TileEntityChest) {
				TileEntityChest chestTileEntity = (TileEntityChest)tileEntity;
				IItemHandler itemHandler = chestTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
				
				int lastIndex = -1;
				
				for(int i = 0; i < this.minerInventory.getSlots(); i++) {
					ItemStack stackInSlot = this.minerInventory.getStackInSlot(i);		
					if(stackInSlot.isEmpty())break;
					itemHandler.insertItem(i, stackInSlot, false);
					lastIndex = i;
				}			
				
				ItemStack pickaxe = this.getHeldItem(EnumHand.MAIN_HAND);
				
				if(!pickaxe.isEmpty() && itemHandler.getSlots() > lastIndex + 1) {
					itemHandler.insertItem(lastIndex + 1, pickaxe, false);
				}
				
				if(!this.getCustomNameTag().equals(this.lastName)) {
					ItemStack headDrop = new ItemStack(Items.SKULL, 1, 3);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("SkullOwner", this.getCustomNameTag());
					headDrop.setTagCompound(nbt);
					
					if(!headDrop.isEmpty() && itemHandler.getSlots() > lastIndex + 2) {
						itemHandler.insertItem(lastIndex + 2, headDrop, false);
					}
				}
			}		
		}
		
		super.onDeath(cause);
	}
	
	public void placeGraveStone() {
		this.world.setBlockState(this.getPosition().up(), InitBlock.GRAVE_STONE.getDefaultState().withProperty(InitBlock.GRAVE_STONE.FACING, this.miningDirection.getOpposite()));
	
		TileEntity tileEntity = world.getTileEntity(this.getPosition().up());
		
		if(tileEntity instanceof TileEntityGraveStone) {
			TileEntityGraveStone graveStoneTE = (TileEntityGraveStone)tileEntity;
			graveStoneTE.setName(this.getCustomNameTag());
			graveStoneTE.setMonths(this.months);
			graveStoneTE.setBlocksMined(this.blocksMined);
			this.world.setTileEntity(this.getPosition().up(), graveStoneTE);
		}
	}
	
	public void setMiningDistance(int months) {	
		this.miningDistance = months * 5 + 10;
	}
	
}
