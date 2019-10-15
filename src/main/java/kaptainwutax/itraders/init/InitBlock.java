package kaptainwutax.itraders.init;

import kaptainwutax.itraders.block.BlockSkullRefiner;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

    public static final BlockSkullRefiner SKULL_REFINER = new BlockSkullRefiner("skull_refiner");
    public static final ItemBlock SKULL_REFINER_ITEM_BLOCK = getItemBlock(SKULL_REFINER);

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(SKULL_REFINER);
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(SKULL_REFINER_ITEM_BLOCK);
    }

    private static ItemBlock getItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        String resourceName = block.getRegistryName().getResourcePath();
        itemBlock.setUnlocalizedName(resourceName);
        itemBlock.setRegistryName(resourceName);
        return itemBlock;
    }

}
