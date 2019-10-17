package kaptainwutax.itraders.init;

import kaptainwutax.itraders.block.BlockInfusionCauldron;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlock {

    public static final BlockInfusionCauldron INFUSION_CAULDRON = new BlockInfusionCauldron("infusion_cauldron");
    public static final ItemBlock INFUSION_CAULDRON_ITEM = getItemBlock(INFUSION_CAULDRON);

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(INFUSION_CAULDRON);
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(INFUSION_CAULDRON_ITEM);
    }

    private static ItemBlock getItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        String resourceName = block.getRegistryName().getResourcePath();
        itemBlock.setUnlocalizedName(resourceName);
        itemBlock.setRegistryName(resourceName);
        return itemBlock;
    }

}
