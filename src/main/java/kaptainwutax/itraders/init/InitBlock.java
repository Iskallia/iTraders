package kaptainwutax.itraders.init;

import kaptainwutax.itraders.block.BlockGraveStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

	public static BlockGraveStone GRAVE_STONE = new BlockGraveStone("grave_stone", Material.ROCK);
	
	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registerBlock(GRAVE_STONE, registry);
	}
	
	private static void registerBlock(Block block, IForgeRegistry<Block> registry) {
		registry.register(block);
	}

}