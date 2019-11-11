
package iskallia.itraders.init;

import iskallia.itraders.Traders;
import iskallia.itraders.block.*;
import iskallia.itraders.block.entity.*;
import iskallia.itraders.block.BlockGraveStone;
import iskallia.itraders.block.BlockInfusionCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

	public static BlockGraveStone GRAVE_STONE = new BlockGraveStone("grave_stone", Material.ROCK);
	public static ItemBlock ITEM_GRAVE_STONE = getItemBlock(GRAVE_STONE);

	public static final BlockInfusionCauldron INFUSION_CAULDRON = new BlockInfusionCauldron("infusion_cauldron");
	public static final ItemBlock ITEM_INFUSION_CAULDRON = getItemBlock(INFUSION_CAULDRON);
	
	public static final BlockCryoChamber CRYO_CHAMBER = new BlockCryoChamber("cryo_chamber", Material.IRON);

	public static final BlockGhostPedestal GHOST_PEDESTAL = new BlockGhostPedestal("ghost_pedestal");
	public static final ItemBlock ITEM_GHOST_PEDESTAL = getItemBlock(GHOST_PEDESTAL);

	public static final BlockMagicOre MAGIC_ORE_BLOCK = new BlockMagicOre("magic_ore_block");
	public static final ItemBlock ITEM_MAGIC_ORE_BLOCK = getItemBlock(MAGIC_ORE_BLOCK);

	public static final BlockTraderStatue TRADER_STATUE = new BlockTraderStatue("trader_statue");
	public static final ItemBlock ITEM_TRADER_STATUE = getItemBlock(TRADER_STATUE);

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registerBlock(GRAVE_STONE, registry);
		registerBlock(INFUSION_CAULDRON, registry);
		registerBlock(CRYO_CHAMBER, registry);
		registerBlock(GHOST_PEDESTAL, registry);
		registerBlock(MAGIC_ORE_BLOCK, registry);
		registerBlock(TRADER_STATUE, registry);
	}

    public static void registerTileEntities() { // TODO: Eliminate redefinition of Resource Names
        GameRegistry.registerTileEntity(TileEntityInfusionCauldron.class, Traders.getResource("infusion_cauldron"));
        GameRegistry.registerTileEntity(TileEntityGraveStone.class, Traders.getResource("grave_stone"));
        GameRegistry.registerTileEntity(TileEntityCryoChamber.class, Traders.getResource("cryo_chamber"));
 		GameRegistry.registerTileEntity(TileEntityGhostPedestal.class, Traders.getResource("ghost_pedestal"));
    	GameRegistry.registerTileEntity(TileEntityTraderStatue.class, Traders.getResource("trader_statue"));
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(ITEM_INFUSION_CAULDRON);
		registry.register(ITEM_GRAVE_STONE);
		registry.register(ITEM_GHOST_PEDESTAL);
		registry.register(ITEM_MAGIC_ORE_BLOCK);
		registry.register(ITEM_TRADER_STATUE);
	}

	/* -------------------------- */

	private static void registerBlock(Block block, IForgeRegistry<Block> registry) {
		registry.register(block);
	}

	private static ItemBlock getItemBlock(Block block) {
		ItemBlock itemBlock = new ItemBlock(block);
		String resourceName = block.getRegistryName().getResourcePath();
		itemBlock.setUnlocalizedName(resourceName);
		itemBlock.setRegistryName(resourceName);
		return itemBlock;
	}

}
