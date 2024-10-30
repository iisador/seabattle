package ru.isador.games.seabattle.services.commands;

import java.util.Map;

public class ResponsePlayerJoined extends Response {

    private final String playerName;
    private final Map<Byte, Byte> aliveShips;

    public ResponsePlayerJoined(String playerName, Map<Byte, Byte> aliveShips) {
        super(ResponseType.PLAYER_JOINED);
        this.playerName = playerName;
        this.aliveShips = aliveShips;
    }

    public Map<Byte, Byte> getAliveShips() {
        return aliveShips;
    }

    public String getPlayerName() {
        return playerName;
    }
}
