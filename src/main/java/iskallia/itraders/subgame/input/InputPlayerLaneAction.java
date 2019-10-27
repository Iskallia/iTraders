package iskallia.itraders.subgame.input;

import iskallia.itraders.subgame.GameController;
import iskallia.itraders.subgame.GamePlayer;
import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.task.MoveAttackEntityTask;

public class InputPlayerLaneAction extends GameInput {

    private final GamePlayer player;
    private final GameLane lane;

    public InputPlayerLaneAction(GamePlayer player, GameLane lane) {
        this.player = player;
        this.lane = lane;
    }

    @Override
    public GamePlayer getPlayer() {
        return this.player;
    }

    @Override
    public void processInput(GameController ctrl) {
        if (ctrl.isPlayer1(this.getPlayer()) && lane.getEntityPlayer1() != null) {
            lane.getEntityPlayer1().setRunningTask(MoveAttackEntityTask::new);
        }
        if (ctrl.isPlayer2(this.getPlayer()) && lane.getEntityPlayer2() != null) {
            lane.getEntityPlayer2().setRunningTask(MoveAttackEntityTask::new);
        }
    }
}
