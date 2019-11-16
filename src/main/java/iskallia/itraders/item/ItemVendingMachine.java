package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockVendingMachine;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.xml.soap.Text;
import java.util.List;

public class ItemVendingMachine extends Item {

    public ItemVendingMachine(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;

        IBlockState blockstate = world.getBlockState(pos);
        Block block = blockstate.getBlock();

        if (!block.isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack heldStack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, heldStack) && InitBlock.VENDING_MACHINE.canPlaceBlockAt(world, pos)) {
            EnumFacing enumFacing = EnumFacing.fromAngle(player.rotationYaw);
            BlockVendingMachine.placeVendingMachine(world, pos, enumFacing, InitBlock.VENDING_MACHINE, heldStack.getTagCompound());

            SoundType soundType = block.getSoundType(blockstate, world, pos, player);
            world.playSound(player, pos, soundType.getPlaceSound(),
                    SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

            heldStack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null) {
            tooltip.add("No trader is in yet.");
            tooltip.add("");
            tooltip.add("(" + TextFormatting.DARK_PURPLE + "Right click"
                    + TextFormatting.GRAY + " with a boxed");
            tooltip.add("Trader to put them in.)");

        } else {
            String nickname = stackNBT.getString("Nickname");
            NBTTagList recipeList = stackNBT.getCompoundTag("Offers")
                    .getTagList("Recipes", Constants.NBT.TAG_COMPOUND);

            tooltip.add(TextFormatting.AQUA + nickname
                    + TextFormatting.GRAY + " is in");
            tooltip.add("with " + recipeList.tagCount() + " trades.");
        }

        super.addInformation(stack, world, tooltip, flagIn);
    }

}
