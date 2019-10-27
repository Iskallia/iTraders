package iskallia.itraders.subgame.lane;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import iskallia.itraders.subgame.GameCardEntity;
import iskallia.itraders.subgame.GameController;
import iskallia.itraders.subgame.lane.task.LaneEntityTask;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class GameLane {

    private final BlockPos posFrom;
    private final BlockPos posTo;
    private final BlockPos interactionPosFrom;
    private final BlockPos interactionPosTo;
    private LaneEntity fromEntity;
    private LaneEntity toEntity;

    public GameLane(BlockPos posFrom, BlockPos posTo, EnumFacing direction) {
        this.posFrom = posFrom;
        this.interactionPosFrom = posFrom.add(direction.getDirectionVec()).up();
        this.posTo = posTo;
        this.interactionPosTo = posFrom.subtract(direction.getDirectionVec()).up();
    }

    public BlockPos getAbsPosFrom() {
        return posFrom;
    }

    public BlockPos getInteractionPosFrom() {
        return interactionPosFrom;
    }

    public BlockPos getAbsPosTo() {
        return posTo;
    }

    public BlockPos getInteractionPosTo() {
        return interactionPosTo;
    }

    @Nullable
    public LaneEntity getEntityPlayer1() {
        return fromEntity;
    }

    public boolean spawnLaneEntityPlayer1(GameController ctrl, GameCardEntity cardEntity) {
        if (getEntityPlayer1() != null) {
            return false;
        }
        this.fromEntity = new LaneEntity(this, cardEntity,
                new Vector3(posFrom.subtract(ctrl.getOffset())).add(0.5, 0, 0.5),
                new Vector3(posTo.subtract(ctrl.getOffset())).add(0.5, 0, 0.5));
        return true;
    }

    @Nullable
    public LaneEntity getEntityPlayer2() {
        return toEntity;
    }

    public boolean spawnLaneEntityPlayer2(GameController ctrl, GameCardEntity cardEntity) {
        if (getEntityPlayer2() != null) {
            return false;
        }
        this.toEntity = new LaneEntity(this, cardEntity,
                new Vector3(posTo.subtract(ctrl.getOffset())).add(0.5, 0, 0.5),
                new Vector3(posFrom.subtract(ctrl.getOffset())).add(0.5, 0, 0.5));
        return true;
    }

    @Nullable
    public LaneEntity getOpposingEntity(LaneEntity thisEntity) {
        if (getEntityPlayer1() != null && !getEntityPlayer1().equals(thisEntity)) {
            return getEntityPlayer1();
        }
        if (getEntityPlayer2() != null && !getEntityPlayer2().equals(thisEntity)) {
            return getEntityPlayer2();
        }
        return null;
    }

    public void tick() {
        if (this.getEntityPlayer1() != null) {
            if (this.getEntityPlayer1().isDead()) {
                //TODO death effects or smth
                this.fromEntity = null;
            } else {
                LaneEntityTask task = this.getEntityPlayer1().getRunningTask();
                task.tick(this);
                if (task.isFinished()) {
                    task.stopTask(this);
                    this.getEntityPlayer1().clearRunningTask();
                }
            }
        }
        if (this.getEntityPlayer2() != null) {
            if (this.getEntityPlayer2().isDead()) {
                //TODO death effects or smth
                this.toEntity = null;
            } else {
                LaneEntityTask task = this.getEntityPlayer2().getRunningTask();
                task.tick(this);
                if (task.isFinished()) {
                    task.stopTask(this);
                    this.getEntityPlayer2().clearRunningTask();
                }
            }
        }
    }

    public boolean areTasksFinished() {
        if (this.getEntityPlayer1() != null) {
            if (!this.getEntityPlayer1().getRunningTask().isFinished()) {
                return false;
            }
        }
        if (this.getEntityPlayer2() != null) {
            if (!this.getEntityPlayer2().getRunningTask().isFinished()) {
                return false;
            }
        }
        return true;
    }
}
