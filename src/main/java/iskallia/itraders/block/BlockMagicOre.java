package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMagicOre extends Block {

    public BlockMagicOre(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setHardness(2f);
        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

}
