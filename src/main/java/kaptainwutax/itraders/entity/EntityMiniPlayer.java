package kaptainwutax.itraders.entity;

import kaptainwutax.itraders.SkinProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class EntityMiniPlayer extends EntityLivingBase {

    public EntityLivingBase owner;

    public final SkinProfile skin = new SkinProfile();
    private String previousName = "";

    public NonNullList<ItemStack> armors = NonNullList.withSize(4, ItemStack.EMPTY);

    public EntityMiniPlayer(World world) {
        super(world);
        setSize(0.25f, 0.25f);
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
    public boolean attackEntityAsMob(Entity entityIn) { return false; }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) { return false; }

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
    protected void collideWithEntity(Entity entityIn) { }

    @Override
    protected void collideWithNearbyEntities() { }

    @Override
    public boolean canBePushed() {
        return false;
    }

    public void setOwner(UUID ownerUUID) {
        this.setOwner(world.getPlayerEntityByUUID(ownerUUID));
    }

    public void setOwner(EntityLivingBase owner) {
        this.owner = world.getPlayerEntityByUUID(owner.getUniqueID());
        this.setPosition(owner.posX, owner.posY, owner.posZ);
        System.out.println("SET " + this.owner);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void onLivingUpdate() {
        if (this.owner == null) {
            System.out.println("DED @ " + posX + " " + posY + " " + posZ + " " + owner);
            setDead();
            return;
        }

        this.setPosition(
                this.owner.posX,
                this.owner.posY + 1.60f,
                this.owner.posZ
        );
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote) {
            String name = this.getCustomNameTag();

            if (!previousName.equals(name)) {
                this.skin.updateSkin(name);
                this.previousName = name;
            }
        }
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return armors;
    }

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) { }

    @Nonnull
    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

}
