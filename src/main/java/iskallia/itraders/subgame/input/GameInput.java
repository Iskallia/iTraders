package iskallia.itraders.subgame.input;

import iskallia.itraders.subgame.GameController;
import iskallia.itraders.subgame.GamePlayer;

public abstract class GameInput {

    public abstract GamePlayer getPlayer();

    public abstract void processInput(GameController ctrl);

}
