package iskallia.itraders.multiblock.reactor;

import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorHeatSink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockReactorHeatSink extends BlockReactorSlave {

    public BlockReactorHeatSink(String name) {
        super(name);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityReactorHeatSink();
    }

}
