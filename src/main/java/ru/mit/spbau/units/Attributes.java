package ru.mit.spbau.units;

import ru.mit.spbau.Bufs.Buff;

/**
 * JavaBean
 * Contains all attributes of unit
 */
public final class Attributes {

    private int maxHp;
    private int currentHp;
    private double visionRange;
    private int attack;
    private final AttributesGetter getter;

    public Attributes(int maxHp, double visionRange, int attack) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.visionRange = visionRange;
        this.attack = attack;
        this.getter = new AttributesGetter();
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int hp) {
        maxHp = hp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int hp) {
        currentHp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int newAttack) {
        attack = newAttack;
    }

    public double getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(double newVisionRange) {
        visionRange = newVisionRange;
    }

    public void applyBuff(Buff buff) {
        buff.apply(this);
    }

    public void removeBuff(Buff buff) {
        buff.remove(this);
    }

    public AttributesGetter getGetter() {
        return getter;
    }

    /**
     * This class is needed to prevent strategies from cheating.
     * Strategies should be able only to get attributes to choose their moves
     * they should not modify it -- it will do our TODO SomeClass
     */
    private class AttributesGetter {

        public int getMaxHp() {
            return maxHp;
        }

        public int getCurrentHp() {
            return currentHp;
        }

        public int getAttack() {
            return attack;
        }

        public double getVisionRange() {
            return visionRange;
        }

    }

}
