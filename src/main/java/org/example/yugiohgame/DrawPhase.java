package org.example.yugiohgame;

public class DrawPhase implements GamePhase {
    @Override
    public void drawCard(Game game) {
        game.drawCardFromDeck();
        System.out.println("Card drawn!");
        game.setPhase(new MainPhase());
    }

    @Override
    public void playCard(Game game) {
        System.out.println("Cannot play cards in Draw Phase.");
    }

    @Override
    public void attack(Game game) {
        System.out.println("Cannot attack in Draw Phase.");
    }

    @Override
    public void endTurn(Game game) {
        System.out.println("Cannot end turn in Draw Phase.");
    }
}
