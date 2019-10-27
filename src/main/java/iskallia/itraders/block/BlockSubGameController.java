package iskallia.itraders.block;

import javax.annotation.Nullable;

import iskallia.itraders.block.entity.TileEntitySubGameController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSubGameController extends BlockContainer {

    public BlockSubGameController() {
        super(Material.IRON, MapColor.BLACK);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySubGameController();
    }
}
