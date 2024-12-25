package org.example.yugiohgame;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> deck = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();
    private Card monsterSlot = null;
    private int lifePoints = 4000;
    private boolean isMonsterInAttackPosition = true;
    private boolean bufferApplied = false;

    public Player(List<Card> deck) {
        this.deck = deck;
        this.hand = new ArrayList<>();
        drawCard();
    }

    public void drawCard() {
        if (!deck.isEmpty()) {
            Card card = deck.remove(0);
            hand.add(card);
        } else {
            System.out.println("The deck is empty! No cards to draw.");
        }
    }


    public void placeCardOnField(int handIndex) {
        if (handIndex < 0 || handIndex >= hand.size()) {
            System.out.println("Invalid hand index.");
            return;
        }

        Card card = hand.get(handIndex);

        if (card instanceof BaseCard && monsterSlot == null) {
            monsterSlot = (BaseCard) card;
        } else {
            System.out.println("Cannot place the card. Either it's not a BaseCard or the slot is already occupied.");
            return;
        }

        hand.remove(handIndex);
    }

    public void resetBuffer() {
        bufferApplied = false;
    }

    public void attack(Player opponent) {
        if (monsterSlot == null) {
            System.out.println("No monster available to attack.");
            return;
        }

        if (opponent.monsterSlot != null) {

            System.out.println("A battle between monsters has started!");
            if (opponent.isMonsterInAttackPosition) {
                int attackdifferent = monsterSlot.getAttack() - opponent.monsterSlot.getAttack();
                if (attackdifferent > 0) {

                    opponent.lifePoints = opponent.lifePoints - attackdifferent;
                    System.out.println("Opponent's monster is destroyed!");
                    opponent.monsterSlot = null;
                } else if (attackdifferent < 0) {
                    lifePoints = lifePoints + attackdifferent;
                    System.out.println("Your monster is destroyed!");
                    monsterSlot = null;
                }else {
                    System.out.println("Both monsters are destroyed!");
                    opponent.monsterSlot = null;
                    monsterSlot = null;
                }
            }else {
                int attackDifference = monsterSlot.getAttack() - opponent.monsterSlot.getDefense();
                if (attackDifference > 0 ) {
                    System.out.println("Opponent's monster is destroyed!");
                    opponent.monsterSlot = null;    
                } else if (attackDifference < 0) {
                    lifePoints += attackDifference;
                    System.out.println("Your attack failed! You take " + (-attackDifference) + " damage.");
                }else {
                    System.out.println("Both monsters survive the battle.");
                }
            }
        } else {
            System.out.println("Direct attack on the opponent!");
            opponent.lifePoints -= monsterSlot.getAttack();

        }

    }

    public void applyBonus(int bonus) {
        if (monsterSlot != null && !bufferApplied) {
            monsterSlot = new BuffedCard(monsterSlot, bonus);
            bufferApplied = true;
        }
    }

    public void setMonsterSlot(Card card) {
        if (card instanceof BaseCard) {
            this.monsterSlot = (BaseCard) card;
        } else {
            System.out.println("Only BaseCard can be placed in the monster slot.");
        }
    }

    public boolean isMonsterInAttackPosition() { return isMonsterInAttackPosition; }
    public void changeMonsterPosition() { isMonsterInAttackPosition = !isMonsterInAttackPosition; }

    public void endTurn() {
        System.out.println("Turn ended.");
    }

    // Getters
    public List<Card> getHand() {
        return hand;
    }

    public Card getMonsterSlot() {
        return monsterSlot;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void displayHand() {
        System.out.println("Your hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ": " + hand.get(i).getName());
        }
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }
}
