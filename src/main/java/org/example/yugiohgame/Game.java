// Game.java
package org.example.yugiohgame;

public class Game {
    private GamePhase currentPhase;
    private final Player player1;
    private final Player player2;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private Player currentPlayer;
    private YugiohGameApp ui;

    public Game(Player player1, Player player2) {
        this.currentPhase = new DrawPhase();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public void setUI(YugiohGameApp ui) {
        this.ui = ui;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setPhase(GamePhase phase) {
        this.currentPhase = phase;
    }

    public void drawCard() {
        currentPhase.drawCard(this);
        if (ui != null) {
            ui.updateUI();
        }
    }

    public void playCard() {
        currentPhase.playCard(this);
        if (ui != null) {
            ui.updateUI();
        }
    }

    public void attack() {
        currentPhase.attack(this);
        if (ui != null) {
            ui.updateUI();
        }
    }

    public void endTurn() {
        currentPhase.endTurn(this);
        if (ui != null) {
            ui.updateUI();
        }
    }

    public void drawCardFromDeck() {
        currentPlayer.drawCard();
    }

    public void placeMonsterOrSpell(int handIndex) {
        currentPlayer.placeCardOnField(handIndex);
    }

    public void attackOpponent() {
        Player opponent = currentPlayer == player1 ? player2 : player1;
        currentPlayer.attack(opponent);
        checkGameOver();
    }

    public void switchTurn() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
        setPhase(new DrawPhase());
    }

    private void checkGameOver() {
        if (player1.getLifePoints() <= 0) {
            ui.displayGameOver("Player 2");
        } else if (player2.getLifePoints() <= 0) {
            ui.displayGameOver("Player 1");
        }
    }
}
