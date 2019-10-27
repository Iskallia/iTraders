package iskallia.itraders.client.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class FakeRenderLaneEntity extends EntityLivingBase {

    private final NonNullList<ItemStack> inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);

    public FakeRenderLaneEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventoryArmor;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        switch (slotIn.getSlotType()) {
            case HAND:
                return this.inventoryHands.get(slotIn.getIndex());
            case ARMOR:
                return this.inventoryArmor.get(slotIn.getIndex());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        switch (slotIn.getSlotType()) {
            case HAND:
                this.inventoryHands.set(slotIn.getIndex(), stack);
                break;
            case ARMOR:
                this.inventoryArmor.set(slotIn.getIndex(), stack);
        }
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }
}
