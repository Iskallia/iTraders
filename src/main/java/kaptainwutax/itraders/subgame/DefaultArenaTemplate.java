package kaptainwutax.itraders.subgame;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;

/**
 * Class: DefaultArenaTemplate
 * Created by HellFirePvP
 * Date: 19.10.2019 / 12:58
 */
public class DefaultArenaTemplate extends ArenaTemplate {

    public static DefaultArenaTemplate INSTANCE = new DefaultArenaTemplate();

    private DefaultArenaTemplate() {
        addBlockCube(Blocks.DOUBLE_STONE_SLAB.getDefaultState()
                .withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE)
                .withProperty(BlockStoneSlab.SEAMLESS, true),
                -3, 1, -2,  3, 1, 2);
        addBlockCube(Blocks.IRON_BLOCK.getDefaultState(),
                -4, 1, -2, -4, 1, 2);
        addBlockCube(Blocks.IRON_BLOCK.getDefaultState(),
                4, 1, -2,  4, 1, 2);
        addBlockCube(Blocks.GOLD_BLOCK.getDefaultState(),
                -5, 1, -2, -5, 1, 2);
        addBlockCube(Blocks.GOLD_BLOCK.getDefaultState(),
                5, 1, -2,  5, 1, 2);
    }

}
