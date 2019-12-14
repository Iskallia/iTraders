package iskallia.itraders.multiblock.reactor;

import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityPowerCube;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorSlave;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiblockReactor extends PatternBlockArray {

    public static final MultiblockReactor INSTANCE = new MultiblockReactor();

    private MultiblockReactor() {
        super(new ResourceLocation(Traders.MOD_ID, "pattern_reactor"));
        load();
    }

    private void load() {
        int y, z; // <-- Hey do not judge my laziness! :p

        IBlockState reactorBlock = InitBlock.REACTOR_BLOCK.getDefaultState();
        IBlockState reactorGlass = InitBlock.REACTOR_GLASS.getDefaultState();
        IBlockState reactorHeatSink = InitBlock.REACTOR_HEAT_SINK.getDefaultState();
        IBlockState reactorCore = InitBlock.REACTOR_CORE.getDefaultState();
        IBlockState reactorCPU = InitBlock.REACTOR_CPU.getDefaultState();
        IBlockState[] blockPalette = {
                reactorBlock,
                reactorGlass,
                reactorHeatSink,
                reactorCore,
                reactorCPU
        };

        {
            y = 2;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = 1;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[2]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = 0;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[3]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = -1;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[4]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = -2;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }
    }

    private void addBlockFromPalette(int x, int y, int z, IBlockState blockFromPalette) {
        if (blockFromPalette == null) return;
        super.addBlock(x, y, z, blockFromPalette);
    }

    public boolean structureReactor(World world, BlockPos masterPos) {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos pos = masterPos.add(entry.getKey());

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityReactorSlave) {
                ((TileEntityReactorSlave) tileEntity).connectToMaster(masterPos);

            } else if (!(tileEntity instanceof TileEntityReactorCore)) {
                return false;
            }
        }

        return true;
    }

    public void destructReactor(World world, BlockPos masterPos) {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos pos = masterPos.add(entry.getKey());

            TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

            if (reactorSlave == null)
                continue;

            reactorSlave.disconnectFromMaster();
        }

        TileEntityReactorCore reactorCore = TileEntityReactorCore.getReactorCore(world, masterPos);

        if (reactorCore != null) {
            reactorCore.onReactorDestructed();
        }
    }

    public List<TileEntityPowerCube> getPowerCubes(World world, BlockPos masterPos) {
        List<TileEntityPowerCube> cubes = new LinkedList<>();

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos pos = masterPos.add(xOffset, yOffset, zOffset);
                    TileEntity tileEntity = world.getTileEntity(pos);

                    if (tileEntity instanceof TileEntityPowerCube)
                        cubes.add((TileEntityPowerCube) tileEntity);
                }
            }
        }

        return cubes;
    }

}
