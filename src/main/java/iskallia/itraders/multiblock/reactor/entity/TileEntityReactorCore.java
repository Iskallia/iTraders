package iskallia.itraders.multiblock.reactor.entity;

import iskallia.itraders.multiblock.reactor.MultiblockReactor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityReactorCore extends TileEntity implements ITickable {

    @Override
    public void update() {
        if(MultiblockReactor.INSTANCE.matches(this.world, this.pos)) {
            System.out.println("MULTIBLOCK!");
        } else {
            System.out.println("Nay...");
        }
    }

}
