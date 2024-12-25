package org.example.yugiohgame;

public class EndPhase implements GamePhase {
    @Override
    public void drawCard(Game game) {
        System.out.println("Cannot draw cards in End Phase.");
    }

    @Override
    public void playCard(Game game) {
        System.out.println("Cannot play cards in End Phase.");
    }

    @Override
    public void attack(Game game) {
        System.out.println("Cannot attack in End Phase.");
    }

    @Override
    public void endTurn(Game game) {
        game.switchTurn();
        System.out.println("Switching turn to the other player.");
        game.getCurrentPlayer().resetBuffer();
    }
}
