package ru.mit.spbau.model.map;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.units.Unit;

import java.util.List;

/**
 * This class defines map relatively to some unit (i.e. known map + all objects this unit can see right now)
 */
public final class RelativeMap {

    @NotNull private final MapCell[][] knownMap;
    @NotNull private final GameObject self;
    @NotNull private final List<GameObject> objects;
    private final double visabilityRange;

    private final int mapHeight;
    private final int mapWidth;

    public RelativeMap(@NotNull MapCell[][] knownMap,
                       @NotNull GameObject self,
                       @NotNull List<GameObject> objects) {
        this.knownMap = knownMap;
        mapHeight = knownMap.length;
        if (mapHeight != 0) {
            mapWidth = knownMap[0].length;
        } else {
            mapWidth = 0;
        }
        this.self = self;
        this.objects = objects;
        if (self instanceof Unit) {
            visabilityRange = ((Unit)self).getAttributes().getVisionRange();
        } else {
            visabilityRange = 0;
        }
    }

    @NotNull
    public MapCell[][] getKnownMap() {
        return knownMap;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    @NotNull
    public GameObject getSelf() {
        return self;
    }

    @NotNull
    public List<GameObject> getObjects() {
        return objects;
    }

    public double getVisabilityRange() {
        return visabilityRange;
    }

}
