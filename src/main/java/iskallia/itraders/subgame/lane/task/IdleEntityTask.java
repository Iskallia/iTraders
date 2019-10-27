package iskallia.itraders.subgame.lane.task;

import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.LaneEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IdleEntityTask extends LaneEntityTask {

    public IdleEntityTask(LaneEntity laneEntity) {
        super(laneEntity);
    }

    @Override
    public void startTask(GameLane lane) {}

    @Override
    public void tick(GameLane lane) {}

    @Override
    public void stopTask(GameLane lane) {}

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void adjustEntityForTESR(EntityLivingBase renderEntity) {

    }
}
