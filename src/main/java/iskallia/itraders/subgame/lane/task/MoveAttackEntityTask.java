package iskallia.itraders.subgame.lane.task;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.LaneEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoveAttackEntityTask extends LaneEntityTask {

    private static final float MOVE_PER_TICK = 0.4F;

    private final Vector3 taskTarget;
    private float moveDistance;

    public MoveAttackEntityTask(LaneEntity laneEntity) {
        super(laneEntity);
        float cardSpeed = this.getLaneEntity().getTemplateEntity().getCardData().getTileSpeed();
        float targetDistance = (float) this.getTargetVec().length();

        float tilesToMove = cardSpeed > targetDistance ? cardSpeed : targetDistance;
        this.taskTarget = this.getLaneEntity().getPosition().add(
                this.laneEntity.getTarget().clone()
                        .subtract(this.laneEntity.getPosition().clone())
                        .normalize().multiply(tilesToMove));
        this.moveDistance = tilesToMove;
    }

    @Override
    public void startTask(GameLane lane) {}

    @Override
    public void tick(GameLane lane) {
        float moveDst = moveDistance > MOVE_PER_TICK ? MOVE_PER_TICK : moveDistance;
        LaneEntity other = lane.getOpposingEntity(this.getLaneEntity());
        if (other != null && other.getPosition().distance(this.getLaneEntity().getPosition()) <=
                this.getLaneEntity().getTemplateEntity().getCardData().getDamage().getAttackType().getAttackRange()) {

            this.getLaneEntity().setRunningTask(this.getLaneEntity().getTemplateEntity().getCardData().getDamage().getAttackType()::createAttackTask);
            return;
        }

        Vector3 move = this.getTargetVec().normalize().multiply(moveDst);

        this.laneEntity.setPreviousPosition(this.laneEntity.getPosition());
        this.laneEntity.setPosition(this.laneEntity.getPosition().add(move));
        this.moveDistance -= moveDst;
    }

    @Override
    public void stopTask(GameLane lane) {
        this.laneEntity.setPosition(this.taskTarget);
        this.laneEntity.setPreviousPosition(this.taskTarget);
        this.moveDistance = 0F;
    }

    private Vector3 getTargetVec() {
        return this.laneEntity.getTarget().clone().subtract(this.laneEntity.getPosition().clone());
    }

    @Override
    public boolean isFinished() {
        return this.moveDistance <= 0.001;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void adjustEntityForTESR(EntityLivingBase renderEntity) {

    }
}
