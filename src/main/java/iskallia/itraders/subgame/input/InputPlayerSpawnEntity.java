package iskallia.itraders.subgame.input;

import iskallia.itraders.card.SubCardData;
import iskallia.itraders.subgame.GameCardEntity;
import iskallia.itraders.subgame.GameController;
import iskallia.itraders.subgame.GamePlayer;
import iskallia.itraders.subgame.lane.GameLane;

import java.util.UUID;

public class InputPlayerSpawnEntity extends GameInput {

    private final GamePlayer player;
    private final GameLane lane;
    private final SubCardData data;
    private final UUID cardEntityUUID;

    public InputPlayerSpawnEntity(GamePlayer player, GameLane lane, SubCardData data, UUID cardEntityUUID) {
        this.player = player;
        this.lane = lane;
        this.data = data;
        this.cardEntityUUID = cardEntityUUID;
    }

    @Override
    public GamePlayer getPlayer() {
        return this.player;
    }

    @Override
    public void processInput(GameController ctrl) {
        if (ctrl.isPlayer1(getPlayer()) && this.lane.getEntityPlayer1() == null) {
            this.lane.spawnLaneEntityPlayer1(ctrl, new GameCardEntity(getPlayer(), this.data, this.cardEntityUUID));
        }
        if (ctrl.isPlayer2(getPlayer()) && this.lane.getEntityPlayer2() == null) {
            this.lane.spawnLaneEntityPlayer2(ctrl, new GameCardEntity(getPlayer(), this.data, this.cardEntityUUID));
        }
    }
}
