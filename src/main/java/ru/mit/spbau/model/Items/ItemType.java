package ru.mit.spbau.model.Items;

/**
 * Defines which class this item belongs to.
 *
 * if item marked as INSTA_USE then it wont be
 * moved to player's inventory but will be
 * instantly used
 */
public enum ItemType {
    WEAPON,
    ARMOR,
    INSTA_USE,
    GRAAL
}
