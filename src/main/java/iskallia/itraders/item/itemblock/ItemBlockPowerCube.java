package iskallia.itraders.item.itemblock;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.entity.EntityItemPowerCube;
import iskallia.itraders.init.InitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBlockPowerCube extends ItemBlock {

    public ItemBlockPowerCube(String name, BlockPowerCube blockPowerCube) {
        super(blockPowerCube);

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
    public Entity createEntity(World world, Entity entity, ItemStack itemstack) {
        EntityItemPowerCube entityItemPowerCube = new EntityItemPowerCube(world, entity.posX, entity.posY, entity.posZ, itemstack);

        entityItemPowerCube.motionX = entity.motionX;
        entityItemPowerCube.motionY = entity.motionY;
        entityItemPowerCube.motionZ = entity.motionZ;

        entityItemPowerCube.setPickupDelay(20);

        if (entity instanceof EntityItem) {
            entityItemPowerCube.setThrower(((EntityItem) entity).getThrower());
            entityItemPowerCube.setOwner(((EntityItem) entity).getOwner());
        }

        return entityItemPowerCube;
    }

}
