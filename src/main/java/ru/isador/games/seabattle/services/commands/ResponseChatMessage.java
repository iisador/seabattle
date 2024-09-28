package ru.isador.games.seabattle.services.commands;

public class ResponseChatMessage extends Response {

    private final String playerName;
    private final String message;

    public ResponseChatMessage(String playedName, String message) {
        super(ResponseType.CHAT_MESSAGE);
        this.playerName = playedName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerName() {
        return playerName;
    }
}
