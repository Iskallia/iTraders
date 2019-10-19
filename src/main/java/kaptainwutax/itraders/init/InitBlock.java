
package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.block.BlockCryoChamber;
import kaptainwutax.itraders.block.BlockGraveStone;
import kaptainwutax.itraders.block.BlockInfusionCauldron;
import kaptainwutax.itraders.block.entity.TileEntityCryoChamber;
import kaptainwutax.itraders.block.entity.TileEntityGraveStone;
import kaptainwutax.itraders.block.entity.TileEntityInfusionCauldron;
import kaptainwutax.itraders.block.render.TESRGraveStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

	public static BlockGraveStone GRAVE_STONE = new BlockGraveStone("grave_stone", Material.ROCK);
	public static ItemBlock ITEM_GRAVE_STONE = getItemBlock(GRAVE_STONE);

	public static final BlockInfusionCauldron INFUSION_CAULDRON = new BlockInfusionCauldron("infusion_cauldron");
	public static final ItemBlock ITEM_INFUSION_CAULDRON = getItemBlock(INFUSION_CAULDRON);
	
	public static final BlockCryoChamber CRYO_CHAMBER = new BlockCryoChamber("cryo_chamber", Material.IRON);

	public static void registerBlocks(IForgeRegistry<Block> registry) {
		registerBlock(GRAVE_STONE, registry);
		registerBlock(INFUSION_CAULDRON, registry);
		registerBlock(CRYO_CHAMBER, registry);
	}

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityInfusionCauldron.class, Traders.getResource("infusion_cauldron"));
        GameRegistry.registerTileEntity(TileEntityGraveStone.class, Traders.getResource("grave_stone"));
        GameRegistry.registerTileEntity(TileEntityCryoChamber.class, Traders.getResource("cryo_chamber"));
    }
    
    public static void registerTileEntityRenderers() {
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGraveStone.class, new TESRGraveStone());
    }

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(ITEM_INFUSION_CAULDRON);
		registry.register(ITEM_GRAVE_STONE);
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
