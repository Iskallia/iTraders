package kaptainwutax.itraders.entity;

import com.mojang.authlib.GameProfile;

import kaptainwutax.itraders.SkinProfile;
import kaptainwutax.itraders.net.FakeDigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityMiner extends EntityCreature {
	
	public FakeUser fakePlayer;
	public final SkinProfile skin = new SkinProfile();	
	public FakeDigManager digManager;
	private String lastName = "Miner";
	private EnumFacing currentDirection;
	private int walkTime;
	
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
	public void onItemPickup(Entity entity, int quantity) {
		if(entity instanceof EntityItem) {
			EntityItem entityItem = (EntityItem)entity;
		}
		
		super.onItemPickup(entity, quantity);
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
			if(this.currentDirection == null) {
				this.currentDirection = EnumFacing.Plane.HORIZONTAL.random(this.rand);
			}
			
			boolean mining = false;
			
			for(int i = 1; i >= 0; i--) {
				BlockPos pos = this.getPosition().up(i).offset(this.currentDirection);
				
				if(!this.world.isAirBlock(pos)) {
					this.digManager.onPlayerDamageBlock(pos, EnumFacing.NORTH);
					mining = true;
					break;
				}
			}	
			
			if(!mining) {
				this.navigator.setPath(this.navigator.getPathToPos(this.getPosition().offset(this.currentDirection)), 0.5f);
			}	
			
			this.walkTime++;
		}	
				
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.fakePlayer.setHeldItem(EnumHand.MAIN_HAND, this.getHeldItemMainhand());	
	}
	
	@Override
	public void addPotionEffect(PotionEffect potioneffect) {
		super.addPotionEffect(potioneffect);
		this.fakePlayer.addPotionEffect(potioneffect);
	}

}
