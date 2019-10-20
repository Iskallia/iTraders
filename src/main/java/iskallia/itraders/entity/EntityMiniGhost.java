package iskallia.itraders.entity;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class EntityMiniGhost extends EntityLivingBase {

    private static final DataParameter<String> PARENT_UUID =
            EntityDataManager.createKey(EntityMiniGhost.class, DataSerializers.STRING);

    public final SkinProfile skin = new SkinProfile();
    private String previousName = "";

    public NonNullList<ItemStack> armors = NonNullList.withSize(4, ItemStack.EMPTY);

    public EntityMiniGhost(World world) {
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

    public void setParentUUID(UUID parentUUID) {
        this.dataManager.set(PARENT_UUID, parentUUID.toString());
    }

    public UUID getParentUUID() {
        return UUID.fromString(this.dataManager.get(PARENT_UUID));
    }

    @Nullable
    public EntityPlayer getParent() {
        return world.getPlayerEntityByUUID(getParentUUID());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PARENT_UUID, "");
    }

    @Override
    public void onLivingUpdate() {
        EntityPlayer parent = getParent();

        if (parent == null || !parent.isEntityAlive()) {
            setDead();
            return;
        }

        this.setPositionAndRotation(
                parent.posX,
                parent.posY + 1.60f,
                parent.posZ,
                this.rotationYaw,
                parent.rotationPitch
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

    @Override
    public boolean isInRangeToRenderDist(double distance) {
//        EntityPlayer parent = getParent();
//
//        if (parent != null)
//            return parent.isInRangeToRenderDist(distance);

        return super.isInRangeToRenderDist(distance);
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

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false; // Disable saving of mini ghosts
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) { }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) { }

}
