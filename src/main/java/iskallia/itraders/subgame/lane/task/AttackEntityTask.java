package iskallia.itraders.subgame.lane.task;

import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.LaneEntity;
import net.minecraft.entity.EntityLivingBase;

public abstract class AttackEntityTask extends LaneEntityTask {

    private int animationTick;
    private boolean attacked = false;

    public AttackEntityTask(LaneEntity laneEntity) {
        super(laneEntity);
        this.animationTick = getAttackAnimationTime();
    }

    public float getAttackRange() {
        return this.getLaneEntity().getTemplateEntity().getCardData().getDamage().getAttackType().getAttackRange();
    }

    public abstract int getAttackAnimationTime();

    @Override
    public void startTask(GameLane lane) {}

    @Override
    public void tick(GameLane lane) {
        if (!attacked) {
            attacked = true; //If someone sets this task when it's not applicable, their own implementatory fault.
            LaneEntity opposing = lane.getOpposingEntity(this.getLaneEntity());
            if (opposing != null && opposing.getPosition().distance(this.getLaneEntity().getPosition()) <= getAttackRange()) {
                opposing.damage(this.getLaneEntity().getTemplateEntity().getCardData().getDamage().getDamageRoll());
            }
        } else {
            this.animationTick = Math.max(this.animationTick - 1, 0);
        }
    }

    @Override
    public void stopTask(GameLane lane) {}

    @Override
    public boolean isFinished() {
        return attacked && animationTick <= 0;
    }

    @Override
    public void adjustEntityForTESR(EntityLivingBase renderEntity) {

    }
}
