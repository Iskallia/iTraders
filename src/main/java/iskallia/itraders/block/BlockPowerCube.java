package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemBooster;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

/*
 * NBT Structure: {
 *     Nickname: "iGoodie", // Nickname of the subscriber
 *     Months: 3, // Months of subscription
 *
 *     Rarity: 1, // Enum ordinal of the Rarity
 *     Decay: { RemainingTicks: 100, MaxTicks: 300 } // 100/300 of use time remaining
 * }
 */
public class BlockPowerCube extends Block {

    public static ItemStack generateRandomly(ItemStack eggStack, ItemStack boosterStack) {
        if(eggStack.getItem() != InitItem.SPAWN_EGG_FIGHTER)
            return ItemStack.EMPTY;

        // TODO: Generate a random Power Cube
        // Remember! This routine requires random Rarity & Decay stuff generation! ^^

        return ItemStack.EMPTY;
    }

    /* ------------------------------- */

    public BlockPowerCube(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setHardness(2f);
    }



}
