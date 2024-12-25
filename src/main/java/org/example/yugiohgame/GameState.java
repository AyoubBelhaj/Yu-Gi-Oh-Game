package org.example.yugiohgame;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final List<Card> deck;
    private final List<Card> field;
    private final int playerLifePoints;
    private final int opponentLifePoints;

    public GameState(List<Card> deck, List<Card> field, int playerLifePoints, int opponentLifePoints) {
        this.deck = new ArrayList<>(deck);
        this.field = new ArrayList<>(field);
        this.playerLifePoints = playerLifePoints;
        this.opponentLifePoints = opponentLifePoints;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getField() {
        return field;
    }

    public int getPlayerLifePoints() {
        return playerLifePoints;
    }

    public int getOpponentLifePoints() {
        return opponentLifePoints;
    }
}
