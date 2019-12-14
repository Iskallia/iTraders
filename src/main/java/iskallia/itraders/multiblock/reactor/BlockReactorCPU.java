package iskallia.itraders.multiblock.reactor;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockReactorCPU extends Block {

    public BlockReactorCPU(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

}
