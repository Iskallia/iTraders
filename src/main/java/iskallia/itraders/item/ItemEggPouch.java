package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.gui.GuiHandler;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.world.data.DataEggPouch;
import iskallia.itraders.world.storage.PouchInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.xml.crypto.Data;

public class ItemEggPouch extends Item {

    public ItemEggPouch(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!player.isSneaking())
            player.openGui(Traders.getInstance(), GuiHandler.POUCH, world, 0, 0, 0);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                IItemHandler capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (capability != null)
                    return this.fillInventory(player, tileEntity, capability);

            } else {
                return this.spawnRandomFighter(player, world, pos, hand, facing, hitX, hitY, hitZ);
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

//    @SideOnly(Side.SERVER)
    private EnumActionResult fillInventory(EntityPlayer player, TileEntity tileEntity, IItemHandler itemHandler) {
        EnumActionResult result = EnumActionResult.PASS;
        WorldServer world = (WorldServer) tileEntity.getWorld();
        PouchInventory pouch = DataEggPouch.get(world).getOrCreatePouch(player);

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i) != ItemStack.EMPTY)
                continue;

            ItemStack itemStack = pouch.extractItem(0, 1, false);

            if (itemStack == ItemStack.EMPTY)
                break;

            pouch.move(1);

            if (itemHandler.insertItem(i, itemStack, false) == ItemStack.EMPTY)
                result = EnumActionResult.SUCCESS;
        }

        if (result == EnumActionResult.SUCCESS) {
            BlockPos pos = tileEntity.getPos();
            int particleCount = 100;
            world.spawnParticle(EnumParticleTypes.SPELL_WITCH, false,
                    pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d,
                    particleCount, 0, 0, 0, Math.PI);
            world.playSound(null, player.getPosition(),
                    SoundEvents.BLOCK_NOTE_BELL,
                    SoundCategory.PLAYERS, 1.0f, 0.2f * (Item.itemRand.nextFloat() - Item.itemRand.nextFloat()) + 0.6f);
        }

        return result;
    }

    private EnumActionResult spawnRandomFighter(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = DataEggPouch.get(world).getOrCreatePouch(player).randomFighterEgg();

        if (itemStack != null && (itemStack.getItem() instanceof ItemSpawnEggFighter)) {
            ItemSpawnEggFighter eggItem = (ItemSpawnEggFighter) itemStack.getItem();

            if (itemStack.hasDisplayName()) {
                StringBuilder sb = new StringBuilder();
                sb.append(TextFormatting.GREEN).append(itemStack.getDisplayName());

                int months = InitItem.SPAWN_EGG_FIGHTER.getMonths(itemStack);

                sb.append(TextFormatting.GRAY);
                if (months != -1)
                    sb.append("(").append(months).append(")");
                sb.append(", I choose you!");

                player.sendStatusMessage(new TextComponentString(sb.toString()), true);
            }

            world.playSound(null, player.getPosition(),
                    SoundEvents.BLOCK_NOTE_BELL,
                    SoundCategory.PLAYERS, 1.0f, 0.2f * (Item.itemRand.nextFloat() - Item.itemRand.nextFloat()) + 0.6f);

            return eggItem.onItemUse(itemStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
        }

        return EnumActionResult.PASS;
    }

}