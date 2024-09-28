package ru.isador.games.seabattle.services.commands;

public class ResponsePlayerLeaved extends Response {

    private final String playerName;

    public ResponsePlayerLeaved(String playerName) {
        super(ResponseType.PLAYER_LEAVED);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
