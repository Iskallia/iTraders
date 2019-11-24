
package iskallia.itraders.init;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockCryoChamber;
import iskallia.itraders.block.BlockCubeChamber;
import iskallia.itraders.block.BlockGhostPedestal;
import iskallia.itraders.block.BlockGraveStone;
import iskallia.itraders.block.BlockInfusionCauldron;
import iskallia.itraders.block.BlockMagicOre;
import iskallia.itraders.block.BlockPowerChamber;
import iskallia.itraders.block.entity.TileEntityCryoChamber;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.block.entity.TileEntityGraveStone;
import iskallia.itraders.block.entity.TileEntityInfusionCauldron;
import iskallia.itraders.block.entity.TileEntityPowerChamber;
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
	public static final BlockPowerChamber POWER_CHAMBER = new BlockPowerChamber("power_chamber", Material.IRON);
	
	public static final BlockGhostPedestal GHOST_PEDESTAL = new BlockGhostPedestal("ghost_pedestal");
	public static final ItemBlock ITEM_GHOST_PEDESTAL = getItemBlock(GHOST_PEDESTAL);
	
	public static final BlockMagicOre MAGIC_ORE_BLOCK = new BlockMagicOre("magic_ore_block");
	public static final ItemBlock ITEM_MAGIC_ORE_BLOCK = getItemBlock(MAGIC_ORE_BLOCK);
	
	public static final BlockCubeChamber CUBE_CHAMBER = new BlockCubeChamber("cube_chamber");
	public static final ItemBlock ITEM_CUBE_CHAMBER = getItemBlock(CUBE_CHAMBER);
	
	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registerBlock(GRAVE_STONE, registry);
		registerBlock(INFUSION_CAULDRON, registry);
		registerBlock(CRYO_CHAMBER, registry);
		registerBlock(POWER_CHAMBER, registry);
		registerBlock(GHOST_PEDESTAL, registry);
		registerBlock(MAGIC_ORE_BLOCK, registry);
		registerBlock(CUBE_CHAMBER, registry);
	}
	
	public static void registerTileEntities() { // TODO: Eliminate redefinition of Resource Names
		GameRegistry.registerTileEntity(TileEntityInfusionCauldron.class, Traders.getResource("infusion_cauldron"));
		GameRegistry.registerTileEntity(TileEntityGraveStone.class, Traders.getResource("grave_stone"));
		GameRegistry.registerTileEntity(TileEntityCryoChamber.class, Traders.getResource("cryo_chamber"));
		GameRegistry.registerTileEntity(TileEntityPowerChamber.class, Traders.getResource("power_chamber"));
		GameRegistry.registerTileEntity(TileEntityGhostPedestal.class, Traders.getResource("ghost_pedestal"));
		GameRegistry.registerTileEntity(TileEntityCubeChamber.class, Traders.getResource("cube_chamber"));
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(ITEM_INFUSION_CAULDRON);
		registry.register(ITEM_GRAVE_STONE);
		registry.register(ITEM_GHOST_PEDESTAL);
		registry.register(ITEM_MAGIC_ORE_BLOCK);
		registry.register(ITEM_CUBE_CHAMBER);
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
