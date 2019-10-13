package kaptainwutax.itraders.entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kaptainwutax.itraders.SkinProfile;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.S2CFighterHeight;
import kaptainwutax.itraders.util.Product;
import kaptainwutax.itraders.util.TieredLoot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFighter extends EntityZombie {
	
	public final SkinProfile skin = new SkinProfile();	
	public String lastName = "Fighter";
	
	//public float sizeMultiplier = this.rand.nextFloat() * 2.0f + 0.8f;
	public float sizeMultiplier = 0.5f;
	public TieredLoot loot = TieredLoot.get(TieredLoot.Tier.COMMON);
	
	public EntityFighter(World world) {
		super(world);			
		this.setCustomNameTag(lastName);			
		
		if(!this.world.isRemote) {
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
					.setBaseValue(this.rand.nextFloat() * 0.25d + 0.25d);	
			
			 this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
			 
			 this.loot = this.getFromSize(this.sizeMultiplier);
		} else {
			this.sizeMultiplier = 0.0f;
		}
	}
	
	public TieredLoot getFromSize(float m) {
		if(m <= 1.0f)return TieredLoot.get(TieredLoot.Tier.COMMON);
		else if(m <= 2.5f)return TieredLoot.get(TieredLoot.Tier.RARE);
		else return TieredLoot.get(TieredLoot.Tier.EPIC);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean canPickUpLoot() {
		return true;
	}
	
	@Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
    }
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(this.isDead)return;
		
		if(this.world.isRemote) {
			String name = this.getCustomNameTag();
			
			if(!lastName.equals(name)) {
				this.skin.updateSkin(name);
				this.lastName = name;
			}
		} else {
			double amplitude = this.motionX * this.motionX + this.motionZ * this.motionZ;

			if(amplitude > 0.0034D) {
				this.setSprinting(true);
				this.getJumpHelper().setJumping();
			} else {
				this.setSprinting(false);				
			}
		}
		
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);		
		compound.setFloat("SizeMultiplier", this.sizeMultiplier);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);		
		if(compound.hasKey("SizeMultiplier"))this.changeSize(compound.getFloat("SizeMultiplier"));	
		
		boolean needsGear = true;
		
        for(EntityEquipmentSlot s: EntityEquipmentSlot.values()) {
        	if(s == EntityEquipmentSlot.HEAD)continue;
        	ItemStack stack = this.getItemStackFromSlot(s);
        	needsGear &= stack.getCount() == 0;
        }
		
        if(needsGear) {
	        for(EntityEquipmentSlot s: EntityEquipmentSlot.values()) {
	        	if(s == EntityEquipmentSlot.HEAD)continue;
	        	this.setItemStackToSlot(s, this.loot.enchant(this.loot.getRandomLoot(s)));
	        }
        }
	}
	
	public void changeSize(float m) {
		if(m == this.sizeMultiplier)return;
		this.sizeMultiplier = m;
		this.setSize(0.6F * m, 1.95F * m);
		this.multiplySize(1);

		if(!this.world.isRemote) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
				.setBaseValue(this.rand.nextInt(91) + (10 * m));	
			this.setHealth(this.getMaxHealth());			
			
			this.loot = this.getFromSize(this.sizeMultiplier);
			InitPacket.PIPELINE.sendToAllTracking(new S2CFighterHeight(this), this);
		}
	}
	
	@Override
	public float getEyeHeight() {
		return 1.74f * this.sizeMultiplier;
	}
	
	@Override
	public boolean isChild() {
		return false;
	}
	
	@Override
	public void setChild(boolean childZombie) {
		return;
	}
	
	@Override
	protected boolean shouldBurnInDay() {
		return false;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData livingdata1 = super.onInitialSpawn(difficulty, livingdata);
		this.setCustomNameTag(this.getCustomNameTag());	
		return livingdata1;
	}
	
	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		super.dropEquipment(wasRecentlyHit, lootingModifier);
		if(this.world.isRemote)return;
		
		List<Product> drops = InitConfig.CONFIG_FIGHTER.LOOT.stream()
				.filter(product -> product.isValid()).collect(Collectors.toList());

		Collections.shuffle(drops);
	
		for(int i = 0; i < Math.min(drops.size(), InitConfig.CONFIG_FIGHTER.LOOT_COUNT); i++) {
			Product drop = drops.get(i);
			this.entityDropItem(drop.toStack(), 0.0F);
		}
		
		if(this.getCustomNameTag() != this.lastName) {
			ItemStack headDrop = new ItemStack(Items.SKULL, 1, 3);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("SkullOwner", this.getCustomNameTag());
			headDrop.setTagCompound(nbt);
			this.entityDropItem(headDrop, 0.0F);
		}		
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if(!this.world.isRemote) {
			((WorldServer)this.world).spawnParticle(
					EnumParticleTypes.SWEEP_ATTACK,
					this.posX, this.posY, this.posZ, 
					1, 0.0f, 0.0f, 0.0f, 0
			);
			
			((WorldServer)this.world).playSound(
					null, this.getPosition(), 
					SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 
					SoundCategory.PLAYERS, 1.0f, 
					this.rand.nextFloat() - this.rand.nextFloat()
			);
		}
		
		return super.attackEntityAsMob(entity);
	}
	
}
