package ru.mit.spbau.model.map;

import org.jetbrains.annotations.NotNull;

public final class MapCell {

    @NotNull private final TextureType texture;
    @NotNull private final VisabilityType visability;

    public MapCell(@NotNull TextureType texture, @NotNull VisabilityType visability) {
        this.texture = texture;
        this.visability = visability;

        if (texture == TextureType.NOTHING && visability != VisabilityType.UNVISITED) {
            throw new IllegalArgumentException("Cell which contain nothing can not be visited or visible");
        }
    }

    public MapCell(@NotNull TextureType texture) {
        this(texture, VisabilityType.UNVISITED);
    }

    public TextureType getTexture() {
        return texture;
    }

    public VisabilityType getVisability() {
        return visability;
    }

    /**
     * What this cell contains in terms of texture (i.e. units not counted)
     */
    public enum TextureType {
        EMPTY,
        WALL,
        WATER,
        EXIT,
        NOTHING
    }

    /**
     * Defined if unit sees what happens in this cell
     */
    public enum VisabilityType {
        VISIBLE,
        VISITED,
        UNVISITED
    }

}
