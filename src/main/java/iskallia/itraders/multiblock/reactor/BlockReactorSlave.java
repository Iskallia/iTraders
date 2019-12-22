package iskallia.itraders.multiblock.reactor;

import iskallia.itraders.Traders;
import iskallia.itraders.gui.GuiHandler;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorSlave;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockReactorSlave extends Block {

    public BlockReactorSlave(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborPos) {
        super.onNeighborChange(world, pos, neighborPos);

        IBlockState blockStateChanged = world.getBlockState(neighborPos);
        Block changedBlock = blockStateChanged.getBlock();

        if (changedBlock == InitBlock.POWER_CUBE)
            onNeighborPowerCube(world, pos, neighborPos);
    }

    public void onNeighborPowerCube(IBlockAccess world, BlockPos pos, BlockPos cubePos) {
        TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

        if (reactorSlave != null && reactorSlave.hasMaster()) {
            TileEntityReactorCore master = reactorSlave.getMaster();
            if (master != null) {
                MultiblockReactor.INSTANCE.validateReactorInventory(world, master.getPos());
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

        if (reactorSlave != null) {
            TileEntityReactorCore master = reactorSlave.getMaster();
            if (master != null && master.isStructured()) {
                for (int i = 0; i < 24; i++) {
                    ItemStack stackInSlot = master.getInventoryHandler().getStackInSlot(i);
                    System.out.println(i + "# " + stackInSlot);
                }
                player.openGui(Traders.getInstance(), GuiHandler.REACTOR, world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }

        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!world.isRemote) {
            TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

            if (reactorSlave != null && reactorSlave.hasMaster()) {
                BlockPos masterPos = reactorSlave.getMasterPos();
                MultiblockReactor.INSTANCE.destructReactor(world, masterPos);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

}
