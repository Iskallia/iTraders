package iskallia.itraders.item;

import javax.annotation.Nullable;

import iskallia.itraders.Traders;
import iskallia.itraders.entity.EntityItemMagicOreDust;
import iskallia.itraders.init.InitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMagicOreDust extends Item {

    public ItemMagicOreDust(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity e, ItemStack itemstack) {
        EntityItemMagicOreDust dustEntity = new EntityItemMagicOreDust(world, e.posX, e.posY, e.posZ, itemstack);
        dustEntity.motionX = e.motionX;
        dustEntity.motionY = e.motionY;
        dustEntity.motionZ = e.motionZ;
        dustEntity.setPickupDelay(20);
        if (e instanceof EntityItem) {
            dustEntity.setThrower(((EntityItem) e).getThrower());
            dustEntity.setOwner(((EntityItem) e).getOwner());
        }
        return dustEntity;
    }
}
