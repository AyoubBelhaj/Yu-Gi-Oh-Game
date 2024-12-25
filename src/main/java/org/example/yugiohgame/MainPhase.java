package org.example.yugiohgame;

public class MainPhase implements GamePhase {
    @Override
    public void drawCard(Game game) {
        System.out.println("Cannot draw cards in Main Phase.");
    }

    @Override
    public void playCard(Game game) {
        int selectedCardIndex = 0;  // This would be determined by the player selecting a card (index from the hand)
        game.placeMonsterOrSpell(selectedCardIndex);
        System.out.println("Card played!");
    }

    @Override
    public void attack(Game game) {
        game.setPhase(new AttackPhase());
        System.out.println("Moving to Attack Phase.");
    }

    @Override
    public void endTurn(Game game) {
        game.setPhase(new EndPhase());
        System.out.println("End turn!");
    }
}
