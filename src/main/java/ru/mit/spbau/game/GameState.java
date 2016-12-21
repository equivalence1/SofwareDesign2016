package ru.mit.spbau.game;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.map.MapCell;
import ru.mit.spbau.units.CreepUnit;
import ru.mit.spbau.units.PlayerUnit;
import ru.mit.spbau.units.Position;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class GameState {

    @NotNull private final MapCell[][] map;
    private PlayerUnit player;
    @NotNull private final Set<CreepUnit> creeps;

    private GameState(List<String> lines) throws IOException {
        creeps = new HashSet<>();

        final int height = lines.size();
        final int width = lines.stream().map(String::length).max(Integer::compare).orElseGet(() -> 0);

        map = new MapCell[height][width];
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            for (int j = 0; j < width; j++) {
                if (line.length() < j) {
                    setChar(i, j, line.charAt(j));
                } else {
                    setChar(i, j, ' ');
                }
            }
        }

        if (player == null) {
            throw new IOException("Map does not contain user character");
        }
    }

    /**
     * Read map from file and generate state from it
     * @param file containing map
     * @return state
     * @throws IOException in case of error
     */
    public static GameState fromFile(@NotNull File file) throws IOException {
        final Scanner scanner = new Scanner(file);
        final List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        return new GameState(lines);
    }

    public MapCell[][] getMap() {
        return map;
    }

    public PlayerUnit getPlayerUnit() {
        return player;
    }

    private void setChar(int i, int j, char ch) throws IOException {
        map[i][j] = makeMapCell(i, j, ch);
    }

    @NotNull
    private MapCell makeMapCell(int i, int j, char ch) throws IOException {
        switch (ch) {
            case ' ':
                return new MapCell(MapCell.TextureType.EMPTY);
            case 'W':
                return new MapCell(MapCell.TextureType.WATER);
            case '#':
                return new MapCell(MapCell.TextureType.WALL);
            case 'E':
                return new MapCell(MapCell.TextureType.EXIT);
            case 'Z':
                addZombie(new Position(i, j));
                return new MapCell(MapCell.TextureType.EMPTY);
            case 'U':
                addPlayer(new Position(i, j));
                return new MapCell(MapCell.TextureType.EMPTY);
            default:
                throw new IOException("unknown symbol '" + ch + "'");
        }
    }

    private void addZombie(@NotNull Position pos) {
        creeps.add(new CreepUnit());
    }

    private void addPlayer(@NotNull Position pos) throws IllegalStateException {
        if (player != null) {
            throw new IllegalStateException("Two users on map: we only support single user mode for now");
        }
        player = new PlayerUnit(); // TODO add position
    }

}
