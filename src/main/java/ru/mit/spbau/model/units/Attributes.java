package ru.mit.spbau.model.units;

import ru.mit.spbau.model.Buffs.Buff;
import ru.mit.spbau.model.Copyable;

/**
 * JavaBean
 * Contains all attributes of unit
 */
public final class Attributes implements Copyable {

    private int maxHp;
    private int currentHp;
    private double visionRange;
    private int attack;

    public Attributes(int maxHp, double visionRange, int attack) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.visionRange = visionRange;
        this.attack = attack;
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

    public Attributes copy() {
        final Attributes attributes = new Attributes(maxHp, visionRange, attack);
        attributes.currentHp = currentHp;
        return attributes;
    }

}
