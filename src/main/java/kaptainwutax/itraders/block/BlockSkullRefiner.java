package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSkullNeck;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSkullRefiner extends Block {

    public BlockSkullRefiner(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = playerIn.getHeldItem(hand);

        if (facing != EnumFacing.UP)
            return false;

        System.out.printf("ACTIVATION with %s @ %s\n",
                heldStack.getItem().getClass().getSimpleName(),
                facing);

        worldIn.destroyBlock(pos.west(), false);
        worldIn.destroyBlock(pos.north(), false);
        worldIn.destroyBlock(pos.east(), false);
        worldIn.destroyBlock(pos.south(), false);
        worldIn.destroyBlock(pos.up(), false);

        ItemStack necklaceStack = ItemSkullNeck.generateRandom("iGoodie"); // TODO fetch name from skull
        playerIn.dropItem(necklaceStack, false, false);

        return false;
    }

}
