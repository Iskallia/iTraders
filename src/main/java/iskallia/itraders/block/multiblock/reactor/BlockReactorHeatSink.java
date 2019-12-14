package iskallia.itraders.block.multiblock.reactor;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockReactorHeatSink extends Block {

    public BlockReactorHeatSink(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

}
