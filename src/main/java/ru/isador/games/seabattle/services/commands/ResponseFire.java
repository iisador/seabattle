package ru.isador.games.seabattle.services.commands;

import java.util.Map;

import ru.isador.games.seabattle.web.game.FireResult;

public class ResponseFire extends Response {

    private final String attacker;
    private final String opponent;
    private final int x;
    private final int y;
    private final FireResult result;
    private final char[][] matrix;
    private final Map<Integer, Integer>  aliveShips;
    private final long gameDuration;

    public ResponseFire(String attacker, String opponent, int x, int y, FireResult result, char[][] matrix, Map<Integer, Integer> aliveShips, long gameDuration) {
        super(ResponseType.FIRE_RESULT);
        this.attacker = attacker;
        this.opponent = opponent;
        this.x = x;
        this.y = y;
        this.result = result;
        this.matrix = matrix;
        this.aliveShips = aliveShips;
        this.gameDuration = gameDuration;
    }

    public String getAttacker() {
        return attacker;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public String getOpponent() {
        return opponent;
    }

    public FireResult getResult() {
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Map<Integer, Integer>  getAliveShips() {
        return aliveShips;
    }

    public long getGameDuration() {
        return gameDuration;
    }
}
