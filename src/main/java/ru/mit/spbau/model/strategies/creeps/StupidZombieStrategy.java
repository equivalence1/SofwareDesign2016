package ru.mit.spbau.model.strategies.creeps;

import org.jetbrains.annotations.NotNull;
import org.omg.CORBA.PUBLIC_MEMBER;
import ru.mit.spbau.model.game.GameObject;
import ru.mit.spbau.model.game.Player;
import ru.mit.spbau.model.map.RelativeMap;
import ru.mit.spbau.model.units.creeps.CreepMove;
import ru.mit.spbau.model.units.users.PlayerUnit;

/**
 * Stupid creep strategy which run to player if he sees it
 * and and just stands still if he does not.
 */
public final class StupidZombieStrategy implements CreepStrategy {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public CreepMove nextMove(@NotNull RelativeMap map) {
        PlayerUnit playerUnit = null;
        for (GameObject object : map.getObjects()) {
            if (object instanceof PlayerUnit) {
                playerUnit = (PlayerUnit)object;
            }
        }

        if (playerUnit == null) {
            return CreepMove.NO_MOVE;
        }

        if (playerUnit.getPosition().getX() < map.getSelf().getPosition().getX()) {
            return CreepMove.LEFT;
        }

        if (playerUnit.getPosition().getX() > map.getSelf().getPosition().getX()) {
            return CreepMove.RIGHT;
        }

        if (playerUnit.getPosition().getY() < map.getSelf().getPosition().getY()) {
            return CreepMove.UP;
        }

        if (playerUnit.getPosition().getY() > map.getSelf().getPosition().getY()) {
            return CreepMove.DOWN;
        }

        return CreepMove.NO_MOVE;
    }

}
