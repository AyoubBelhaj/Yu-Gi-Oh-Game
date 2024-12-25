package org.example.yugiohgame;

public class BaseCard implements Card {
    private final String name;
    private final int attack;
    private final int defense;
    private final String description;
    private final String imageUrl;

    public BaseCard(String name, int attack, int defense, String description, String imageUrl) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}