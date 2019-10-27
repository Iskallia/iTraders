package iskallia.itraders.card.damage;

import iskallia.itraders.subgame.lane.LaneEntity;
import iskallia.itraders.subgame.lane.task.LaneEntityTask;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public enum SubCardAttackType {

    MELEE(0.5F, () -> new ItemStack(Items.IRON_SWORD));

    private final float attackRange;
    private final Supplier<ItemStack> attackStackProvider;

    SubCardAttackType(float attackRange, Supplier<ItemStack> attackStackProvider) {
        this.attackRange = attackRange;
        this.attackStackProvider = attackStackProvider;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public ItemStack getAttackStack() {
        return attackStackProvider.get();
    }

    public LaneEntityTask createAttackTask(LaneEntity laneEntity) {
        //TODO uh... generic provider fn
        return null;
    }
}
