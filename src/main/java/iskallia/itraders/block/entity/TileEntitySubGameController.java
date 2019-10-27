package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import iskallia.itraders.subgame.GameController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntitySubGameController extends TileEntitySynchronized implements ITickable {

    private GameController controller;

    @Override
    public void update() {

    }

    public GameController getController() {
        return controller;
    }
}
