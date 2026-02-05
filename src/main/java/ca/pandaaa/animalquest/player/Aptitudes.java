package ca.pandaaa.animalquest.player;

public class Aptitudes {
    private int strength;
    private int health;
    private int mana;

    public Aptitudes() {
        this.strength = 0;
        this.health = 0;
        this.mana = 0;
    }

    public Aptitudes(int strength, int health, int mana) {
        this.strength = strength;
        this.health = health;
        this.mana = mana;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getTotalPointsUsed() {
        return strength + health + mana;
    }

}
