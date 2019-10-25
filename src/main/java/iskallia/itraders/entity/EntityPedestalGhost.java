package iskallia.itraders.entity;

import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSkullNeck;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityPedestalGhost extends EntityLivingBase {

    public final SkinProfile skin = new SkinProfile();
    public String name;
    public EnumFacing facing;
    public BlockPos blockPos;
    public NonNullList<ItemStack> armors = NonNullList.withSize(4, ItemStack.EMPTY);

    public EntityPedestalGhost(World worldIn) {
        super(worldIn);
        setSize(0.25f, 0.25f);
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
    public void onLivingUpdate() {
        if (shouldDespawn()) {
            world.removeEntity(this);
        }
    }

    public boolean shouldDespawn() {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (!(tileEntity instanceof TileEntityGhostPedestal))
            return true;

        TileEntityGhostPedestal teGhostPedestal = (TileEntityGhostPedestal) tileEntity;
        ItemStack stack = teGhostPedestal.getNecklace();

        if (stack.getItem() != InitItem.SKULL_NECKLACE)
            return true;

        String headOwner = ItemSkullNeck.getHeadOwner(stack);

        if (headOwner == null)
            return true;

        if (!headOwner.equals(name))
            return true;

        return false;
    }

}
