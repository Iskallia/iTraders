package iskallia.itraders.subgame;

import java.util.function.Supplier;

public enum GamePhase {

    PLAYER_INPUT_1(false, () -> 1),
    RUN_TICK_1    (true,  () -> 2),
    PLAYER_INPUT_2(false, () -> 3),
    RUN_TICK_2    (true,  () -> 0),

    STARTUP(false, () -> 0),
    FINISH (false, () -> 5);

    private final boolean runGameTick;
    private Supplier<Integer> nextPhase;

    GamePhase(boolean runAndProgressGameTick, Supplier<Integer> nextPhase) {
        this.runGameTick = runAndProgressGameTick;
        this.nextPhase = nextPhase;
    }

    public boolean doesRunGameTick() {
        return runGameTick;
    }

    public GamePhase getNextPhase() {
        return GamePhase.values()[nextPhase.get()];
    }

}
