package iskallia.itraders.subgame.lane;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import iskallia.itraders.block.entity.TileEntitySubGameController;
import iskallia.itraders.client.entity.FakeRenderLaneEntity;
import iskallia.itraders.subgame.GameCardEntity;
import iskallia.itraders.subgame.GameController;
import iskallia.itraders.subgame.lane.task.IdleEntityTask;
import iskallia.itraders.subgame.lane.task.LaneEntityTask;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class LaneEntity {

    private final GameLane lane;
    private final GameCardEntity entity;
    private LaneEntityTask runningTask;
    private Vector3 target;

    private int currentHealth;

    private Vector3 position;
    private Vector3 previousPosition;

    public LaneEntity(GameLane lane, GameCardEntity entity, Vector3 position, Vector3 target) {
        this.lane = lane;
        this.entity = entity;
        this.position = position;
        this.target = target;
        this.previousPosition = this.position.clone();

        this.currentHealth = entity.getCardData().getHealth();
    }

    public GameCardEntity getTemplateEntity() {
        return entity;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setPreviousPosition(Vector3 prevPosition) {
        this.previousPosition = prevPosition;
    }

    public Vector3 getPosition() {
        return position.clone();
    }

    public Vector3 getPreviousPosition() {
        return previousPosition.clone();
    }

    public Vector3 getTarget() {
        return target.clone();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void damage(int dmg) {
        this.currentHealth = Math.max(this.currentHealth - dmg, 0);
    }

    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    public void clearRunningTask() {
        this.runningTask = new IdleEntityTask(this);
    }

    public void setRunningTask(@Nonnull Function<LaneEntity, LaneEntityTask> taskFn) {
        this.runningTask = taskFn.apply(this);
        this.runningTask.startTask(this.lane);
    }

    @Nonnull
    public LaneEntityTask getRunningTask() {
        return runningTask;
    }

    @SideOnly(Side.CLIENT)
    public void drawEntity(TileEntitySubGameController ctrl, GameController gameController, double x, double y, double z, float pTicks) {
        EntityLivingBase renderEntity = new FakeRenderLaneEntity(Minecraft.getMinecraft().world);

        renderEntity.posX = this.position.getX();
        renderEntity.posY = this.position.getY();
        renderEntity.posZ = this.position.getZ();
        renderEntity.prevPosX = this.previousPosition.getX();
        renderEntity.prevPosY = this.previousPosition.getY();
        renderEntity.prevPosZ = this.previousPosition.getZ();
        renderEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getTemplateEntity().getCardData().getDamage().getAttackType().getAttackStack());

        getRunningTask().adjustEntityForTESR(renderEntity);

        //TODO modelplayer render for renderEntity,
        //TODO add nameplate with L1: name, L2: health
    }
}
