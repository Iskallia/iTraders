package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.block.BlockGraveStone;
import kaptainwutax.itraders.tile.TileInfusionCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import kaptainwutax.itraders.block.BlockInfusionCauldron;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

    public static BlockGraveStone GRAVE_STONE = new BlockGraveStone("grave_stone", Material.ROCK);
    public static final BlockInfusionCauldron INFUSION_CAULDRON = new BlockInfusionCauldron("infusion_cauldron");
    public static final ItemBlock INFUSION_CAULDRON_ITEM = getItemBlock(INFUSION_CAULDRON);

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registerBlock(GRAVE_STONE, registry);
        registerBlock(INFUSION_CAULDRON, registry);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileInfusionCauldron.class, Traders.getResource("infusion_cauldron"));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(INFUSION_CAULDRON_ITEM);
    }

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
