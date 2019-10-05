package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.storage.EggPouchWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemEggPouch extends Item {

    public ItemEggPouch(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack pouchItemStack = playerIn.getHeldItem(handIn);

        if (worldIn.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, pouchItemStack);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) { // TODO: Remove. Here for debug purposes
            Item spawn_egg_fighter = Item.REGISTRY.getObject(new ResourceLocation(Traders.MOD_ID, "spawn_egg_fighter"));
            EggPouchWorldSavedData.get(worldIn).putSpawnEgg("iGoodie", new ItemStack(spawn_egg_fighter, 1));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        List<ItemStack> eggsOfiGoodie = EggPouchWorldSavedData.get(worldIn).getPouchContent("iGoodie");
        System.out.printf("Eggs of iGoodie: %s\n", eggsOfiGoodie);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}