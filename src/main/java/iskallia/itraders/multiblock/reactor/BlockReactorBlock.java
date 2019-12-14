package iskallia.itraders.multiblock.reactor;

import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockReactorBlock extends BlockReactorSlave {

    public BlockReactorBlock(String name) {
        super(name);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityReactorBlock();
    }

}
