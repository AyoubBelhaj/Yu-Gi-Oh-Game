package org.example.yugiohgame;

public class BuffedCard implements Card {
    private final Card baseCard;
    private final int extraAttack;

    public BuffedCard(Card baseCard, int extraAttack) {
        this.baseCard = baseCard;
        this.extraAttack = extraAttack;
    }

    @Override
    public String getName() {
        return baseCard.getName();
    }

    @Override
    public int getAttack() {
        return baseCard.getAttack() + extraAttack;
    }

    @Override
    public int getDefense() {
        return baseCard.getDefense();
    }

    @Override
    public String getDescription() {
        return baseCard.getDescription();
    }

    @Override
    public String getImageUrl() {
        return baseCard.getImageUrl();
    }
}