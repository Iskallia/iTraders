package iskallia.itraders.block.multiblock.reactor;

import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import iskallia.itraders.Traders;
import iskallia.itraders.init.InitBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

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

}
