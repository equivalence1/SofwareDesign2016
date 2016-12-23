package ru.mit.spbau.model.map;

import org.jetbrains.annotations.NotNull;

/**
 * Defines one cell of our map
 */
public final class MapCell {

    @NotNull private TextureType texture;
    @NotNull private VisabilityType visability;

    public MapCell(@NotNull TextureType texture, @NotNull VisabilityType visability) {
        this.texture = texture;
        this.visability = visability;

        if (texture == TextureType.UNKNOWN && visability != VisabilityType.UNVISITED) {
            throw new IllegalArgumentException("Cell which contain nothing can not be visited or visible");
        }
    }

    public MapCell(@NotNull TextureType texture) {
        this(texture, VisabilityType.UNVISITED);
    }

    public TextureType  getTexture() {
        return texture;
    }

    public VisabilityType getVisability() {
        return visability;
    }

    public void setTexture(@NotNull TextureType texture) {
        this.texture = texture;
    }

    public void setVisability(@NotNull VisabilityType visability) {
        this.visability = visability;
    }

    /**
     * What this cell contains in terms of texture (i.e. game objects are not counted)
     */
    public enum TextureType {
        EMPTY,
        WALL,
        WATER,
        EXIT,
        UNKNOWN
    }

    /**
     * Defines how unit sees what happens in this cell
     */
    public enum VisabilityType {
        VISIBLE,
        VISITED,
        UNVISITED
    }

}
