package org.example.yugiohgame;

public interface GamePhase {
    void drawCard(Game game);
    void playCard(Game game);
    void attack(Game game);
    void endTurn(Game game);
}