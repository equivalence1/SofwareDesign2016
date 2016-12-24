package ru.mit.spbau.model.units.users;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.model.Items.Item;
import ru.mit.spbau.model.Items.ItemType;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.strategies.users.PlayerStrategy;
import ru.mit.spbau.model.units.Attributes;
import ru.mit.spbau.model.units.Inventory;
import ru.mit.spbau.model.units.Unit;
import ru.mit.spbau.model.units.creeps.CreepUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Player unit can be potentially very different from creep unit.
 * It is true in our case as well:
 * 1. Player has inventory and creeps don't.
 * 2. When user dies we should stop the game.
 * 3. Player and creep may have different kinds of moves
 *
 * This I think gives us enough reasons to split {@link Unit} class
 * into at least two classes -- this one and {@link CreepUnit}
 *
 * Also it may seem strange that this class has method {@link PlayerUnit#nextMove(RelativeMap, int)},
 * same as {@link PlayerStrategy} does and it only calls {@link PlayerStrategy#nextMove(RelativeMap, int)}
 * and I could just implement strategy right here.
 * This is done due to security reasons. I want all strategies to be completely separated from all
 * other logic and have no ability to cheat. In this particular case I don't want them to have
 * any ability to obtain reference on the corresponding {@link Unit} object.
 */
public final class PlayerUnit extends Unit {

    @NotNull private static final Logger LOGGER = Logger.getLogger(PlayerUnit.class.getName());
    @NotNull private static final Attributes DEFAULT_ATTRIBUTES = new Attributes(100, 10.0, 10);

    private final PlayerStrategy playerStrategy; // can be null in copy
    @NotNull private final Inventory inventory;

    public PlayerUnit(@NotNull Position pos, PlayerStrategy playerStrategy, @NotNull Attributes attributes) {
        super(pos, attributes);
        this.playerStrategy = playerStrategy;
        this.inventory = new Inventory();
    }

    private PlayerUnit(@NotNull PlayerUnit playerUnit) {
        super(playerUnit);
        this.playerStrategy = null;
        this.inventory = playerUnit.inventory.copy();
    }

    public PlayerUnit(@NotNull Position pos, @NotNull PlayerStrategy playerStrategy) {
        this(pos, playerStrategy, DEFAULT_ATTRIBUTES);
    }

    /**
     * Notify player's unit that he lost
     * @param name player's name
     * @param score player's score
     */
    public void notifyLose(@NotNull String name, int score) {
        playerStrategy.notifyLose(name, score);
    }

    /**
     * Notify player's unit that he won
     * @param name player's name
     * @param score player's score
     */
    public void notifyWin(@NotNull String name, int score) {
        playerStrategy.notifyWin(name, score);
    }

    /**
     * Make a single move
     * @param relativeMap map as this unit sees it
     * @param score player's score
     * @return next move
     */
    @NotNull
    public UserMove nextMove(@NotNull RelativeMap relativeMap, int score) {
        try {
            return playerStrategy.nextMove(relativeMap, score);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception occurred in player's strategy.", e);
            return UserMove.UNKNOWN;
        }
    }

    /**
     * Pick item to inventory if we can
     * @param item item to pick
     */
    public void pickItem(@NotNull Item item) {
        if (item.getItemClass() != ItemType.INSTA_USE) {
            inventory.pickItem(item);
        }
        item.pick(this);
    }

    /**
     * Drop item of given type from inventory
     * @param itemType item type to drop
     */
    @Nullable
    public Item dropItem(@NotNull ItemType itemType) {
        final Item item = inventory.dropType(itemType);
        if (item != null) {
            item.drop();
        }
        return item;
    }

    /**
     * Checks if we can take this item
     * (for now we just can not carry two items of a same {@link ItemType}
     *
     * @param item item to check
     * @return true if we can pick
     */
    public boolean canTake(@NotNull Item item) {
        return inventory.getItems().stream().filter(i -> i.getItemClass() == item.getItemClass()).count() == 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public PlayerUnit copy() {
        return new PlayerUnit(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final int getCost() {
        return 0;
    }

}
