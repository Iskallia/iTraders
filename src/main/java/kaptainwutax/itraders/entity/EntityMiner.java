package kaptainwutax.itraders.entity;

import com.mojang.authlib.GameProfile;

import kaptainwutax.itraders.SkinProfile;
import kaptainwutax.itraders.net.FakeDigManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityMiner extends EntityCreature {

	public final SkinProfile skin = new SkinProfile();	
	private String lastName = "Miner";
	
	public FakeUser fakePlayer;
	public FakeDigManager digManager;
	private EnumFacing miningDirection;
	
	private ItemStackHandler minerInventory = new ItemStackHandler(3 * 9 - 1);
	private BlockPos startMiningPosition;
	private int miningDistance;
	
	public EntityMiner(World world) {
		super(world);
		this.setCustomNameTag(this.lastName);		
		this.setCanPickUpLoot(true);
		
		if(!this.world.isRemote) {
			this.fakePlayer = new FakeUser((WorldServer)this.world, new GameProfile(null, this.lastName));		
			this.digManager = new FakeDigManager(this.fakePlayer);
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
	public void onUpdate() {
		super.onUpdate();
		
		String name = this.getCustomNameTag();

		if(!lastName.equals(name)) {
			if(this.world.isRemote) {
				this.skin.updateSkin(name);
			}
			
			this.lastName = name;
		}
		
		if(!world.isRemote) {    
			this.handleMining();
		}	
		
		this.updateArmSwingProgress();
	}
	
	private void handleMining() {
		double distanceSq = this.getPosition().distanceSq(this.startMiningPosition);
		
		if(distanceSq > this.miningDistance * this.miningDistance) {
			this.onKillCommand();
		}
		
		if(this.dead)return;
		
		if(this.miningDirection == null) {
			this.miningDirection = EnumFacing.Plane.HORIZONTAL.random(this.rand);
		}
		
		boolean mining = false;
		
		for(int i = 1; i >= 0; i--) {
			BlockPos pos = this.getPosition().up(i).offset(this.miningDirection);
			
			if(!this.world.isAirBlock(pos)) {
				this.digManager.onPlayerDamageBlock(pos, EnumFacing.NORTH);			
				this.swingArm(EnumHand.MAIN_HAND);
				mining = true;
				break;
			}
		}	
		
		if(!mining) {
			this.navigator.setPath(this.navigator.getPathToPos(this.getPosition().offset(this.miningDirection, 1)), 0.5f);
		}			
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
			this.world.setBlockState(this.getPosition(), Blocks.CHEST.getDefaultState());
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
			}		
		}
		
		super.onDeath(cause);
	}
	
	public void setMiningDistance(int subMonths) {
		if(subMonths < 3) {
			this.miningDistance = 10 + this.rand.nextInt(25);
		} else if(subMonths < 6) {
			this.miningDistance = 10 + this.rand.nextInt(55);
		} else if(subMonths < 12) {
			this.miningDistance = 10 + this.rand.nextInt(85);
		} else if(subMonths < 24) {
			this.miningDistance = 10 + this.rand.nextInt(121);
		} else {
			this.miningDistance = 10 + this.rand.nextInt(151);
		}
	}
	
}
