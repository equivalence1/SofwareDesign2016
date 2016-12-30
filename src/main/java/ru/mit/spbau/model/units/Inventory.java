package ru.mit.spbau.model.units;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.model.Copyable;
import ru.mit.spbau.model.Items.Item;
import ru.mit.spbau.model.Items.ItemType;

import java.util.HashSet;
import java.util.Set;

public final class Inventory implements Copyable {

    @NotNull private final Set<Item> items;

    public Inventory() {
        items = new HashSet<>();
    }

    private Inventory(@NotNull Inventory inventory) {
        this.items = new HashSet<>();
        inventory.items.forEach(i -> this.items.add((Item)i.copy()));
    }

    /**
     * Add item to inventory
     */
    public void pickItem(@NotNull Item item) {
        items.add(item);
    }


    /**
     * Drop item of a given type if exists
     * @return dropped item or null
     */
    @Nullable
    public Item dropType(@NotNull ItemType itemType) {
        for (Item item : items) {
            if (item.getItemClass() == itemType) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    /**
     * @return true if contains item of a give type
     */
    public boolean contains(@NotNull ItemType itemType) {
        for (Item item : items) {
            if (item.getItemClass() == itemType) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public Set<Item> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Inventory copy() {
        return new Inventory(this);
    }

}
