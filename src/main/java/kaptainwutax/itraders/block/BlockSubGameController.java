package kaptainwutax.itraders.block;

import javax.annotation.Nullable;

import kaptainwutax.itraders.block.entity.TileEntitySubGameController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Class: BlockGameController
 * Created by HellFirePvP
 * Date: 16.10.2019 / 19:54
 */
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
