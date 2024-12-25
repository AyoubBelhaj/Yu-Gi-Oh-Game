package org.example.yugiohgame;

public class AttackPhase implements GamePhase {
    @Override
    public void drawCard(Game game) {
        System.out.println("Cannot draw cards in Attack Phase.");
    }

    @Override
    public void playCard(Game game) {
        System.out.println("Cannot play cards in Attack Phase.");
    }

    @Override
    public void attack(Game game) {
        game.attackOpponent();
        System.out.println("Attack performed!");
    }

    @Override
    public void endTurn(Game game) {
        game.setPhase(new EndPhase());
        System.out.println("End turn!");
    }
}
