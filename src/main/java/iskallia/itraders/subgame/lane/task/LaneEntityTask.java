package iskallia.itraders.subgame.lane.task;

import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.LaneEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class LaneEntityTask {

    protected final LaneEntity laneEntity;

    protected LaneEntityTask(LaneEntity laneEntity) {
        this.laneEntity = laneEntity;
    }

    @Nonnull
    public LaneEntity getLaneEntity() {
        return laneEntity;
    }

    public abstract void startTask(GameLane lane);

    public abstract void tick(GameLane lane);

    public abstract void stopTask(GameLane lane);

    public abstract boolean isFinished();

    @SideOnly(Side.CLIENT)
    public abstract void adjustEntityForTESR(EntityLivingBase renderEntity);

}
