package ru.isador.games.seabattle.services.commands;

import java.util.Map;

public class ResponsePlayerJoined extends Response {

    private final String playerName;
    private final Map<Integer, Integer> aliveShips;

    public ResponsePlayerJoined(String playerName, Map<Integer, Integer> aliveShips) {
        super(ResponseType.PLAYER_JOINED);
        this.playerName = playerName;
        this.aliveShips = aliveShips;
    }

    public Map<Integer, Integer> getAliveShips() {
        return aliveShips;
    }

    public String getPlayerName() {
        return playerName;
    }
}
