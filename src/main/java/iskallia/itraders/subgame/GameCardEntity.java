package iskallia.itraders.subgame;

import iskallia.itraders.card.SubCardData;

import java.util.UUID;

public class GameCardEntity {

    private final GamePlayer owner;
    private final SubCardData cardData;
    private final UUID cardEntityPlayerUUID;

    public GameCardEntity(GamePlayer owner, SubCardData cardData, UUID cardEntityPlayerUUID) {
        this.owner = owner;
        this.cardData = cardData;
        this.cardEntityPlayerUUID = cardEntityPlayerUUID;
    }

    public GamePlayer getOwner() {
        return owner;
    }

    public SubCardData getCardData() {
        return cardData;
    }

    public UUID getCardEntityPlayerUUID() {
        return cardEntityPlayerUUID;
    }
}
