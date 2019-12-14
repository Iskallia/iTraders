package iskallia.itraders.multiblock.reactor;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorSlave;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReactorSlave extends Block {

    public BlockReactorSlave(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);

        // TODO: if neighbor is in Reactor:
        // TODO:    include in master TE
        System.out.println(neighbor + " is changed!");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

            if (reactorSlave != null && reactorSlave.hasMaster()) {
                // TODO: Open Reactor GUI
                System.out.println("Opened Reactor GUI! :p");
                return true;
            }
            System.out.println("Not structured yet!");
        }

        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
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
