package ru.mit.spbau.model.map;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.game.Position;
import ru.mit.spbau.model.strategies.users.PlayerStrategy;
import ru.mit.spbau.model.units.Unit;
import ru.mit.spbau.model.units.creeps.ZombieCreep;
import ru.mit.spbau.model.units.creeps.CreepMove;
import ru.mit.spbau.model.units.creeps.CreepUnit;
import ru.mit.spbau.model.units.users.PlayerUnit;
import ru.mit.spbau.model.units.users.UserMove;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains our map for current level and
 * also does all the logic related to units movement.
 */
public final class LevelMap {

    @NotNull private static final Logger LOGGER = Logger.getLogger(LevelMap.class.getName());
    private static final double EPS = 1e-9; // this one is only for operations with doubles

    @NotNull private final PlayerStrategy playerStrategy;

    private PlayerUnit playerUnit;
    @NotNull private List<GameObject> gameObjects;
    @NotNull private final MapCell[][] map;
    private final int mapWidth;
    private final int mapHeight;

    private LevelMap(@NotNull List<String> lines, @NotNull PlayerStrategy playerStrategy) throws IOException {
        this.gameObjects = new ArrayList<>();
        this.playerStrategy = playerStrategy;

        mapHeight = lines.size();
        mapWidth = lines.stream().map(String::length).max(Integer::compare).orElseGet(() -> 0);

        map = new MapCell[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            final String line = lines.get(i);
            for (int j = 0; j < mapWidth; j++) {
                if (line.length() < j) {
                    makeCell(i, j, ' ');
                } else {
                    makeCell(i, j, line.charAt(j));
                }
            }
        }

        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof PlayerUnit) {
                playerUnit = (PlayerUnit)gameObject;
                break;
            }
        }

        assert playerUnit != null : "No player unit on map";
    }

    /**
     * Read map from file and generate state from it
     * @param file containing map
     * @param playerStrategy player strategy to use in player's unit created
     * @return state
     * @throws IOException in case of error
     */
    public static LevelMap fromFile(@NotNull File file, @NotNull PlayerStrategy playerStrategy) throws IOException {
        final Scanner scanner = new Scanner(file);
        final List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        return new LevelMap(lines, playerStrategy);
    }

    @NotNull
    public MapCell[][] getMap() {
        return map;
    }

    @NotNull
    public PlayerUnit getPlayerUnit() {
        return playerUnit;
    }

    /**
     * Out of all {@link GameObject}s on map find all units
     * @return all units on map
     */
    @NotNull
    public List<Unit> getAllUnits() {
        final List<Unit> units = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Unit) {
                units.add((Unit)gameObject);
            }
        }
        return units;
    }

    /**
     * Out of all {@link Unit}s on map find all creeps
     * @return list of all creeps on map
     */
    public List<CreepUnit> getAllCreeps() {
        final List<CreepUnit> creeps = new ArrayList<>();
        for (Unit unit : getAllUnits()) {
            if (unit instanceof CreepUnit) {
                creeps.add((CreepUnit)unit);
            }
        }
        return creeps;
    }

    /**
     * Update know maps for all units on map
     */
    public void updateKnownMaps() {
        getAllUnits().forEach(this::updateKnownMap);
    }

    /**
     * Update {@link Unit#knownMap} for a given unit
     * @param unit for whom to update map
     * @return unit's map after update
     */
    public MapCell[][] updateKnownMap(@NotNull Unit unit) {
        if (unit.getKnownMap() == null) {
            initMap(unit);
        }
        final MapCell[][] knownMap = unit.getKnownMap();

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (doesSee(unit, new Position(j, i))) {
                    knownMap[i][j].setTexture(map[i][j].getTexture());
                    knownMap[i][j].setVisability(MapCell.VisabilityType.VISIBLE);
                } else {
                    if (knownMap[i][j].getVisability() == MapCell.VisabilityType.VISIBLE) {
                        knownMap[i][j].setVisability(MapCell.VisabilityType.VISITED);
                    }
                }
            }
        }

        return knownMap;
    }

    /**
     * Get map relative to some unit
     * @param unit for whom to generate map
     * @return relative map for this unit
     */
    @NotNull
    public RelativeMap getRelativeMap(@NotNull Unit unit) {
        final List<GameObject> objects = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (unit != gameObject && doesSee(unit, gameObject.getPosition())) {
                objects.add(gameObject.copy());
            }
        }
        return new RelativeMap(unit.getKnownMap(), unit.copy(), objects);
    }

    /**
     * let every unit perform one move
     */
    public void doOneMove() {
        updateKnownMaps();
        final UserMove playersMove = getPlayerUnit().nextMove(getRelativeMap(getPlayerUnit()));
        if (playersMove == UserMove.UNKNOWN) {
            LOGGER.log(Level.INFO, "User tried to perform unknown move");
            return;
        }

        if (playersMove == UserMove.PICK) {
            pickItem(playerUnit);
        }

        final List<Unit> units = new ArrayList<>();
        final List<Position.Direction> directions = new ArrayList<>();

        units.add(playerUnit);
        directions.add(getDirection(playersMove));

        for (CreepUnit creepUnit : getAllCreeps()) {
            CreepMove creepMove = creepUnit.nextMove(getRelativeMap(creepUnit));
            directions.add(getDirection(creepMove));
            units.add(creepUnit);
        }

        final boolean[] moveDone = new boolean[units.size()];
        for (int i = 0; i < moveDone.length; i++) {
            moveDone[i] = false;
        }

        for (int i = 0; i < directions.size(); i++) {
            final Unit current = units.get(i);
            if (!current.isAlive()) {
                continue;
            }
            if (moveDone[i]) {
                continue;
            }
            for (int j = i + 1; j < directions.size(); j++) {
                if (!units.get(j).isAlive()) {
                    continue;
                }
                if (doesIntersect(current.getPosition(), directions.get(i),
                        units.get(j).getPosition(), directions.get(j))) {
                    unitsFight(current, units.get(j));
                    moveDone[i] = true;
                    moveDone[j] = true;
                    if (!current.isAlive()) {
                        break;
                    }
                }
            }
            if (!current.isAlive()) {
                continue;
            }
            if (!moveDone[i] && canMoveToDirection(current.getPosition(), directions.get(i))) {
                current.move(directions.get(i));
            }
            moveDone[i] = true;
        }

        refreshGameObjects();
    }

    private boolean doesIntersect(Position pos1, Position.Direction direction1,
                                  Position pos2, Position.Direction direction2) {
        if (pos1.equals(pos2.copy().move(direction2))
                && pos2.equals(pos1.copy().move(direction1))) {
            return true;
        }
        if (pos1.copy().move(direction1).equals(pos2.copy().move(direction2))) {
            return true;
        }
        return false;
    }

    private boolean canMoveToDirection(Position pos, Position.Direction direction) {
        final Position afterMove = pos.copy().move(direction);
        if (afterMove.getX() < 0 || afterMove.getY() < 0 || afterMove.getX() >= mapWidth
                || afterMove.getY() >= mapHeight) {
            return false;
        }
        return (map[afterMove.getY()][afterMove.getX()].getTexture() == MapCell.TextureType.EMPTY);
    }

    private Position.Direction getDirection(@NotNull UserMove userMove) {
        switch (userMove) {
            case UP:
                return Position.Direction.UP;
            case DOWN:
                return Position.Direction.DOWN;
            case LEFT:
                return Position.Direction.LEFT;
            case RIGHT:
                return Position.Direction.RIGHT;
            default:
                return Position.Direction.HOLD;
        }
    }

    private Position.Direction getDirection(@NotNull CreepMove creepMove) {
        switch (creepMove) {
            case UP:
                return Position.Direction.UP;
            case DOWN:
                return Position.Direction.DOWN;
            case LEFT:
                return Position.Direction.LEFT;
            case RIGHT:
                return Position.Direction.RIGHT;
            default:
                return Position.Direction.HOLD;
        }
    }

    private void pickItem(@NotNull PlayerUnit playerUnit) {

    }

    private void unitsFight(@NotNull Unit unit1, @NotNull Unit unit2) {
        unit1.attacked(unit2.getAttributes().getAttack());
        unit2.attacked(unit1.getAttributes().getAttack());
    }

    private void refreshGameObjects() {
        final List<GameObject> newGameObjects = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Unit) {
                if (((Unit) gameObject).isAlive()) {
                    newGameObjects.add(gameObject);
                }
            } else {
                newGameObjects.add(gameObject);
            }
        }
        gameObjects = newGameObjects;
    }

    private void initMap(@NotNull Unit unit) {
        final MapCell[][] tmpMap = new MapCell[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                tmpMap[i][j] = new MapCell(MapCell.TextureType.UNKNOWN, MapCell.VisabilityType.UNVISITED);
            }
        }
        unit.setKnownMap(tmpMap);
    }

    private boolean doesSee(@NotNull Unit unit, @NotNull Position pos) {
        return (unit.getPosition().getDistance(pos) <= unit.getAttributes().getVisionRange() + EPS);
    }


    private void makeCell(int i, int j, char ch) throws IOException {
        map[i][j] = makeMapCell(i, j, ch);
    }

    @NotNull
    private MapCell makeMapCell(int i, int j, char ch) throws IOException {
        switch (ch) {
            case '.':
                return new MapCell(MapCell.TextureType.EMPTY);
            case 'W':
                return new MapCell(MapCell.TextureType.WATER);
            case '#':
                return new MapCell(MapCell.TextureType.WALL);
            case 'E':
                return new MapCell(MapCell.TextureType.EXIT);
            case 'Z':
                gameObjects.add(new ZombieCreep(new Position(i, j)));
                return new MapCell(MapCell.TextureType.EMPTY);
            case 'U':
                gameObjects.add(new PlayerUnit(new Position(i, j), playerStrategy));
                return new MapCell(MapCell.TextureType.EMPTY);
            default:
                throw new IOException("unknown symbol '" + ch + "'");
        }
    }

}
