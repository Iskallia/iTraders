package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.entity.EntityTrader;
import iskallia.itraders.init.InitItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

/*
 * NBT: {
 *     Nickname: "iGoodie",
 *     Offers:{ ... }
 * }
 */
public class ItemCardboardBox extends Item {

    public ItemCardboardBox(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!carryingTrader(stack)) {
            tooltip.add("No trader boxed yet.");
            tooltip.add("");
            tooltip.add("(" + TextFormatting.DARK_PURPLE + "Shift + Right click"
                    + TextFormatting.GRAY + " on a");
            tooltip.add("Trader to box them.)");

        } else {
            NBTTagCompound stackNBT = stack.getTagCompound();
            String nickname = stackNBT.getString("Nickname");
            NBTTagList recipeList = stackNBT.getCompoundTag("Offers")
                    .getTagList("Recipes", Constants.NBT.TAG_COMPOUND);

            tooltip.add("Carrying " + TextFormatting.AQUA + nickname);
            tooltip.add("with " + recipeList.tagCount() + " trades.");
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static void boxTrader(EntityTrader trader, ItemStack cardboardStack) {
        if (cardboardStack.getItem() != InitItem.CARDBOARD_BOX)
            return; // Can box Traders only into a Cardboard Box

        String customNameTag = trader.getCustomNameTag();
        NBTBase offersNBT = trader.serializeNBT().getTag("Offers");

        NBTTagCompound stackNBT = cardboardStack.getTagCompound();
        if (stackNBT == null) stackNBT = new NBTTagCompound();

        stackNBT.setString("Nickname", customNameTag);
        stackNBT.setTag("Offers", offersNBT);
        cardboardStack.setTagCompound(stackNBT);
    }

    public static boolean carryingTrader(ItemStack cardboardStack) {
        if (cardboardStack.getItem() != InitItem.CARDBOARD_BOX)
            return false;

        NBTTagCompound stackNBT = cardboardStack.getTagCompound();

        if (stackNBT == null)
            return false;

        return stackNBT.hasKey("Nickname", Constants.NBT.TAG_STRING)
                && stackNBT.hasKey("Offers", Constants.NBT.TAG_COMPOUND);
    }

}
