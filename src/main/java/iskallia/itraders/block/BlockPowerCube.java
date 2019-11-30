package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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

    public static ItemStack generateRandomly(ItemStack eggStack) {
        if (eggStack.getItem() != InitItem.SPAWN_EGG_FIGHTER)
            return ItemStack.EMPTY;

        ItemStack cubeStack = new ItemStack(InitBlock.ITEM_POWER_CUBE, 1, 0);

        NBTTagCompound cubeNBT = new NBTTagCompound();
        cubeNBT.setString("Nickname", eggStack.getDisplayName());
        cubeNBT.setInteger("Rarity", CubeRarity.randomRarity().ordinal());
        // TODO: RNG-stuff

        cubeStack.setTagCompound(cubeNBT);

        return cubeStack;
    }

    /* ------------------------------- */

    public BlockPowerCube(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setHardness(2f);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null) return;

        CubeRarity rarity = CubeRarity.values()[stackNBT.getInteger("Rarity")];

        TextFormatting rarityColor = rarity.color;
        tooltip.add(rarityColor + "Rarity: "
                + TextFormatting.GRAY + rarity.translation());
        tooltip.add(rarityColor + "Nickname: "
                + TextFormatting.GRAY + stackNBT.getString("Nickname"));

    }

    /* ------------------------------- */

    public enum CubeRarity {

        COMMON(0.7, "common", TextFormatting.AQUA),
        RARE(0.15, "rare", TextFormatting.GOLD),
        EPIC(0.10, "epic", TextFormatting.LIGHT_PURPLE),
        MEGA(0.5, "mega", TextFormatting.GREEN);

        public static CubeRarity randomRarity() {
            double random = Math.random();
            for (CubeRarity rarity : values()) {
                if (random <= rarity.chance) {
                    return rarity;
                }

                random -= rarity.chance;
            }

            return COMMON; // Shouldn't even reach here
        }

        private double chance;
        private TextFormatting color;
        private String i18nKey;

        CubeRarity(double chance, String i18nKey, TextFormatting color) {
            this.chance = chance;
            this.i18nKey = i18nKey;
            this.color = color;
        }

        public String translation() {
            return I18n.format("itraders.cube.rarity." + i18nKey + ".name");
        }

    }

}
