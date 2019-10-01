package kaptainwutax.itraders.entity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;

import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.S2CFighterHeight;
import kaptainwutax.itraders.util.Product;
import kaptainwutax.itraders.util.TieredLoot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.internal.Trees.This;

public class EntityFighter extends EntityZombie {
	
	public AtomicReference<GameProfile> gameProfile = new AtomicReference<GameProfile>();
	public AtomicReference<NetworkPlayerInfo> playerInfo = new AtomicReference<NetworkPlayerInfo>();
	
	public String lastName = "Fighter";
	
	public float sizeMultiplier = this.rand.nextFloat() * 2 + 1;
	public TieredLoot loot = TieredLoot.get(TieredLoot.Tier.COMMON);
	
	public EntityFighter(World world) {
		super(world);			
		this.setCustomNameTag(lastName);			
		
		if(!this.world.isRemote) {
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
					.setBaseValue(this.rand.nextFloat() * 0.2d + 0.22d);	
			
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
        
        for(EntityEquipmentSlot s: EntityEquipmentSlot.values()) {
        	this.setItemStackToSlot(s, this.loot.enchant(this.loot.getRandomLoot(s)));
        }
    }
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(this.isDead)return;
		
		if(this.world.isRemote) {
			String name = this.getCustomNameTag();
			
			if(!lastName.equals(name)) {
				this.updateSkin(name);
			}
		}	
		
		if(!this.world.isRemote && this.ticksExisted % 5 == 0) {
			InitPacket.PIPELINE.sendToAllTracking(new S2CFighterHeight(this), this);
		}
		
		if(this.moveForward > 0.2f) {
			this.setSprinting(true);
			this.getJumpHelper().setJumping();
		} else {
			this.setSprinting(false);
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
		}
		
		this.loot = this.getFromSize(this.sizeMultiplier);
	}
	
	@Override
	public float getEyeHeight() {
		return 1.74f * this.sizeMultiplier;
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

	public void updateSkin(String name) {
		if(!this.world.isRemote)return;
		
		gameProfile.set(new GameProfile(null, name));
		gameProfile.set(TileEntitySkull.updateGameprofile(gameProfile.get()));
		playerInfo.set(new NetworkPlayerInfo(gameProfile.get()));
		this.lastName = name;
	}
	
	@SideOnly(value = Side.CLIENT)
    public ResourceLocation getLocationSkin() {
    	if(this.playerInfo == null || this.playerInfo.get() == null) {
    		return DefaultPlayerSkin.getDefaultSkinLegacy();
    	}		
    	
        return this.playerInfo.get().getLocationSkin();
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
		}
		
		return super.attackEntityAsMob(entity);
	}
	
}
